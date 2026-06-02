package br.com.fiap.agro.soa.service;

import br.com.fiap.agro.soa.domain.DadosClimaticos;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Implementação de {@link ServicoClimatico} que consome a API pública <b>NASA POWER</b>
 * (dados de clima/solar por latitude/longitude) — serviço externo "espacial" da issue #7.
 *
 * <p>Usa o endpoint de <i>climatology</i>, que devolve médias anuais ("ANN") por parâmetro:
 * T2M (temperatura a 2m), PRECTOTCORR (precipitação) e RH2M (umidade relativa).</p>
 *
 * <p><b>Resiliência:</b> o {@link RestClient} tem timeouts de conexão/leitura; qualquer falha
 * (timeout, rede, JSON inesperado) é encapsulada em
 * {@link ServicoClimaticoIndisponivelException} para o orquestrador aplicar o fallback.</p>
 */
@Service
public class NasaPowerServicoClimatico implements ServicoClimatico {

    private static final Logger log = LoggerFactory.getLogger(NasaPowerServicoClimatico.class);
    private static final String FONTE = "NASA POWER";

    private final RestClient restClient;

    public NasaPowerServicoClimatico(
            @Value("${soa.nasa.power-base-url:https://power.larc.nasa.gov}") String baseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(8000);
        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public DadosClimaticos consultar(double latitude, double longitude) {
        try {
            JsonNode raiz = restClient.get()
                    .uri(uri -> uri
                            .path("/api/temporal/climatology/point")
                            .queryParam("parameters", "T2M,PRECTOTCORR,RH2M")
                            .queryParam("community", "AG")
                            .queryParam("latitude", latitude)
                            .queryParam("longitude", longitude)
                            .queryParam("format", "JSON")
                            .build())
                    .retrieve()
                    .body(JsonNode.class);

            JsonNode parametros = raiz.path("properties").path("parameter");
            if (parametros.isMissingNode()) {
                throw new IllegalStateException("resposta da NASA POWER sem 'properties.parameter'");
            }

            double temperatura = parametros.path("T2M").path("ANN").asDouble();
            double precipitacao = parametros.path("PRECTOTCORR").path("ANN").asDouble();
            double umidade = parametros.path("RH2M").path("ANN").asDouble();

            return new DadosClimaticos(temperatura, precipitacao, umidade, FONTE);

        } catch (Exception e) {
            log.warn("Falha ao consultar NASA POWER ({}, {}): {}", latitude, longitude, e.toString());
            throw new ServicoClimaticoIndisponivelException(
                    "NASA POWER indisponível para (" + latitude + ", " + longitude + ")", e);
        }
    }

    @Override
    public String getFonte() {
        return FONTE;
    }
}
