package com.cabin.ter.service.impl;

import com.cabin.ter.service.ThymeleafPageRenderingService;
import com.cabin.ter.util.TemporaryLinkUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ThymeleafPageRenderingServiceImpl implements ThymeleafPageRenderingService {
    private WxMpService wxMpService;
    // TODO:这里我不知道哪里造成循环依赖，目前使用@Lazy来进行处理
    @Lazy
    @Autowired
    public void setWxMpService(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
    }
    public void emailBindingRenderingPage(String openId){
        String temporaryUserEmailBindingLink = TemporaryLinkUtil.buildTemporaryUserEmailBindingLink(openId);
        log.info("生成临时绑定邮箱链接{}",temporaryUserEmailBindingLink);
        String messageContent = "请点击以下链接绑定您的邮箱：<a href=\"" + temporaryUserEmailBindingLink + "\">绑定邮箱</a>";
        WxMpKefuMessage wxMpKefuMessage = WxMpKefuMessage.TEXT()
                .toUser(openId)
                .content(messageContent)
                .build();
        try {
            wxMpService.getKefuService().sendKefuMessage(wxMpKefuMessage);
        } catch (Exception e) {
            log.error("发送绑定邮箱提醒消息失败", e);
        }
    }
}
