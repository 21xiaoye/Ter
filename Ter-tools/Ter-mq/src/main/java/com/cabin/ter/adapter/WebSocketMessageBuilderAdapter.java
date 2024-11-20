package com.cabin.ter.adapter;

import com.cabin.ter.admin.domain.UserDomain;
import com.cabin.ter.constants.dto.ScanSuccessMessageDTO;
import com.cabin.ter.constants.enums.WSRespTypeEnum;
import com.cabin.ter.constants.vo.response.*;
import com.cabin.ter.listener.listener.UserOnlineListener;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     消息适配器
 * </p>
 * @author xiaoye
 * @date Created in 2024-05-10 10:16
 */
@Component
public class WebSocketMessageBuilderAdapter {
    /**
     * 组装用户二维码url
     *
     * @param wxMpQrCodeTicket
     * @return
     */
    public static WSBaseResp<WsLoginUrlResp> buildLoginResp(WxMpQrCodeTicket wxMpQrCodeTicket ){
        WSBaseResp<WsLoginUrlResp> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        wsBaseResp.setData(WsLoginUrlResp.builder().loginUrl(wxMpQrCodeTicket.getUrl()).build());
        return wsBaseResp;
    }

    /**
     * 用户扫码成功
     * @return
     */
    public static WSBaseResp<ScanSuccessMessageDTO> buildScanSuccessResp() {
        WSBaseResp<ScanSuccessMessageDTO> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return wsBaseResp;
    }
    /**
     * 用户登录成功
     * @param userDomain
     * @param token
     * @return
     */
    public static WSBaseResp<WSLoginSuccess> buildLoginSuccessResp(UserDomain userDomain, String token){
        WSBaseResp<WSLoginSuccess> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess wsLoginSuccess = WSLoginSuccess.builder()
                .avatar(userDomain.getUserAvatar())
                .name(userDomain.getUserName())
                .token(token)
                .uId(userDomain.getUserId())
                .build();
        wsBaseResp.setData(wsLoginSuccess);
        return wsBaseResp;
    }

    public static WSBaseResp<Void> buildInvalidateTokenResp() {
        WSBaseResp<Void> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return wsBaseResp;
    }
    public static WSBaseResp<WSApplyUserInfoResp> buildApplyUserInfoResp(WSApplyUserInfoResp applyUserInfoResp){
        WSBaseResp<WSApplyUserInfoResp> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.APPLY.getType());
        wsBaseResp.setData(applyUserInfoResp);
        return wsBaseResp;
    }
    public static WSBaseResp<UserOfflineResp> buildUserOfflineResp(Long userId){
        WSBaseResp<UserOfflineResp> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.USER_OFFLINE.getType());
        wsBaseResp.setData(UserOfflineResp.builder().userId(userId).build());
        return wsBaseResp;
    }
    public static WSBaseResp<UserOnlineResp> buildUserOnlineResp(Long userId){
        WSBaseResp<UserOnlineResp> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSRespTypeEnum.USER_ONLINE.getType());
        wsBaseResp.setData(UserOnlineResp.builder().userId(userId).build());
        return wsBaseResp;
    }
}
