package com.cabin.ter.common.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.concurrent.Callable;

/**
 * <p>
 *     websocket 服务端
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 01:14
 */
@Slf4j
@Service
@Data
public class WebsocketServer implements Callable<Channel> {


    private int port;

    //配置服务端NIO线程组
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();


    @Autowired
    private NioWebSocketChannelInitializer nioWebSocketChannelInitializer;

    private Channel channel;
    @Override
    public Channel call()  {
        ChannelFuture channelFuture;
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            //boss辅助客户端的tcp连接请求  worker负责与客户端之前的读写操作
            serverBootstrap.group(bossGroup, workerGroup)
                    //配置客户端的channel类型
                    .channel(NioServerSocketChannel.class)
                    //配置TCP参数，握手字符串长度设置
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //TCP_NODELAY算法，尽可能发送大块数据，减少充斥的小块数据
                    .option(ChannelOption.TCP_NODELAY, true)
                    //开启心跳包活机制，就是客户端、服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //配置固定长度接收缓存区分配器
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))
                    // websocket 初始化 handler
                    .childHandler(nioWebSocketChannelInitializer);

            log.info("Netty Websocket服务器启动完成，已绑定端口 " + port + " 阻塞式等候客户端连接");

            channelFuture = serverBootstrap.bind(port).sync();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return channel;
    }


    /**
     * 摧毁
     */
    public void destroy() {
        if (null == channel) {
            return;
        }
        channel.close();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}

