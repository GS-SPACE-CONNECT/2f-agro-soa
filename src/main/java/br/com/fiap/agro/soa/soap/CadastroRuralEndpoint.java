package br.com.fiap.agro.soa.soap;

import br.com.fiap.agro.soa.domain.CadastroRural;
import br.com.fiap.agro.soa.repository.CadastroRuralRepository;
import br.com.fiap.agro.soa.soap.gen.CadastroRuralTipo;
import br.com.fiap.agro.soa.soap.gen.ConsultarCadastroRuralRequest;
import br.com.fiap.agro.soa.soap.gen.ConsultarCadastroRuralResponse;
import br.com.fiap.agro.soa.soap.gen.RegistrarCadastroRuralRequest;
import br.com.fiap.agro.soa.soap.gen.RegistrarCadastroRuralResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Optional;

/**
 * Endpoint SOAP do Cadastro Rural (issue #6) — simula o sistema legado do governo.
 *
 * <p>O Spring-WS roteia a mensagem para o método cujo {@link PayloadRoot} casa com o
 * namespace + nome do elemento raiz do XML recebido. As operações persistem/consultam no
 * mesmo banco H2 do CRUD REST via {@link CadastroRuralRepository}.</p>
 */
@Endpoint
public class CadastroRuralEndpoint {

    private static final String NAMESPACE_URI = WebServiceConfig.NAMESPACE_URI;

    private final CadastroRuralRepository repository;

    public CadastroRuralEndpoint(CadastroRuralRepository repository) {
        this.repository = repository;
    }

    /**
     * Consulta um cadastro por CPF (prioritário) ou CAR.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "consultarCadastroRuralRequest")
    @ResponsePayload
    public ConsultarCadastroRuralResponse consultar(@RequestPayload ConsultarCadastroRuralRequest req) {
        Optional<CadastroRural> encontrado = Optional.empty();
        if (req.getCpf() != null && !req.getCpf().isBlank()) {
            encontrado = repository.findFirstByCpfOrderByIdDesc(req.getCpf().trim());
        }
        if (encontrado.isEmpty() && req.getCar() != null && !req.getCar().isBlank()) {
            encontrado = repository.findFirstByCarOrderByIdDesc(req.getCar().trim());
        }

        ConsultarCadastroRuralResponse resp = new ConsultarCadastroRuralResponse();
        resp.setEncontrado(encontrado.isPresent());
        encontrado.ifPresent(c -> resp.setCadastro(paraTipo(c)));
        return resp;
    }

    /**
     * Registra um novo cadastro rural e devolve um protocolo.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "registrarCadastroRuralRequest")
    @ResponsePayload
    public RegistrarCadastroRuralResponse registrar(@RequestPayload RegistrarCadastroRuralRequest req) {
        CadastroRural salvo = repository.save(new CadastroRural(
                req.getCpf(),
                req.getCar(),
                req.getNomeProdutor(),
                req.getMunicipio(),
                req.getUf(),
                req.getAreaHa(),
                "REGISTRADO"
        ));

        RegistrarCadastroRuralResponse resp = new RegistrarCadastroRuralResponse();
        resp.setProtocolo("CAR-" + salvo.getId());
        resp.setSituacao(salvo.getSituacao());
        resp.setMensagem("Cadastro rural registrado com sucesso para " + salvo.getNomeProdutor());
        return resp;
    }

    /** Converte a entidade JPA para o tipo do contrato SOAP (gerado do XSD). */
    private CadastroRuralTipo paraTipo(CadastroRural c) {
        CadastroRuralTipo tipo = new CadastroRuralTipo();
        tipo.setCpf(c.getCpf());
        tipo.setCar(c.getCar());
        tipo.setNomeProdutor(c.getNomeProdutor());
        tipo.setMunicipio(c.getMunicipio());
        tipo.setUf(c.getUf());
        tipo.setAreaHa(c.getAreaHa());
        tipo.setSituacao(c.getSituacao());
        return tipo;
    }
}
