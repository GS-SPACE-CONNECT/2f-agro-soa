package br.com.fiap.agro.soa.domain;

/**
 * Nível de severidade de um {@link Alerta}.
 *
 * <p><b>Pilar POO — Encapsulamento/Abstração:</b> representa um conjunto fechado de
 * valores válidos como um tipo próprio, em vez de usar "strings mágicas" espalhadas
 * pelo código.</p>
 */
public enum NivelSeveridade {
    BAIXO,
    MEDIO,
    ALTO,
    CRITICO
}
