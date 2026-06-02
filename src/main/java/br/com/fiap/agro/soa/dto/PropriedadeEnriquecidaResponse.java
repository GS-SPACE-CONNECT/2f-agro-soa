package br.com.fiap.agro.soa.dto;

import br.com.fiap.agro.soa.domain.DadosClimaticos;

/**
 * Propriedade <b>enriquecida</b> com o dado climático externo (NASA POWER) — issue #7.
 *
 * <p>Quando o serviço externo falha, {@code climaDisponivel=false}, {@code clima=null} e
 * {@code aviso} explica o fallback — a resposta continua útil (resiliência).</p>
 */
public record PropriedadeEnriquecidaResponse(
        PropriedadeResponse propriedade,
        boolean climaDisponivel,
        DadosClimaticos clima,
        String alerta,
        String aviso
) {
}
