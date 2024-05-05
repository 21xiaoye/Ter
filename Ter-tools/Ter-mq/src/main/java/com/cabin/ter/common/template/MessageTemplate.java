package com.cabin.ter.common.template;

import com.cabin.ter.common.constants.participant.msg.MessageParticipant;
import com.cabin.ter.common.service.MessageProcessing;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *     消息发送接口
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 12:55
 */
@Slf4j
public abstract class MessageTemplate {
    private MessageProcessing messageProcessing;
    public MessageTemplate(){
    }
    public MessageTemplate(MessageProcessing messageProcessing) {
        this.messageProcessing = messageProcessing;
    }

    public MessageProcessing getMessageProcessing() {
        return messageProcessing;
    }

    public void setMessageProcessing(MessageProcessing messageProcessing) {
        this.messageProcessing = messageProcessing;
    }

    /**
     * 以下代码：
     *
     * 最初想的是在消息发送利用模板模式进行一些公共处理，
     * 但这个公共处理暂时还没想好
     */
    /**
     * 对消息进行初始化、风控
     *
     * @param message
     * @param <T>
     */
    public <T extends MessageParticipant>  void messageTemplate(MessageParticipant message)  {
        this.messageRisk(message);
        this.initMessage(message);
    }

    /**
     * 初始化消息
     *
     * @param message
     * @param <T>
     */
    protected <T extends MessageParticipant> void initMessage(MessageParticipant message){}

    /**
     * 审核消息内容，具体实现由子类完成
     *
     * @param message
     * @param <T>
     */
    protected <T extends MessageParticipant> void messageRisk(MessageParticipant message){}

    /**
     * 执行消息推送
     *
     * @return
     */
    protected abstract <T extends MessageParticipant> Boolean messageSend(MessageParticipant message);

}
