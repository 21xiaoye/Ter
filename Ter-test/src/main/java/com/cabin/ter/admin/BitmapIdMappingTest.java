package com.cabin.ter.admin;

import cn.hutool.core.lang.Snowflake;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

class BitmapIdMapper {
    private final BitSet bitmap;
    private final int bitmapSize = (int)Math.pow(2,16);

    public BitmapIdMapper() {
        this.bitmap = new BitSet(bitmapSize);
    }
    public int mapId(long snowflakeId) {
        int hash = getHash(snowflakeId);
        int mappedId = hash % bitmapSize;

        while (bitmap.get(mappedId)) {
            mappedId = (mappedId + 1) % bitmapSize;
        }
        bitmap.set(mappedId);

        return mappedId;
    }

    private int getHash(long id) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(ByteBuffer.allocate(Long.BYTES).putLong(id).array());
            return Math.abs(ByteBuffer.wrap(hash).getInt());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

public class BitmapIdMappingTest {
    public static void main(String[] args) throws InterruptedException {
            BitmapIdMapper idMapper = new BitmapIdMapper();
            Snowflake idGenerator = new Snowflake();
            Set<Integer> integers = Collections.synchronizedSet(new HashSet<>());
            ExecutorService executorService = Executors.newFixedThreadPool(200);
            int numTasks = 250000;

            ConcurrentLinkedQueue<String> results = new ConcurrentLinkedQueue<>();
            CountDownLatch latch = new CountDownLatch(numTasks);

            for (int i = 0; i < numTasks; i++) {
                executorService.submit(() -> {
                    try {
                        long id = idGenerator.nextId();
                        int mappedId = idMapper.mapId(id);

                        if (!integers.add(mappedId)) {
                            System.out.println("数据已存在: " + mappedId);
                        }
                        results.add("Snowflake ID: " + id + " -> Mapped Bitmap Position: " + mappedId);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            executorService.shutdown();

            results.forEach(System.out::println);
            System.out.println("Unique IDs mapped: " + integers.size());
    }
}
