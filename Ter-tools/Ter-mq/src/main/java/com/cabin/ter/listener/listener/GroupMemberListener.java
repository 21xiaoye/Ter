package com.cabin.ter.listener.listener;

import com.cabin.ter.template.RocketMQEnhanceTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 *     向群组邀请成员发送邀请通知
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-29 10:39
 */
@Component
@Slf4j
public class GroupMemberListener {
    private RocketMQEnhanceTemplate rocketMQEnhanceTemplate;

}
