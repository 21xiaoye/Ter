package com.cabin.ter.service;


import com.cabin.ter.constants.request.UserEmailBindingReq;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


/**
 * <p>
 *     处理用户扫码和授权事件：
 *          用户扫码之后，向用户发送一个授权链接，当用户授权完成之后，
 *          会执行 authorize 方法，在此方法中可以处理用户授权之后的操作，
 *          我这里等待用户授权完成后，保存微信返回的用户信息，通知用户绑定邮箱，
 *          用户绑定邮箱之后，完成注册操作。
 *
 *          如果用户在此之前，已经使用微信进行授权，则直接登录成功。
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-09 11:30
 */

@Service
public interface WxMsgService {
    WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage) throws UnsupportedEncodingException;
    void authorize(WxOAuth2UserInfo userInfo);
    void emailBinding(UserEmailBindingReq userEmailBindingReq);
}
