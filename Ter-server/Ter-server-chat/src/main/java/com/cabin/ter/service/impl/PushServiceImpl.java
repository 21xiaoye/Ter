package com.cabin.ter.service.impl;

import com.cabin.ter.constants.dto.PushMessageDTO;
import com.cabin.ter.constants.participant.constant.TopicConstant;
import com.cabin.ter.constants.vo.response.WSBaseResp;
import com.cabin.ter.service.PushService;
import com.cabin.ter.template.RocketMQEnhanceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xiaoye
 * @date Created in 2024-05-30 13；11
 */
@Service
public class PushServiceImpl implements PushService {
    @Autowired
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;
    @Override
    public void sendPushMsg(WSBaseResp<?> msg, List<Long> uidList) {
        rocketMQEnhanceTemplate.send(TopicConstant.PUSH_TOPIC, new PushMessageDTO(uidList, msg));
    }
    @Override
    public void sendPushMsg(WSBaseResp<?> msg, Long uid) {
        rocketMQEnhanceTemplate.send(TopicConstant.PUSH_TOPIC, new PushMessageDTO(uid, msg));
    }
    @Override
    public void sendPushMsg(WSBaseResp<?> msg) {
        rocketMQEnhanceTemplate.send(TopicConstant.PUSH_TOPIC, new PushMessageDTO(msg));
    }
}
