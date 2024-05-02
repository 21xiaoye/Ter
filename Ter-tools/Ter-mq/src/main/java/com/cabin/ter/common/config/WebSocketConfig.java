package com.cabin.ter.common.config;

import com.cabin.ter.common.constants.entity.ws.ServerInfo;
import com.cabin.ter.common.util.CacheUtil;
import com.cabin.ter.common.util.RedisUtil;
import com.cabin.ter.common.websocket.WebsocketServer;
import com.cabin.ter.constants.enums.Status;
import com.cabin.ter.constants.vo.response.ApiResponse;
import io.netty.channel.Channel;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>
 *     开启websocket服务
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 01:05
 */
@Component
@Slf4j
public class WebSocketConfig {
    @Value("${netty.port}")
    private int port;

    @Autowired
    private WebsocketServer websocketServer;

    private static ExecutorService executorService = Executors.newCachedThreadPool();


    @PostConstruct
    public void start() throws InterruptedException {
        try {

            log.info("启动Netty服务，获取可用端口：{}", port);
            Future<Channel> future = executorService.submit(websocketServer);
            Channel channel = future.get();
            if (null == channel) {
                throw new RuntimeException("netty server open error channel is null");
            }
            while (!channel.isActive()) {
                log.info("启动Netty服务，循环等待启动...");
                Thread.sleep(500);
            }
            // 放入 缓存
            CacheUtil.serverInfoMap.put(port, new ServerInfo("127.0.0.1", port, new Date()));
            CacheUtil.serverMap.put(port, websocketServer);

            log.info("启动Netty服务，完成：{}", channel.localAddress());

        } catch (Exception e) {
            log.error("启动Netty服务失败", e);
        }
    }

    @PreDestroy
    public void clear() {
        try {
            log.info("关闭Netty服务开始，端口：{}", port);
            WebsocketServer websocketServer = CacheUtil.serverMap.get(port);
            if (null == websocketServer) {
                CacheUtil.serverMap.remove(port);
            }
            websocketServer.destroy();
            CacheUtil.serverMap.remove(port);
            CacheUtil.serverInfoMap.remove(port);
            log.info("关闭Netty服务完成，端口：{}", port);
        } catch (Exception e) {
            log.error("关闭Netty服务失败，端口：{}", port, e);
        }
    }
}
