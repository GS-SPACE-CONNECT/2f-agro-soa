package br.com.fiap.agro.soa.service;

/**
 * Sinaliza que um {@link ServicoClimatico} externo (ex.: NASA POWER) não pôde ser consultado
 * — timeout, indisponibilidade ou resposta inválida. Quem orquestra a integração captura
 * esta exceção e aplica o fallback resiliente (issue #7).
 */
public class ServicoClimaticoIndisponivelException extends RuntimeException {

    public ServicoClimaticoIndisponivelException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
