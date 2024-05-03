package com.cabin.ter.common.websocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SimpleWebSocketClient   {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        try {
            URI uri = new URI("ws://localhost:9004");
            for (int i = 0; i < 100; i++) {
                executorService.submit(() -> {
                    WebSocketClient client = new WebSocketClient(uri) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            log.info("Connected to port: 9004");
                            for (int j = 0; j < 2000; j++) { // 每个连接发送2000条消息
                                send("Message " + j);
                            }
                        }

                        @Override
                        public void onMessage(String message) {
                            log.info("Received: " + message);
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            log.info("Connection closed with exit code " + code + ", additional info: " + reason);
                        }

                        @Override
                        public void onError(Exception ex) {

                            log.error("An error occurred:" + ex);
                        }
                    };
                    client.connect();
                });
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
