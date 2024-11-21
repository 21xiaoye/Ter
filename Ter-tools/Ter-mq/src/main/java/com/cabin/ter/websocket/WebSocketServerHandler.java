package com.cabin.ter.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cabin.ter.constants.enums.WSReqTypeEnum;
import com.cabin.ter.constants.request.WSAuthorize;
import com.cabin.ter.constants.request.WsReqMsg;
import com.cabin.ter.service.WebSocketPublicService;
import com.cabin.ter.util.CacheUtil;
import com.cabin.ter.adapter.MQMessageBuilderAdapter;
import com.cabin.ter.util.NettyUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *     处理接受消息类，上下线业务逻辑的handler
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 01:16
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    @Autowired
    private WebSocketPublicService webSocketPublicService;


    /**
     * 读取 消息
     *
     * @param ctx
     * @param frame
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            ctx.channel().close();
            return;
        }

        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (!(frame instanceof TextWebSocketFrame)) {
            sendErrorMessage(ctx, "仅支持文本(Text)格式，不支持二进制消息");
        }

        try {
            assert frame instanceof TextWebSocketFrame;
            String request = ((TextWebSocketFrame) frame).text();
            log.info("服务端收到新信息：" + request);
            WsReqMsg reqMsg = JSONUtil.toBean(request, WsReqMsg.class);
            WSReqTypeEnum wsReqTypeEnum = WSReqTypeEnum.of(reqMsg.getType());

            switch (wsReqTypeEnum){
                case WX_LOGIN -> {
                    webSocketPublicService.handleLoginReq(ctx.channel());
                }
                // 心跳检测，直接跳过
                case HEARTBEAT,AUTHORIZE -> {
                    log.info("心跳检测");
                }
                default -> {
                    log.info("未知类型");
                }
            }
        }catch (Exception e){
            Channel channel = ctx.channel();
            channel.writeAndFlush(new TextWebSocketFrame("参数错误"));
            throw new RuntimeException("参数错误，json转换失败");
        }
    }
    private void userOffLine(ChannelHandlerContext ctx) {
        this.webSocketPublicService.removed(ctx.channel());
        ctx.channel().close();
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent idleStateEvent) {
            // 读空闲
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 关闭用户的连接
                userOffLine(ctx);
            }
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            this.webSocketPublicService.connect(ctx.channel());
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
            log.info("用户握手成功，开始进行授权");
            if (StrUtil.isNotBlank(token)) {
                this.webSocketPublicService.authorize(ctx.channel(), new WSAuthorize(token));
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 捕获异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
        ctx.close();
        //  清除缓存
        CacheUtil.cacheChannel.remove(ctx.channel().id().toString(), ctx.channel());
        log.error("异常信息：\r\n" + cause.getMessage());
    }

    /**
     * 当客户端主动链接服务端的链接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx)   {
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("客户端上线");
        log.info("客户端信息：有一客户端链接到本服务端。channelId：" + channel.id());
        log.info("客户端IP:" + channel.localAddress().getHostString());
        log.info("客户端Port:" + channel.localAddress().getPort());
        log.info("客户端信息完毕");

        this.webSocketPublicService.connect(ctx.channel());
//        SendChannelInfo userChannelInfo = new SendChannelInfo(channel.localAddress().getHostString(),
//                channel.localAddress().getPort(), channel.id().toString(), new Date());

//        redisUtil.pushObj(userChannelInfo);
        CacheUtil.cacheChannel.put(channel.id().toString(), channel);

        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ctx.writeAndFlush(MQMessageBuilderAdapter.buildMsg(channel.id().toString(), str));
    }


    /**
     * 客户端断开链接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        userOffLine(ctx);
        log.info("客户端断开链接" + ctx.channel().localAddress().toString());
        CacheUtil.cacheChannel.remove(ctx.channel().id().toString(), ctx.channel());
    }


    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(errorMsg));
    }
}
