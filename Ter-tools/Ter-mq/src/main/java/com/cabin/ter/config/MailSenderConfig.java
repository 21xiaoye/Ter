package com.cabin.ter.config;

import com.cabin.ter.constants.enums.Status;
import com.cabin.ter.exception.BaseException;
import com.cabin.ter.sys.domain.MailProperties;
import com.cabin.ter.sys.mapper.MailPropertiesMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * <p>
 *     邮箱源配置
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-05-01 15:53
 */
@Slf4j
@Component
@AllArgsConstructor
public class MailSenderConfig {
    @Autowired
    private MailPropertiesMapper mailPropertiesMapper;

    private final List<JavaMailSenderImpl> javaMailSenderList = new ArrayList<>();

    /**
     * 初始化 sender
     */
    @PostConstruct
    public void buildMailSender(){
        List<MailProperties> mailList = mailPropertiesMapper.findMailList();
        log.info(mailList.toString());
        mailList.forEach(item -> {
            try {
                JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
                javaMailSender.setDefaultEncoding(item.getEncoding());
                javaMailSender.setHost(item.getMailHost());
                javaMailSender.setPort(item.getPort());
                javaMailSender.setProtocol(item.getProtocol());
                javaMailSender.setUsername(item.getMailName());
                javaMailSender.setPassword(item.getMailPasswd());
                Properties props = javaMailSender.getJavaMailProperties();
                props.put("mail.smtp.auth", "true");  // 启用SMTP认证
                props.put("mail.smtp.ssl.enable", "true");  // 启用SSL
                props.put("mail.smtp.starttls.enable", "true");  // 启用TLS，如果使用TLS端口
                props.put("mail.smtp.timeout", "5000");  // 可选：设置超时时间
                props.put("mail.smtp.socketFactory.port", "465");  // 设置socket端口
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  // 使用SSL工厂
                javaMailSenderList.add(javaMailSender);
            } catch (Exception e) {
                log.error("Error creating JavaMailSenderImpl", e);
                throw new BaseException(Status.ERROR,e);
            }
        });
    }
    /**
     * 获取MailSender
     *
     * @return JavaMailSenderImpl
     */
    public JavaMailSenderImpl getSender(){
        if(javaMailSenderList.isEmpty()){
            buildMailSender();
        }
        // 随机返回一个JavaMailSender
        return javaMailSenderList.get(new Random().nextInt(javaMailSenderList.size()));
    }

    /**
     * 清除sends
     */
    public void clear(){
        javaMailSenderList.clear();
    }
}
