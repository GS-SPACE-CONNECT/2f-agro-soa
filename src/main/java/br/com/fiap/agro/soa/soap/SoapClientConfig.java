package br.com.fiap.agro.soa.soap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Configuração do <b>cliente SOAP</b> usado na integração REST↔SOAP interna (issue #7).
 *
 * <p>Reaproveita as classes JAXB geradas do XSD (mesmo contrato do servidor) e aponta o
 * {@link WebServiceTemplate} para o próprio endpoint SOAP da aplicação — demonstrando a
 * comunicação entre serviços pela rede (HTTP/SOAP).</p>
 */
@Configuration
public class SoapClientConfig {

    /** URI do Web Service SOAP "do governo" (o próprio app, por padrão). */
    @Value("${soa.governo.soap-uri:http://localhost:8080/ws}")
    private String governoSoapUri;

    @Bean
    public Jaxb2Marshaller cadastroRuralMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("br.com.fiap.agro.soa.soap.gen");
        return marshaller;
    }

    @Bean
    public WebServiceTemplate cadastroRuralWebServiceTemplate(Jaxb2Marshaller cadastroRuralMarshaller) {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(cadastroRuralMarshaller);
        template.setUnmarshaller(cadastroRuralMarshaller);
        template.setDefaultUri(governoSoapUri);
        return template;
    }
}
