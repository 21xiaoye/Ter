package mq;

import com.cabin.ter.TerApplication;
import com.cabin.ter.common.constants.entity.msg.EmailParticipant;
import com.cabin.ter.common.constants.enums.MessageEnum;
import com.cabin.ter.common.service.MessageStrategyServiceFactory;
import com.cabin.ter.common.service.impl.EmailStrategyServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@SpringBootTest(classes = TerApplication.class)
@RunWith(SpringRunner.class)
public class MailSendTest {
    @Autowired
    private EmailStrategyServiceImpl emailStrategyService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private ApplicationContext context;
    @Test
    public void sendMail() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(context);
        templateResolver.setCacheable(false);
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");

        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("project", "Spring Boot Demo");
        context.setVariable("author", "Yangkai.Shen");
        context.setVariable("url", "https://github.com/xkcoding/spring-boot-demo");

        String emailTemplate = templateEngine.process("test", context);

        EmailParticipant message = EmailParticipant.builder()
                .to("zouye0113@gmail.com")
                .subject("测试")
                .content(emailTemplate)
                .build();
        MessageStrategyServiceFactory.getInstance().getAwardResult(message, MessageEnum.EMAIL_MESSAGE);
    }

}
