package br.com.fiap.agro.soa.soap;

import br.com.fiap.agro.soa.soap.gen.ConsultarCadastroRuralRequest;
import br.com.fiap.agro.soa.soap.gen.ConsultarCadastroRuralResponse;
import br.com.fiap.agro.soa.soap.gen.RegistrarCadastroRuralRequest;
import br.com.fiap.agro.soa.soap.gen.RegistrarCadastroRuralResponse;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * Cliente SOAP (JAXB + {@link WebServiceTemplate}) do Cadastro Rural — issue #7.
 *
 * <p>Encapsula as chamadas ao Web Service "do governo": o lado REST usa este componente para
 * registrar/consultar no sistema legado via SOAP, sem lidar com XML manualmente.</p>
 */
@Component
public class CadastroRuralClient {

    private final WebServiceTemplate template;

    public CadastroRuralClient(WebServiceTemplate cadastroRuralWebServiceTemplate) {
        this.template = cadastroRuralWebServiceTemplate;
    }

    public RegistrarCadastroRuralResponse registrar(RegistrarCadastroRuralRequest req) {
        return (RegistrarCadastroRuralResponse) template.marshalSendAndReceive(req);
    }

    public ConsultarCadastroRuralResponse consultar(ConsultarCadastroRuralRequest req) {
        return (ConsultarCadastroRuralResponse) template.marshalSendAndReceive(req);
    }
}
