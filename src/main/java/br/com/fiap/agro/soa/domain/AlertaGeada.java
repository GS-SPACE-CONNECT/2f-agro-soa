package br.com.fiap.agro.soa.domain;

/**
 * Alerta de <b>geada</b>: dispara quando a temperatura mínima prevista cai demais.
 *
 * <p><b>Herança</b> de {@link Alerta} + <b>polimorfismo</b> em {@link #gerarMensagem()}.</p>
 */
public class AlertaGeada extends Alerta {

    /** Encapsulamento: temperatura mínima (°C) prevista, específica deste alerta. */
    private final double temperaturaMinimaC;

    public AlertaGeada(NivelSeveridade nivel, double temperaturaMinimaC) {
        super(nivel);
        this.temperaturaMinimaC = temperaturaMinimaC;
    }

    @Override
    public String gerarMensagem() {
        return "ALERTA DE GEADA (%s): mínima prevista de %.1f°C. Proteja culturas sensíveis."
                .formatted(getNivel(), temperaturaMinimaC);
    }

    @Override
    public String getTipo() {
        return "GEADA";
    }

    public double getTemperaturaMinimaC() {
        return temperaturaMinimaC;
    }
}
