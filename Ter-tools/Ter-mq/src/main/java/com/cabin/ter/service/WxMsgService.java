package com.cabin.ter.service;


import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Service;


/**
 * @author xiaoye
 * @date Created in 2024-05-09 11:30
 */

@Service
public interface WxMsgService {
    WxMpXmlOutMessage scan(WxMpService wxMpService, WxMpXmlMessage wxMpXmlMessage);
    void authorize(WxOAuth2UserInfo userInfo);
}
