package com.cabin.ter.websocket;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cabin.ter.adapter.WSAdapter;
import com.cabin.ter.constants.vo.request.WSAuthorize;
import com.cabin.ter.service.WebSocketPublicService;
import com.cabin.ter.util.JwtUtil;
import com.cabin.ter.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * <p>
 *     http协议升级
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 01:15
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class HttpRequestHandler extends SimpleChannelInboundHandler<Object> {
    @Autowired
    private WebSocketPublicService webSocketPublicService;

    /**
     * 读取 数据
     * 描述：读取完连接的消息后，对消息进行处理。
     * 这里仅处理HTTP请求，WebSocket请求交给下一个处理器。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)  {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            ctx.fireChannelRead(((WebSocketFrame) msg).retain());
        }
    }

    /**
     * 描述：处理Http请求，主要是完成HTTP协议到Websocket协议的升级
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req,
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws:/" + ctx.channel() + "/websocket", null, false);
        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
        this.parseIpToken(ctx,req);
    }

    /**
     * 完成websocket协议升级之后，获取此次链接的Ip地址和token验证
     *
     * @param ctx
     * @param request
     */
    private void parseIpToken(ChannelHandlerContext ctx,FullHttpRequest request){
        UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.uri());
        // 获取token参数
        String token = Optional.ofNullable(urlBuilder.getQuery()).map(k->k.get("token")).map(CharSequence::toString).orElse("");
        NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, token);

        // 获取请求路径
        request.setUri(urlBuilder.getPath().toString());
        HttpHeaders headers = request.headers();
        String ip = headers.get("X-Real-IP");
        if (StringUtils.isEmpty(ip)) {//如果没经过nginx，就直接获取远端地址
            InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
            ip = address.getAddress().getHostAddress();
        }
        NettyUtil.setAttr(ctx.channel(), NettyUtil.IP, ip);
        if (StrUtil.isNotBlank(token)) {
            this.webSocketPublicService.authorize(ctx.channel(), new WSAuthorize(NettyUtil.getAttr(ctx.channel(),NettyUtil.TOKEN)));
        }
    }
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        // 如果是非Keep-Alive，关闭连接
        boolean keepAlive = HttpUtil.isKeepAlive(req);
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!keepAlive) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 描述：异常处理，关闭channel
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

