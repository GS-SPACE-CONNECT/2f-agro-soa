package br.com.fiap.agro.soa.rest;

import br.com.fiap.agro.soa.dto.ErroResposta;
import br.com.fiap.agro.soa.service.RecursoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tratamento de erro <b>centralizado</b> da API (issue #4).
 *
 * <p>Converte exceções em respostas JSON padronizadas ({@link ErroResposta}) com o status
 * HTTP correto, evitando vazar stack traces e mantendo o contrato de erro consistente.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Recurso inexistente -> 404 Not Found. */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarNaoEncontrado(RecursoNaoEncontradoException ex,
                                                            HttpServletRequest req) {
        ErroResposta corpo = ErroResposta.de(
                HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo);
    }

    /** Falha de Bean Validation (@Valid) -> 400 com detalhamento campo a campo. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> tratarValidacao(MethodArgumentNotValidException ex,
                                                        HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                erro -> campos.put(erro.getField(), erro.getDefaultMessage()));
        ErroResposta corpo = ErroResposta.deValidacao(
                HttpStatus.BAD_REQUEST.value(), "Bad Request",
                "Falha de validação nos campos enviados", req.getRequestURI(), campos);
        return ResponseEntity.badRequest().body(corpo);
    }

    /** Validação de domínio (ex.: invariantes da entidade) -> 400 Bad Request. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResposta> tratarArgumentoInvalido(IllegalArgumentException ex,
                                                               HttpServletRequest req) {
        ErroResposta corpo = ErroResposta.de(
                HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage(), req.getRequestURI());
        return ResponseEntity.badRequest().body(corpo);
    }
}
