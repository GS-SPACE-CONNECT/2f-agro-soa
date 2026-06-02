package br.com.fiap.agro.soa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Cadastro rural persistido pelo Web Service SOAP (issue #6).
 *
 * <p>Representa o registro do "governo legado" (EMATER/MAPA/CAR): identificado por CPF do
 * produtor e CAR (Cadastro Ambiental Rural). É gravado no mesmo banco H2 do CRUD REST.</p>
 */
@Entity
@Table(name = "cadastro_rural")
public class CadastroRural {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String car;

    @Column(name = "nome_produtor", nullable = false)
    private String nomeProdutor;

    @Column(nullable = false)
    private String municipio;

    @Column(nullable = false, length = 2)
    private String uf;

    @Column(name = "area_ha", nullable = false)
    private double areaHa;

    @Column(nullable = false)
    private String situacao;

    protected CadastroRural() {
    }

    public CadastroRural(String cpf, String car, String nomeProdutor, String municipio,
                         String uf, double areaHa, String situacao) {
        this.cpf = cpf;
        this.car = car;
        this.nomeProdutor = nomeProdutor;
        this.municipio = municipio;
        this.uf = uf;
        this.areaHa = areaHa;
        this.situacao = situacao;
    }

    public Long getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCar() {
        return car;
    }

    public String getNomeProdutor() {
        return nomeProdutor;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getUf() {
        return uf;
    }

    public double getAreaHa() {
        return areaHa;
    }

    public String getSituacao() {
        return situacao;
    }
}
