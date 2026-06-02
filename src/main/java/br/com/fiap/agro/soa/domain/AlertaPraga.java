package br.com.fiap.agro.soa.domain;

/**
 * Alerta de <b>praga</b>: dispara quando há risco/ocorrência de uma praga na cultura.
 *
 * <p><b>Herança</b> de {@link Alerta} + <b>polimorfismo</b> em {@link #gerarMensagem()}.</p>
 */
public class AlertaPraga extends Alerta {

    /** Encapsulamento: nome da praga identificada, privado e validado. */
    private final String nomePraga;

    public AlertaPraga(NivelSeveridade nivel, String nomePraga) {
        super(nivel);
        if (nomePraga == null || nomePraga.isBlank()) {
            throw new IllegalArgumentException("nomePraga é obrigatório");
        }
        this.nomePraga = nomePraga.trim();
    }

    @Override
    public String gerarMensagem() {
        return "ALERTA DE PRAGA (%s): risco de '%s'. Recomenda-se monitoramento/manejo."
                .formatted(getNivel(), nomePraga);
    }

    @Override
    public String getTipo() {
        return "PRAGA";
    }

    public String getNomePraga() {
        return nomePraga;
    }
}
