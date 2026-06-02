package br.com.fiap.agro.soa.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO de <b>entrada</b> (request) do CRUD de Propriedade.
 *
 * <p>Não expomos a entidade de domínio diretamente na fronteira HTTP: o cliente envia este
 * DTO, validado por Bean Validation ({@code @NotBlank}, {@code @NotNull}, {@code @Size}…).
 * As violações são capturadas pelo {@code @RestControllerAdvice} e viram HTTP 400.</p>
 */
public record PropriedadeRequest(

        @NotBlank(message = "produtor é obrigatório")
        String produtor,

        @NotBlank(message = "cultura é obrigatória")
        String cultura,

        @NotNull(message = "latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "latitude deve ser >= -90")
        @DecimalMax(value = "90.0", message = "latitude deve ser <= 90")
        Double latitude,

        @NotNull(message = "longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "longitude deve ser >= -180")
        @DecimalMax(value = "180.0", message = "longitude deve ser <= 180")
        Double longitude,

        @NotNull(message = "areaHa é obrigatória")
        @Positive(message = "areaHa deve ser positiva")
        Double areaHa,

        @NotBlank(message = "municipio é obrigatório")
        String municipio,

        @NotBlank(message = "uf é obrigatória")
        @Size(min = 2, max = 2, message = "uf deve ter exatamente 2 letras")
        String uf
) {
}
