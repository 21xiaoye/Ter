package com.cabin.ter.adapter;

import com.alibaba.fastjson.JSON;
import com.cabin.ter.constants.dto.LoginMessageDTO;
import com.cabin.ter.constants.dto.MsgSendMessageDTO;
import com.cabin.ter.constants.dto.PushMessageDTO;
import com.cabin.ter.constants.dto.ScanSuccessMessageDTO;
import com.cabin.ter.constants.enums.EmailTypeEnum;
import com.cabin.ter.constants.enums.SourceEnum;
import com.cabin.ter.constants.dto.EmailMessageDTO;
import com.cabin.ter.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.constants.vo.response.WSBaseResp;

import java.util.List;

/**
 * <p>
 *     消息队列消息构建类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 01:12
 */

public class MQMessageBuilderAdapter {

    public static WebSocketSingleParticipant buildMsg(String channelId, String content) {
        WebSocketSingleParticipant webSocketSingleParticipant = new WebSocketSingleParticipant();
        webSocketSingleParticipant.setToAddress(channelId);
        webSocketSingleParticipant.setContent(content);
        return webSocketSingleParticipant;
    }
    public static LoginMessageDTO buildLoginMessage(String openId, String email, Integer loginCode,SourceEnum source){
        LoginMessageDTO loginMessageDTO = new LoginMessageDTO();
        loginMessageDTO.setLoginEmail(email);
        loginMessageDTO.setOpenId(openId);
        loginMessageDTO.setLogonCode(loginCode);
        loginMessageDTO.setSource(source);
        return loginMessageDTO;
    }
    public static ScanSuccessMessageDTO buildScanSuccessMessage(String openId, Integer loginCode){
        ScanSuccessMessageDTO scanSuccessMessageDTO = new ScanSuccessMessageDTO();
        scanSuccessMessageDTO.setOpenId(openId);
        scanSuccessMessageDTO.setLoginCode(loginCode);
        return scanSuccessMessageDTO;
    }
    public static PushMessageDTO buildPushMessage(WSBaseResp<?> wsBaseMsg,List<Long> uidList,Integer pushType){
        PushMessageDTO pushMessageDTO = new PushMessageDTO();
        pushMessageDTO.setWsBaseMsg(wsBaseMsg);
        pushMessageDTO.setUidList(uidList);
        pushMessageDTO.setPushType(pushType);
        return pushMessageDTO;
    }
    public static MsgSendMessageDTO buildMsgSendMessage(Long msgId){
        MsgSendMessageDTO msgSendMessageDTO = new MsgSendMessageDTO();
        msgSendMessageDTO.setMsgId(msgId);
        return msgSendMessageDTO;
    }
    public static EmailMessageDTO buildEmailMessageDTO(String subject, String address, String content, EmailTypeEnum emailType, SourceEnum source){
        EmailMessageDTO emailMessageParticipant = new EmailMessageDTO();
        emailMessageParticipant.setSubject(subject);
        emailMessageParticipant.setContent(content);
        emailMessageParticipant.setToAddress(address);
        emailMessageParticipant.setEmailType(emailType);
        emailMessageParticipant.setSource(source);
        return emailMessageParticipant;
    }
    public static WebSocketSingleParticipant json2Obj(String objJsonStr) {
        return JSON.parseObject(objJsonStr, WebSocketSingleParticipant.class);
    }

    public static String obj2Json(WebSocketSingleParticipant msgAgreement) {
        return JSON.toJSONString(msgAgreement);
    }

}