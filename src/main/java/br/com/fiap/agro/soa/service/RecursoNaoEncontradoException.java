package br.com.fiap.agro.soa.service;

/**
 * Lançada quando um recurso (ex.: Propriedade) não é encontrado.
 * É traduzida para HTTP 404 pelo tratamento centralizado de exceções.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
