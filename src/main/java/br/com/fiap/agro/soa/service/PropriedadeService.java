package br.com.fiap.agro.soa.service;

import br.com.fiap.agro.soa.domain.Propriedade;
import br.com.fiap.agro.soa.dto.PropriedadeRequest;
import br.com.fiap.agro.soa.dto.PropriedadeResponse;
import br.com.fiap.agro.soa.repository.PropriedadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio do CRUD de Propriedade.
 *
 * <p>Faz a ponte entre os DTOs (fronteira REST) e a entidade de domínio, delegando a
 * persistência ao {@link PropriedadeRepository}. O controller não conhece a entidade nem o
 * repositório diretamente.</p>
 */
@Service
public class PropriedadeService {

    private final PropriedadeRepository repository;

    public PropriedadeService(PropriedadeRepository repository) {
        this.repository = repository;
    }

    public List<PropriedadeResponse> listar() {
        return repository.findAll().stream()
                .map(PropriedadeResponse::de)
                .toList();
    }

    public PropriedadeResponse buscarPorId(Long id) {
        Propriedade propriedade = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Propriedade não encontrada: id=" + id));
        return PropriedadeResponse.de(propriedade);
    }

    public PropriedadeResponse criar(PropriedadeRequest req) {
        // A própria entidade valida os dados no construtor (encapsulamento, issue #3).
        Propriedade nova = paraEntidade(req);
        return PropriedadeResponse.de(repository.save(nova));
    }

    public PropriedadeResponse atualizar(Long id, PropriedadeRequest req) {
        Propriedade existente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Propriedade não encontrada: id=" + id));
        existente.setProdutor(req.produtor());
        existente.setCultura(req.cultura());
        existente.setLatitude(req.latitude());
        existente.setLongitude(req.longitude());
        existente.setAreaHa(req.areaHa());
        existente.setMunicipio(req.municipio());
        existente.setUf(req.uf());
        return PropriedadeResponse.de(repository.save(existente));
    }

    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Propriedade não encontrada: id=" + id);
        }
        repository.deleteById(id);
    }

    private Propriedade paraEntidade(PropriedadeRequest req) {
        return new Propriedade(
                req.produtor(),
                req.cultura(),
                req.latitude(),
                req.longitude(),
                req.areaHa(),
                req.municipio(),
                req.uf()
        );
    }
}
