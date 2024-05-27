package com.cabin.ter.service;

import com.cabin.ter.constants.dto.EmailBindingDTO;
import com.cabin.ter.constants.vo.request.WSAuthorize;
import com.cabin.ter.constants.vo.response.WSBaseResp;
import io.netty.channel.Channel;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *     处理本地连接服务，列获取验证码、实时数据等
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 20:32
 */
public interface WebSocketPublicService {
    /**
     * 处理用户登录请求，需要返回一张带code的二维码
     *
     * @param channel
     */
    void handleLoginReq(Channel channel) throws WxErrorException;

    /**
     * 处理所有ws连接的事件
     *
     * @param channel
     */
    void connect(Channel channel);

    /**
     * 处理ws断开连接的事件
     *
     * @param channel
     */
    void removed(Channel channel);

    /**
     * 主动认证登录
     *
     * @param channel
     * @param wsAuthorize
     */
    void authorize(Channel channel, WSAuthorize wsAuthorize);

    /**
     * 扫码用户登录成功通知,清除本地Cache中的loginCode和channel的关系
     */
    Boolean scanLoginSuccess(String openId,Integer loginCode, String loginEmail);

    /**
     * 通知用户扫码成功
     *
     * @param loginCode
     */
    Boolean scanSuccess(Integer loginCode);

    Boolean emailBinding(EmailBindingDTO emailBindingDTO);

//    /**
//     * 推动消息给所有在线的人
//     *
//     * @param wsBaseResp 发送的消息体
//     * @param skipUid    需要跳过的人
//     */
//    void sendToAllOnline(WSBaseResp<?> wsBaseResp, Long skipUid);
//
//    /**
//     * 推动消息给所有在线的人
//     *
//     * @param wsBaseResp 发送的消息体
//     */
//    void sendToAllOnline(WSBaseResp<?> wsBaseResp);
//
    void sendToUid(WSBaseResp<?> wsBaseResp, Long uid);
}
