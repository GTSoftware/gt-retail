package ar.com.gtsoftware.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES
            = new HashSet<>(Collections.singleton(MediaType.APPLICATION_JSON_VALUE));

    private static List<Parameter> commonParameters() {
        List<Parameter> parameters = new ArrayList<>(1);
        parameters.add(new ParameterBuilder()
                .name("Authorization")
                .description("JWT for authorization")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .scalarExample("Bearer auth_token")
                .required(false)
                .build());

        return parameters;
    }

    private static ApiInfo getApiInfo() {
        return new ApiInfoBuilder().description("GTRetail REST API documentation")
                .license("GNU GENERAL PUBLIC LICENSE Version 3")
                .contact(new Contact("Rodrigo Tato",
                        "gtsoftware.com.ar",
                        "rotatomel@gmail.com"))
                .title("GTRetail REST API")
                .build();

    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .globalOperationParameters(commonParameters())
                .apiInfo(getApiInfo());
    }
}
