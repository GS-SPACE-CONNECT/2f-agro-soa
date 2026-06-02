package br.com.fiap.agro.soa.dto;

import br.com.fiap.agro.soa.domain.DadosClimaticos;

import java.util.List;

/**
 * Resultado do fluxo de integração completo (issue #7): cadastra a propriedade no REST,
 * registra no "governo" via SOAP e enriquece com o dado climático da NASA.
 *
 * @param propriedade      propriedade criada (lado REST/H2)
 * @param protocoloGoverno protocolo devolvido pelo Web Service SOAP
 * @param situacaoGoverno  situação retornada pelo SOAP (ex.: REGISTRADO)
 * @param climaDisponivel  se o dado climático externo pôde ser obtido
 * @param clima            dado climático (NASA POWER) ou {@code null} em fallback
 * @param alerta           mensagem de alerta agroclimático derivada do clima, se houver
 * @param avisos           mensagens de fallback/observações da integração
 */
public record CadastroIntegradoResponse(
        PropriedadeResponse propriedade,
        String protocoloGoverno,
        String situacaoGoverno,
        boolean climaDisponivel,
        DadosClimaticos clima,
        String alerta,
        List<String> avisos
) {
}
