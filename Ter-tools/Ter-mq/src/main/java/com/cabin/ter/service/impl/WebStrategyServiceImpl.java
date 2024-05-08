package com.cabin.ter.service.impl;

import com.cabin.ter.constants.participant.msg.MessageParticipant;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.service.BaseMessageStrategyService;
import com.cabin.ter.template.MessageTemplate;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     websocket 推送消息策略类
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-03 21:45
 */
@Component
public class WebStrategyServiceImpl extends MessageTemplate
        implements BaseMessageStrategyService {
    @Override
    public <T extends MessageParticipant> Boolean messageStrategy(MessageParticipant message) {
        return this.messageSend(message);
    }

    @Override
    public MessagePushMethodEnum getSource() {
        return MessagePushMethodEnum.USER_WEB_MESSAGE;
    }

    @Override
    protected <T extends MessageParticipant> Boolean messageSend(MessageParticipant message) {

        return null;
    }
}
