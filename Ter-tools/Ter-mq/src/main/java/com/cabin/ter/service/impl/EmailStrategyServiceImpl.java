package com.cabin.ter.service.impl;

import com.cabin.ter.config.MailSenderConfig;
import com.cabin.ter.constants.participant.msg.EmailParticipant;
import com.cabin.ter.constants.participant.msg.MessageParticipant;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.service.BaseMessageStrategyService;
import com.cabin.ter.template.MessageTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 *     邮箱推送策略
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 21:31
 */
@Component
@Slf4j
public class EmailStrategyServiceImpl extends MessageTemplate
        implements BaseMessageStrategyService {
    @Autowired
    private MailSenderConfig mailSenderConfig;

    @Override
    protected <T extends MessageParticipant> Boolean messageSend(MessageParticipant message) {

        JavaMailSenderImpl sender = mailSenderConfig.getSender();
        MimeMessage mimeMessage = sender.createMimeMessage();
        try {
            EmailParticipant emailParticipant =(EmailParticipant) message;
            System.out.println("消费的消息内容为"+emailParticipant);
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setText(emailParticipant.getContent(),true);
            helper.setTo(emailParticipant.getTo());
            helper.setSubject(emailParticipant.getSubject());

            helper.setFrom(Objects.requireNonNull(sender.getUsername()));
        }catch (MessagingException exception){
            throw new RuntimeException("邮件发送失败"+exception);
        }
        sender.send(mimeMessage);
        return Boolean.TRUE;
    }

    @Override
    public MessagePushMethodEnum getSource() {
        return MessagePushMethodEnum.EMAIL_MESSAGE;
    }

    @Override
    public <T extends MessageParticipant> Boolean messageStrategy(MessageParticipant message)  {
        return this.messageSend(message);
    }
}
