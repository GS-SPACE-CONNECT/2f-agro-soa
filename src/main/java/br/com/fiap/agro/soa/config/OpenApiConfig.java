package br.com.fiap.agro.soa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadados da documentação OpenAPI/Swagger (bônus issue #10).
 *
 * <p>UI interativa em {@code /swagger-ui.html} e contrato JSON em {@code /v3/api-docs}.
 * O Web Service SOAP continua documentado pelo WSDL em {@code /ws/cadastro-rural.wsdl}.</p>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI agroSoaOpenApi() {
        return new OpenAPI().info(new Info()
                .title("2F-AGRO SOA — API REST")
                .version("0.0.1")
                .description("Serviços distribuídos (REST + SOAP + Integração NASA/CPTEC) do "
                        + "Space Connect. Documenta os endpoints REST de Propriedade e de Integração.")
                .contact(new Contact().name("GS-SPACE-CONNECT / 2F-AGRO")
                        .url("https://github.com/GS-SPACE-CONNECT/2f-agro-soa"))
                .license(new License().name("FIAP 3ESPZ · GS 2026.1")));
    }
}
