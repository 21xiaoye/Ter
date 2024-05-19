package com.cabin.ter.handler;

import com.cabin.ter.service.WxMsgService;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * <p>
 *     微信扫码事件
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-11 10:09
 */

@Component
public class ScanHandler extends AbstractHandler{
    @Autowired
    private WxMsgService wxMsgService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager)   {
        try {
            return wxMsgService.scan(wxMpService,wxMpXmlMessage);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
