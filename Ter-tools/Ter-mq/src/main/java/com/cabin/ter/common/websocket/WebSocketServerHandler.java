package com.cabin.ter.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import com.cabin.ter.common.constants.entity.msg.WebSocketParticipant;
import com.cabin.ter.common.constants.entity.ws.SendChannelInfo;
import com.cabin.ter.common.constants.enums.ClusterTopicEnum;
import com.cabin.ter.common.service.WebSocketService;
import com.cabin.ter.common.util.CacheUtil;
import com.cabin.ter.common.util.MsgUtil;
import com.cabin.ter.common.util.RedisUtil;
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


        String request = ((TextWebSocketFrame) frame).text();
        log.info("服务端收到新信息：" + request);
        try {


            log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息内容：" + request);
//            JSONUtil.toBean()

            WebSocketParticipant msgAgreement = MsgUtil.json2Obj(request.toString());

            String toChannelId = msgAgreement.getChannelId();

            Channel channel = CacheUtil.cacheChannel.get(toChannelId);
            if (null != channel) {
                channel.writeAndFlush(new TextWebSocketFrame(MsgUtil.obj2Json(msgAgreement) + " lalalalalalalalalalalalal"));
                return;
            }


            log.info("接收消息的用户不在本服务端，PUSH！");
            redisUtil.push(ClusterTopicEnum.REDIS_USER_MESSAGE_PUSH.getMessage(), MsgUtil.obj2Json(msgAgreement));

            if (null != channel) {
                channel.writeAndFlush(new TextWebSocketFrame(request+" lalalalalalalalalalalalal"));
            }

        } catch (Exception e) {
            sendErrorMessage(ctx, "JSON字符串转换出错！");
            e.printStackTrace();
        }
    }

    /**
     * 捕获异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
        ctx.close();
        //  清除 redis
        redisUtil.remove(ctx.channel().id().toString());
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
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("客户端上线");
        log.info("客户端信息：有一客户端链接到本服务端。channelId：" + channel.id());
        log.info("客户端IP:" + channel.localAddress().getHostString());
        log.info("客户端Port:" + channel.localAddress().getPort());
        log.info("客户端信息完毕");


        SendChannelInfo userChannelInfo = new SendChannelInfo(channel.localAddress().getHostString(),
                channel.localAddress().getPort(), channel.id().toString(), new Date());

        redisUtil.pushObj(userChannelInfo);
        CacheUtil.cacheChannel.put(channel.id().toString(), channel);

        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ctx.writeAndFlush(MsgUtil.buildMsg(channel.id().toString(), str));
    }


    /**
     * 客户端断开链接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        log.info("客户端断开链接" + ctx.channel().localAddress().toString());
        redisUtil.remove(ctx.channel().id().toString());
        CacheUtil.cacheChannel.remove(ctx.channel().id().toString(), ctx.channel());
    }


    private void sendErrorMessage(ChannelHandlerContext ctx, String errorMsg) {
        String responseJson = "不支持二进制消息";
        ctx.channel().writeAndFlush(new TextWebSocketFrame(responseJson));
    }
}
