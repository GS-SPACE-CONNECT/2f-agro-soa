package br.com.fiap.agro.soa.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Configuração do Web Service SOAP (Spring-WS), issue #6.
 *
 * <p>Registra o {@link MessageDispatcherServlet} em {@code /ws/*} e, a partir do XSD
 * (contract-first), publica o WSDL gerado dinamicamente em
 * {@code /ws/cadastro-rural.wsdl} (o nome do arquivo vem do id do bean).</p>
 */
@EnableWs
@Configuration
public class WebServiceConfig {

    /** Namespace alvo, igual ao targetNamespace do XSD. */
    public static final String NAMESPACE_URI = "http://fiap.com.br/agro/soa/cadastro-rural";

    /** Servlet que despacha as mensagens SOAP, mapeado em /ws/*. */
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    /**
     * Definição do WSDL 1.1 gerada a partir do XSD.
     * O id do bean ("cadastro-rural") define a URL: /ws/cadastro-rural.wsdl
     */
    @Bean(name = "cadastro-rural")
    public DefaultWsdl11Definition cadastroRuralWsdl(XsdSchema cadastroRuralSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("CadastroRuralPort");
        definition.setLocationUri("/ws");
        definition.setTargetNamespace(NAMESPACE_URI);
        definition.setSchema(cadastroRuralSchema);
        return definition;
    }

    /** Carrega o XSD do classpath para alimentar a geração do WSDL. */
    @Bean
    public XsdSchema cadastroRuralSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/cadastro-rural.xsd"));
    }
}
