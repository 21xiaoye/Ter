package com.cabin.ter.common.websocket;


import com.cabin.ter.common.constants.entity.msg.WebSocketParticipant;
import com.cabin.ter.common.constants.entity.ws.SendChannelInfo;
import com.cabin.ter.common.util.CacheUtil;
import com.cabin.ter.common.util.MsgUtil;
import com.cabin.ter.common.util.RedisUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    private RedisUtil redisUtil;

    /**
     * 读取 消息
     *
     * @param ctx
     * @param frame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            ctx.channel().close();
            return;
        }
        // ping请求
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 只支持文本格式，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            sendErrorMessage(ctx, "仅支持文本(Text)格式，不支持二进制消息");
        }

        // 客服端发送过来的消息
        String request = ((TextWebSocketFrame) frame).text();
        log.info("服务端收到新信息：" + request);
        try {
            //接收msg消息{此处已经不需要自己进行解码}
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息内容：" + request);
            WebSocketParticipant msgAgreement = MsgUtil.json2Obj(request.toString());

            String toChannelId = msgAgreement.getChannelId();
            //判断接收消息用户是否在本服务端
            Channel channel = CacheUtil.cacheChannel.get(toChannelId);
            if (null != channel) {
                channel.writeAndFlush(new TextWebSocketFrame(MsgUtil.obj2Json(msgAgreement) + " lalalalalalalalalalalalal"));
                return;
            }

            //如果为NULL则接收消息的用户不在本服务端，需要push消息给全局
            log.info("接收消息的用户不在本服务端，PUSH！");
            redisUtil.push("uav-flight-message", MsgUtil.obj2Json(msgAgreement));


        } catch (Exception e) {
            sendErrorMessage(ctx, "JSON字符串转换出错！");
            e.printStackTrace();
        }
    }

    /**
     * 心跳检查
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        // 1. 清除 redis
        redisUtil.remove(ctx.channel().id().toString());
        // 2. 清除缓存
        CacheUtil.cacheChannel.remove(ctx.channel().id().toString(), ctx.channel());
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }

    /**
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("客户端上线");
        System.out.println("客户端信息：有一客户端链接到本服务端。channelId：" + channel.id());
        System.out.println("客户端IP:" + channel.localAddress().getHostString());
        System.out.println("客户端Port:" + channel.localAddress().getPort());
        System.out.println("客户端信息完毕");

        //保存用户信息
        SendChannelInfo userChannelInfo = new SendChannelInfo(channel.localAddress().getHostString(),
                channel.localAddress().getPort(), channel.id().toString(), new Date());
        // 放入 redis 和 缓存
        redisUtil.pushObj(userChannelInfo);
        CacheUtil.cacheChannel.put(channel.id().toString(), channel);
        //通知客户端链接建立成功
        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ctx.writeAndFlush(MsgUtil.buildMsg(channel.id().toString(), str));
    }


    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开链接" + ctx.channel().localAddress().toString());
        // 移除 redis  和 缓存
        redisUtil.remove(ctx.channel().id().toString());
        CacheUtil.cacheChannel.remove(ctx.channel().id().toString(), ctx.channel());
    }


    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        String responseJson = "不支持二进制消息";
        ctx.channel().writeAndFlush(new TextWebSocketFrame(responseJson));
    }

}
