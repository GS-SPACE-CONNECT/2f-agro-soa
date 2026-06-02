package br.com.fiap.agro.soa.domain;

/**
 * Alerta de <b>seca</b>: dispara quando a propriedade passa muitos dias sem chuva.
 *
 * <p><b>Herança:</b> reaproveita nível e data de emissão de {@link Alerta}.
 * <b>Polimorfismo:</b> sobrescreve {@link #gerarMensagem()} com a lógica específica de seca.</p>
 */
public class AlertaSeca extends Alerta {

    /** Encapsulamento: dado específico de seca, privado e validado. */
    private final int diasSemChuva;

    public AlertaSeca(NivelSeveridade nivel, int diasSemChuva) {
        super(nivel);
        if (diasSemChuva < 0) {
            throw new IllegalArgumentException("diasSemChuva não pode ser negativo");
        }
        this.diasSemChuva = diasSemChuva;
    }

    @Override
    public String gerarMensagem() {
        return "ALERTA DE SECA (%s): %d dias sem chuva registrados. Avalie irrigação."
                .formatted(getNivel(), diasSemChuva);
    }

    @Override
    public String getTipo() {
        return "SECA";
    }

    public int getDiasSemChuva() {
        return diasSemChuva;
    }
}
