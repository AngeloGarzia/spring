package fr.diginamic.recencement.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Recensement TP10")
                        .version("1.0.0")
                        .description("API REST Spring Boot - Gestion villes/départements")
                        .termsOfService("https://github.com/AngeloGarzia/spring")
                        .contact(new Contact()
                                .name("Angelo")
                                .email("ton@email.com")
                                .url("https://github.com/AngeloGarzia/spring"))
                        .license(new License()
                                .name("Microsoft")
                                .url("https://github.com/AngeloGarzia/spring")));
    }
}
