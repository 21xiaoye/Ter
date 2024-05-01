package com.cabin.ter.common.service;

import com.cabin.ter.common.constants.entity.MessageParticipant;

/**
 * 这个类是一些公共处理，暂未想好怎么使用
 * 这里我的想法是，由用户来进行消息发送初始化和风控的公共接口
 *
 * @author xiaoye
 * @date Created in 2024-05-01 19:47
 */
public interface MessageProcessing {

     <T extends MessageParticipant> void initMessage(MessageParticipant message);
     <T extends MessageParticipant> void messageRisk(MessageParticipant message);
}
