package com.cabin.ter.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * <p>
 *     通道初始化器
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-02 20:56
 */
@Service
public class NioWebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private WebSocketServerHandler webSocketServerHandler;

    @Autowired
    private HttpRequestHandler httpRequestHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //设置log监听器，并且日志级别为debug，方便观察运行流程
        ch.pipeline().addLast("logging", new LoggingHandler("DEBUG"));
        //设置解码器
        ch.pipeline().addLast("http-codec", new HttpServerCodec());
        //聚合器，使用websocket会用到 把HTTP头、HTTP体拼成完整的HTTP请求
        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
        //用于大数据的分区传输
        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        // 用于http 升级成 websocket
        ch.pipeline().addLast("http-handler", httpRequestHandler);
        //自定义的业务handler 处理websocket
        ch.pipeline().addLast("handler", webSocketServerHandler);
    }
}
