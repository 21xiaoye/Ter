package com.cabin.ter.adapter;
import com.cabin.ter.constants.dto.*;
import com.cabin.ter.constants.enums.EmailTypeEnum;
import com.cabin.ter.constants.enums.SourceEnum;
import com.cabin.ter.constants.participant.msg.WebSocketSingleParticipant;
import com.cabin.ter.constants.response.WSBaseResp;

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
    public static EmailMessageDTO buildEmailMessageDTO(String subject, String address, String content, EmailTypeEnum emailType, SourceEnum source){
        EmailMessageDTO emailMessageParticipant = new EmailMessageDTO();
        emailMessageParticipant.setSubject(subject);
        emailMessageParticipant.setContent(content);
        emailMessageParticipant.setToAddress(address);
        emailMessageParticipant.setEmailType(emailType);
        emailMessageParticipant.setSource(source);
        return emailMessageParticipant;
    }
    public static UserOfflineNotifyDTO buildUserOfflineNotifyDTO(Long userId, Long offlineTime){
        UserOfflineNotifyDTO userOfflineNotifyDTO = new UserOfflineNotifyDTO();
        userOfflineNotifyDTO.setUserId(userId);
        userOfflineNotifyDTO.setOfflineTime(offlineTime);
        userOfflineNotifyDTO.setSource(SourceEnum.USER_OFFLINE_SOURCE);
        return userOfflineNotifyDTO;
    }
    public static UserOnlineNotifyDTO buildUserOnlineNotifyDTO(Long userId, Long onlineTime){
        UserOnlineNotifyDTO userOnlineNotifyDTO = new UserOnlineNotifyDTO();
        userOnlineNotifyDTO.setUserId(userId);
        userOnlineNotifyDTO.setOnlineTime(onlineTime);
        userOnlineNotifyDTO.setSource(SourceEnum.USER_ONLINE_SOURCE);
        return userOnlineNotifyDTO;
    }
    public static ChatMessageDTO buildChatMessageDTO(WSBaseResp<?> wsBaseResp, List<Long> userIdList){
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setWsBaseResp(wsBaseResp);
        chatMessageDTO.setUserIdList(userIdList);
        chatMessageDTO.setSource(SourceEnum.CHAT_MESSAGE_SOURCE);
        return chatMessageDTO;
    }
}