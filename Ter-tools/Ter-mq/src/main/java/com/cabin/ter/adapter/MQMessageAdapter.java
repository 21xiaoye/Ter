package com.cabin.ter.adapter;

import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.dto.MsgSendMessageDTO;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.constants.enums.SourceEnum;

public class MQMessageAdapter {
    public static MQBaseMessage msgSendMessageBuild(MsgSendMessageDTO msgSendMessageDTO){
        msgSendMessageDTO.setKey(SourceEnum.MSG_SEND_MESSAGE_SOURCE.getSource());
        msgSendMessageDTO.setPushMethod(MessagePushMethodEnum.USER_WEB_MESSAGE);
        return msgSendMessageDTO;
    }
}
