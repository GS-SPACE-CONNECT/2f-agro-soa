package br.com.fiap.agro.soa.service;

import br.com.fiap.agro.soa.domain.DadosClimaticos;

/**
 * Abstração de um provedor de dados climáticos.
 *
 * <p><b>Pilar POO — Abstração:</b> esta interface define <i>o que</i> um serviço climático
 * faz (consultar dados de um ponto), sem dizer <i>como</i>. Assim, as implementações
 * concretas — NASA POWER e CPTEC-INPE, que chegam na issue #7 — ficam <b>intercambiáveis</b>:
 * o restante do sistema depende apenas deste contrato e pode trocar de fonte sem alteração.</p>
 *
 * <p>É a aplicação do princípio "programe para uma interface, não para uma implementação".</p>
 */
public interface ServicoClimatico {

    /**
     * Consulta os dados climáticos de uma coordenada geográfica.
     *
     * @param latitude  latitude em graus decimais (-90 a 90)
     * @param longitude longitude em graus decimais (-180 a 180)
     * @return dados climáticos do ponto, com a fonte preenchida
     */
    DadosClimaticos consultar(double latitude, double longitude);

    /**
     * Nome legível da fonte (ex.: "NASA POWER"), usado para rastreabilidade e logs.
     */
    String getFonte();
}
