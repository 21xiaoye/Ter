package com.cabin.ter.strategy;

import com.cabin.ter.chat.domain.MessageDomain;
import com.cabin.ter.chat.mapper.MessageDomainMapper;
import com.cabin.ter.constants.enums.MessageTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiaoye
 * @date Created in 2024-06-02 16:10
 */
@Component
public class SystemMsgHandler extends AbstractMsgHandler<String>{
    @Autowired
    private MessageDomainMapper messageDomainMapper;
    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.SYSTEM;
    }

    @Override
    protected void saveMsg(MessageDomain message, String body) {
        MessageDomain messageDomain = new MessageDomain();
        messageDomain.setId(message.getId());
        messageDomain.setContent(body);

        messageDomainMapper.updateByMsgId(messageDomain);
    }

    @Override
    public Object showMsg(MessageDomain msg) {
        return msg.getContent();
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
