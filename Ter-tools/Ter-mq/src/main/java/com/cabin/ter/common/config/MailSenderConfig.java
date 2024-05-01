package com.cabin.ter.common.config;

import com.cabin.ter.sys.domain.MailProperties;
import com.cabin.ter.sys.mapper.MailPropertiesMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

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
    private final List<JavaMailSenderImpl> javaMailSenderList;

    /**
     * 初始化 sender
     */
    @PostConstruct
    public void buildMailSender(){
        List<MailProperties> mailList = mailPropertiesMapper.findMailList();

        mailList.forEach(item->{
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setDefaultEncoding(item.getEncoding());
            javaMailSender.setHost(item.getMailHost());
            javaMailSender.setPort(item.getPort());
            javaMailSender.setProtocol(item.getProtocol());
            javaMailSender.setUsername(item.getMailName());
            javaMailSender.setPassword(item.getMailPasswd());

            javaMailSenderList.add(javaMailSender);
        });
    }
    /**
     * 获取MailSender
     *
     * @return CustomMailSender
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
