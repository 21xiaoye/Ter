package com.cabin.ter.adapter;

import com.cabin.ter.constants.enums.WSRespTypeEnum;
import com.cabin.ter.constants.vo.response.WSBaseResp;
import com.cabin.ter.constants.vo.response.WsLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

/**
 * <p>
 *     消息适配器
 * </p>
 * @author xiaoye
 * @date Created in 2024-05-10 10:16
 */
public class WSAdapter {
    public static WSBaseResp<Object> buildLoginResp(WxMpQrCodeTicket wxMpQrCodeTicket){
        WSBaseResp<Object> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        wsBaseResp.setData(WsLoginUrl.builder().loginUrl(wxMpQrCodeTicket.getUrl()).build());
        return wsBaseResp;
    }

    public static WSBaseResp buildScanSuccessResp() {
        WSBaseResp wsBaseResp = new WSBaseResp();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return wsBaseResp;
    }
}
