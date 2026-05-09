package cl.tenpo.fgeissbuhler.test.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tenpoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tenpo API - Prueba Técnica")
                        .version("v1.0.0")
                        .description("API REST para cálculo de porcentajes adicionales y consulta de historial de solicitudes.")
                        .contact(new Contact()
                                .name("Fabio Geissbuhler Alarcón")
                                .email("gssbhler.code@gmail.com")
                                .url("https://www.linkedin.com/in/fabiogeissbuhler"))
                        .license(new License()
                                .name("MIT License")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:5001")
                                .description("Servidor local de desarrollo")));
    }
}
