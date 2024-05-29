package com.cabin.ter.strategy;

import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.constants.enums.MessageTypeEnum;
import com.cabin.ter.vo.request.TextMsgReq;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     文本消息处理程序
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-29 15:37
 */
@Component
public class TextMsgHandler extends AbstractMsgHandler<TextMsgReq> {
    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.TEXT;
    }

    @Override
    protected void saveMsg(MessageDomain message, TextMsgReq body) {

    }

    @Override
    public Object showMsg(MessageDomain msg) {
        return null;
    }

    @Override
    public Object showReplyMsg(MessageDomain msg) {
        return msg.getContent();
    }

    @Override
    public String showContactMsg(MessageDomain msg) {
        return msg.getContent();
    }
}
