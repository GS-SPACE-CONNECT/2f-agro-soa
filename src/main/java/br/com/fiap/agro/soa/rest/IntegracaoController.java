package br.com.fiap.agro.soa.rest;

import br.com.fiap.agro.soa.dto.CadastroIntegradoResponse;
import br.com.fiap.agro.soa.dto.PropriedadeEnriquecidaResponse;
import br.com.fiap.agro.soa.dto.PropriedadeRequest;
import br.com.fiap.agro.soa.service.IntegracaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST de <b>integração entre serviços</b> (issue #7).
 *
 * <p>Expõe o consumo do serviço externo (NASA POWER) e o fluxo orquestrado REST↔SOAP.</p>
 */
@RestController
@RequestMapping("/api/integracao")
public class IntegracaoController {

    private final IntegracaoService integracaoService;

    public IntegracaoController(IntegracaoService integracaoService) {
        this.integracaoService = integracaoService;
    }

    /**
     * GET /api/integracao/propriedades/{id}/clima
     * Devolve a propriedade enriquecida com o dado climático da NASA POWER (com fallback).
     */
    @GetMapping("/propriedades/{id}/clima")
    public PropriedadeEnriquecidaResponse enriquecer(@PathVariable Long id) {
        return integracaoService.enriquecer(id);
    }

    /**
     * POST /api/integracao/propriedades
     * Cadastra a propriedade (REST), registra no governo (SOAP) e enriquece com clima (NASA).
     */
    @PostMapping("/propriedades")
    public ResponseEntity<CadastroIntegradoResponse> cadastrarIntegrado(
            @Valid @RequestBody PropriedadeRequest req) {
        CadastroIntegradoResponse resultado = integracaoService.cadastrarComIntegracao(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
}
