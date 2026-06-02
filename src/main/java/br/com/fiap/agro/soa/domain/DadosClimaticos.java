package br.com.fiap.agro.soa.domain;

/**
 * Dados climáticos de um ponto geográfico, retornados por um {@link br.com.fiap.agro.soa.service.ServicoClimatico}.
 *
 * <p>Value object imutável (record): só carrega dados, sem identidade própria. A {@code fonte}
 * indica de onde vieram (ex.: "NASA POWER" ou "CPTEC-INPE"), permitindo rastrear a origem
 * independentemente da implementação usada.</p>
 *
 * @param temperaturaMediaC temperatura média em °C
 * @param precipitacaoMm    precipitação acumulada em mm
 * @param umidadeRelativa   umidade relativa do ar em %
 * @param fonte             nome do serviço que forneceu os dados
 */
public record DadosClimaticos(
        double temperaturaMediaC,
        double precipitacaoMm,
        double umidadeRelativa,
        String fonte
) {
}
