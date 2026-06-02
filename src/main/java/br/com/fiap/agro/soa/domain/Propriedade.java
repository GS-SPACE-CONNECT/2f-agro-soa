package br.com.fiap.agro.soa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade central do domínio: uma <b>propriedade rural</b> monitorada pelo Space Connect.
 *
 * <p><b>Pilar POO — Encapsulamento:</b> todos os campos são {@code private} e só podem ser
 * lidos por getters. As alterações passam por setters/construtor que <i>validam</i> os
 * dados (coordenadas dentro da faixa válida, área positiva, UF com 2 letras, etc.), de modo
 * que nunca exista em memória uma {@code Propriedade} em estado inconsistente.</p>
 *
 * <p>Mapeada para persistência com JPA (issue #5): {@code @Entity} + {@code @Id} com
 * {@code @GeneratedValue}. O Hibernate carrega via acesso a campos (reflexão), então a
 * leitura do banco não dispara os setters de validação — eles protegem apenas a escrita
 * vinda da aplicação.</p>
 */
@Entity
@Table(name = "propriedade")
public class Propriedade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String produtor;

    @Column(nullable = false)
    private String cultura;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "area_ha", nullable = false)
    private double areaHa;

    @Column(nullable = false)
    private String municipio;

    @Column(nullable = false, length = 2)
    private String uf;

    /** Construtor sem argumentos exigido pelo JPA. */
    protected Propriedade() {
    }

    public Propriedade(String produtor, String cultura, double latitude, double longitude,
                       double areaHa, String municipio, String uf) {
        setProdutor(produtor);
        setCultura(cultura);
        setLatitude(latitude);
        setLongitude(longitude);
        setAreaHa(areaHa);
        setMunicipio(municipio);
        setUf(uf);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProdutor() {
        return produtor;
    }

    public void setProdutor(String produtor) {
        this.produtor = exigirTexto(produtor, "produtor");
    }

    public String getCultura() {
        return cultura;
    }

    public void setCultura(String cultura) {
        this.cultura = exigirTexto(cultura, "cultura");
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("latitude deve estar entre -90 e 90");
        }
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("longitude deve estar entre -180 e 180");
        }
        this.longitude = longitude;
    }

    public double getAreaHa() {
        return areaHa;
    }

    public void setAreaHa(double areaHa) {
        if (areaHa <= 0) {
            throw new IllegalArgumentException("areaHa deve ser positiva");
        }
        this.areaHa = areaHa;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = exigirTexto(municipio, "municipio");
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        if (uf == null || uf.trim().length() != 2) {
            throw new IllegalArgumentException("uf deve ter exatamente 2 letras");
        }
        this.uf = uf.trim().toUpperCase();
    }

    /** Helper de validação reaproveitado pelos setters de texto obrigatório. */
    private static String exigirTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório");
        }
        return valor.trim();
    }
}
