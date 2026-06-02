package br.com.fiap.agro.soa.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Alerta agroclimático emitido para uma propriedade rural.
 *
 * <p><b>Pilar POO — Abstração:</b> esta classe é {@code abstract} porque "Alerta" genérico
 * não existe no mundo real; o que existe é um alerta <i>de seca</i>, <i>de geada</i> ou
 * <i>de praga</i>. Ela define o contrato comum (o que todo alerta tem e sabe fazer) sem
 * decidir como cada tipo monta sua mensagem.</p>
 *
 * <p><b>Pilar POO — Herança:</b> {@link AlertaSeca}, {@link AlertaGeada} e
 * {@link AlertaPraga} herdam os atributos e o comportamento comum daqui.</p>
 *
 * <p><b>Pilar POO — Encapsulamento:</b> os campos são {@code private} e só expostos por
 * getters; o estado é validado no construtor, impedindo a criação de um alerta inválido.</p>
 *
 * <p><b>Pilar POO — Polimorfismo:</b> o método {@link #gerarMensagem()} é {@code abstract}
 * e cada subclasse o sobrescreve; quem usa um {@code Alerta} chama {@code gerarMensagem()}
 * sem saber qual tipo concreto está por trás.</p>
 */
public abstract class Alerta {

    private final NivelSeveridade nivel;
    private final LocalDateTime emitidoEm;

    /**
     * @param nivel severidade do alerta; obrigatório.
     */
    protected Alerta(NivelSeveridade nivel) {
        // Encapsulamento: validação centralizada garante invariante (nível sempre presente).
        this.nivel = Objects.requireNonNull(nivel, "nível de severidade é obrigatório");
        this.emitidoEm = LocalDateTime.now();
    }

    /**
     * Monta a mensagem legível do alerta.
     *
     * <p>Polimorfismo: cada subclasse decide o texto conforme seus próprios dados.</p>
     */
    public abstract String gerarMensagem();

    /**
     * Rótulo curto do tipo de alerta (ex.: "SECA"), útil para logs e respostas de API.
     */
    public abstract String getTipo();

    public NivelSeveridade getNivel() {
        return nivel;
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }
}
