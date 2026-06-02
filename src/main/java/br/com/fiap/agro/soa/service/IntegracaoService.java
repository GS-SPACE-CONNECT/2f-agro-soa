package br.com.fiap.agro.soa.service;

import br.com.fiap.agro.soa.domain.Alerta;
import br.com.fiap.agro.soa.domain.AlertaGeada;
import br.com.fiap.agro.soa.domain.AlertaSeca;
import br.com.fiap.agro.soa.domain.DadosClimaticos;
import br.com.fiap.agro.soa.domain.NivelSeveridade;
import br.com.fiap.agro.soa.dto.CadastroIntegradoResponse;
import br.com.fiap.agro.soa.dto.PropriedadeEnriquecidaResponse;
import br.com.fiap.agro.soa.dto.PropriedadeRequest;
import br.com.fiap.agro.soa.dto.PropriedadeResponse;
import br.com.fiap.agro.soa.soap.CadastroRuralClient;
import br.com.fiap.agro.soa.soap.gen.RegistrarCadastroRuralRequest;
import br.com.fiap.agro.soa.soap.gen.RegistrarCadastroRuralResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Orquestra a <b>integração entre serviços</b> (issue #7):
 * <ol>
 *   <li>cria a propriedade no lado REST/H2;</li>
 *   <li>registra no "governo" via Web Service <b>SOAP</b> (REST↔SOAP interno);</li>
 *   <li>enriquece com dado climático externo da <b>NASA POWER</b>;</li>
 *   <li>deriva um alerta agroclimático (polimorfismo do domínio da issue #3).</li>
 * </ol>
 *
 * <p>Cada serviço externo é chamado de forma <b>resiliente</b>: falhas não derrubam o fluxo,
 * viram avisos no resultado (fallback).</p>
 */
@Service
public class IntegracaoService {

    private static final Logger log = LoggerFactory.getLogger(IntegracaoService.class);

    private final PropriedadeService propriedadeService;
    private final ServicoClimatico servicoClimatico;
    private final CadastroRuralClient cadastroRuralClient;

    public IntegracaoService(PropriedadeService propriedadeService,
                             ServicoClimatico servicoClimatico,
                             CadastroRuralClient cadastroRuralClient) {
        this.propriedadeService = propriedadeService;
        this.servicoClimatico = servicoClimatico;
        this.cadastroRuralClient = cadastroRuralClient;
    }

    /**
     * Retorna a propriedade enriquecida com o clima da NASA (com fallback se indisponível).
     */
    public PropriedadeEnriquecidaResponse enriquecer(Long id) {
        PropriedadeResponse prop = propriedadeService.buscarPorId(id);
        try {
            DadosClimaticos clima = servicoClimatico.consultar(prop.latitude(), prop.longitude());
            return new PropriedadeEnriquecidaResponse(prop, true, clima, avaliarAlerta(clima), null);
        } catch (ServicoClimaticoIndisponivelException e) {
            return new PropriedadeEnriquecidaResponse(prop, false, null, null,
                    "Dado climático indisponível (" + servicoClimatico.getFonte() + "): " + e.getMessage());
        }
    }

    /**
     * Fluxo de cadastro integrado: REST -> SOAP (governo) -> NASA (clima) -> alerta.
     */
    public CadastroIntegradoResponse cadastrarComIntegracao(PropriedadeRequest req) {
        List<String> avisos = new ArrayList<>();

        // 1) Cria no lado REST/H2.
        PropriedadeResponse prop = propriedadeService.criar(req);

        // 2) Registra no "governo" via SOAP (REST -> SOAP interno).
        String protocolo = null;
        String situacao = null;
        try {
            RegistrarCadastroRuralResponse soap = cadastroRuralClient.registrar(montarRequisicaoSoap(prop));
            protocolo = soap.getProtocolo();
            situacao = soap.getSituacao();
        } catch (Exception e) {
            log.warn("Falha no registro SOAP do governo: {}", e.toString());
            avisos.add("Registro no governo (SOAP) indisponível: " + e.getMessage());
        }

        // 3) Enriquece com clima externo (NASA POWER).
        DadosClimaticos clima = null;
        String alerta = null;
        try {
            clima = servicoClimatico.consultar(prop.latitude(), prop.longitude());
            alerta = avaliarAlerta(clima);
        } catch (ServicoClimaticoIndisponivelException e) {
            avisos.add("Dado climático indisponível (" + servicoClimatico.getFonte() + "): " + e.getMessage());
        }

        return new CadastroIntegradoResponse(
                prop, protocolo, situacao, clima != null, clima, alerta, avisos);
    }

    /** Monta a requisição SOAP a partir da propriedade (CPF/CAR simulados para o demo). */
    private RegistrarCadastroRuralRequest montarRequisicaoSoap(PropriedadeResponse prop) {
        RegistrarCadastroRuralRequest req = new RegistrarCadastroRuralRequest();
        req.setCpf("SIMULADO-" + prop.id());
        req.setCar("CAR-" + prop.uf() + "-" + prop.id());
        req.setNomeProdutor(prop.produtor());
        req.setMunicipio(prop.municipio());
        req.setUf(prop.uf());
        req.setAreaHa(prop.areaHa());
        return req;
    }

    /**
     * Deriva um alerta agroclimático a partir do clima.
     *
     * <p>Polimorfismo em ação: trabalhamos com a referência abstrata {@link Alerta} e chamamos
     * {@code gerarMensagem()} sem saber o tipo concreto.</p>
     *
     * @return mensagem do alerta, ou {@code null} se as condições estiverem normais.
     */
    private String avaliarAlerta(DadosClimaticos clima) {
        Alerta alerta = null;
        if (clima.temperaturaMediaC() <= 5.0) {
            alerta = new AlertaGeada(NivelSeveridade.ALTO, clima.temperaturaMediaC());
        } else if (clima.precipitacaoMm() < 1.0) {
            // PRECTOTCORR vem em mm/dia; valor muito baixo sugere período seco.
            alerta = new AlertaSeca(NivelSeveridade.MEDIO, 30);
        }
        return alerta == null ? null : alerta.gerarMensagem();
    }
}
