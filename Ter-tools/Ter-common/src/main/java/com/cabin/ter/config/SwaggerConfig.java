package com.cabin.ter.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *     Swagger 生成API文档
 * </p>
 *
 * @author xiaoye
 * @date Created in 2024-04-29 18:23
 */

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


        License license = new License();
        license.setName("GNU General Public License v3.0");
        license.setUrl("https://github.com/21xiaoye/Ter/blob/main/LICENSE");
        return new Info()
                .title("Ter-接口文档")
                .description("Ter 项目文档")
                .version("v1")
                .contact(contact)
                .license(license);
    }
}
