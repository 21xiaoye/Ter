package com.cabin.ter.handler;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <P>
 *     记录所有请求信息
 * </P>
 *
 * @author xiaoye
 * @date Created in 2024-05-19 11:38
 */
@Slf4j
@Component
public class LogHandler extends AbstractHandler{
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        log.info("\n接收到请求消息，内容：{}", JSONUtil.toJsonStr(wxMpXmlMessage));
        return null;
    }
}
