package br.com.fiap.agro.soa.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Payload de erro <b>padronizado</b> devolvido pelo tratamento centralizado de exceções.
 *
 * <p>Mantém o mesmo formato para todos os erros da API, facilitando o consumo pelo cliente.
 * O campo {@code campos} só é preenchido em erros de validação (mapeia campo -> mensagem).</p>
 */
public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos
) {

    public static ErroResposta de(int status, String erro, String mensagem, String caminho) {
        return new ErroResposta(LocalDateTime.now(), status, erro, mensagem, caminho, null);
    }

    public static ErroResposta deValidacao(int status, String erro, String mensagem,
                                           String caminho, Map<String, String> campos) {
        return new ErroResposta(LocalDateTime.now(), status, erro, mensagem, caminho, campos);
    }
}
