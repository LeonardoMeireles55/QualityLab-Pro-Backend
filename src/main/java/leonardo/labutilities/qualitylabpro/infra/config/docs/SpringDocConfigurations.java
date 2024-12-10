package leonardo.labutilities.qualitylabpro.infra.config.docs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("QualityLabPro API")
                        .description
                                ("REST API for CRUD operations on analytical test" +
                                        " specifications and internal control" +
                                        " data, generating validations according to Westgard rules.")
                        .contact(new Contact()
                                .name("Time Backend")
                                .email("leomeireles55@hotmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http:/labutilities/api/license")));
    }
}
