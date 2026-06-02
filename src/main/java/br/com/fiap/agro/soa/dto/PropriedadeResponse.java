package br.com.fiap.agro.soa.dto;

import br.com.fiap.agro.soa.domain.Propriedade;

/**
 * DTO de <b>saída</b> (response) do CRUD de Propriedade.
 *
 * <p>Convertemos a entidade para este formato antes de responder, isolando o modelo de
 * domínio/persistência do contrato público da API.</p>
 */
public record PropriedadeResponse(
        Long id,
        String produtor,
        String cultura,
        double latitude,
        double longitude,
        double areaHa,
        String municipio,
        String uf
) {

    /** Fábrica de conversão entidade -> DTO de resposta. */
    public static PropriedadeResponse de(Propriedade p) {
        return new PropriedadeResponse(
                p.getId(),
                p.getProdutor(),
                p.getCultura(),
                p.getLatitude(),
                p.getLongitude(),
                p.getAreaHa(),
                p.getMunicipio(),
                p.getUf()
        );
    }
}
