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
