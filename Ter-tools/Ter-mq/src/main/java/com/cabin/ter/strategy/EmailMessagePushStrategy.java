package com.cabin.ter.strategy;

import com.cabin.ter.config.MailSenderConfig;
import com.cabin.ter.constants.dto.EmailMessageDTO;
import com.cabin.ter.constants.enums.MessagePushMethodEnum;
import com.cabin.ter.template.MessageTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Arrays;
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
public class EmailMessagePushStrategy extends MessageTemplate<EmailMessageDTO>
        implements MessageStrategyBase<EmailMessageDTO> {
    @Autowired
    private MailSenderConfig mailSenderConfig;
    @Autowired
    private TemplateEngine templateEngine;
    @Override
    public  Boolean messageStrategy(EmailMessageDTO message) {
        return this.messageSend(message);
    }
    @Override
    public MessagePushMethodEnum getSource() {
        return MessagePushMethodEnum.EMAIL_MESSAGE;
    }
    @Override
    protected Boolean messageSend(EmailMessageDTO message) {
        JavaMailSenderImpl sender = mailSenderConfig.getSender();
        MimeMessage mimeMessage = sender.createMimeMessage();
        try {
            Context context  = new Context();
            String content = null;
            switch (message.getEmailType()){
                case SYSTEM_VERIFICATION_CODE -> {
                    context.setVariable("code", Arrays.asList(message.getContent().split("")));
                    content = templateEngine.process("EmailVerificationCodePage", context);
                }
                case SYSTEM_WEL_COME -> {
                    context.setVariable("name", message.getContent());
                    content = templateEngine.process("WelComePage", context);
                }
                default ->
                    throw new RuntimeException("该邮件类型不支持");
            }
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setText(content,true);
            helper.setTo(message.getToAddress());
            helper.setSubject(message.getSubject());
            helper.setFrom(Objects.requireNonNull(sender.getUsername()));
        }catch (MessagingException exception){
            throw new RuntimeException("邮件发送失败"+exception);
        }
        sender.send(mimeMessage);
        return Boolean.TRUE;
    }
}
