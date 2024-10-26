package com.cabin.ter.strategy;

import com.cabin.ter.config.MailSenderConfig;
import com.cabin.ter.constants.dto.EmailMessageDTO;
import com.cabin.ter.constants.dto.MQBaseMessage;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
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
public class EmailMessagePushStrategy extends MessageTemplate
        implements MessageStrategyBase {
    @Autowired
    private MailSenderConfig mailSenderConfig;

    @Override
    public <T extends MQBaseMessage> Boolean messageStrategy(T message) {
        return this.messageSend(message);
    }
    @Override
    public MessagePushMethodEnum getSource() {
        return MessagePushMethodEnum.EMAIL_MESSAGE;
    }
    @Override
    protected <T extends MQBaseMessage> Boolean messageSend(T message) {
        JavaMailSenderImpl sender = mailSenderConfig.getSender();
        MimeMessage mimeMessage = sender.createMimeMessage();
        try {
            EmailMessageDTO emailParticipant =(EmailMessageDTO) message;
            System.out.println("消费的消息内容为"+emailParticipant);
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setText(emailParticipant.getContent(),true);
            helper.setTo(emailParticipant.getToAddress());
            helper.setSubject(emailParticipant.getSubject());

            helper.setFrom(Objects.requireNonNull(sender.getUsername()));
        }catch (MessagingException exception){
            throw new RuntimeException("邮件发送失败"+exception);
        }
        sender.send(mimeMessage);
        return Boolean.TRUE;
    }
}
