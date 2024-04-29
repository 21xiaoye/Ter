package com.cabin.ter.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info())
                .externalDocs(new ExternalDocumentation()
                        .description("Ter Github")
                        .url("https://github.com/21xiaoye/Ter.git"));
    }

    private Info info(){
        Contact contact = new Contact();
        contact.setEmail("zouye0113@gmail.com");
        contact.setName("邺子意");
        contact.setUrl("https://github.com/21xiaoye");

        return new Info()
                .title("Ter-接口文档")
                .description("Ter 项目文档")
                .version("v1")
                .contact(contact);
    }
}
