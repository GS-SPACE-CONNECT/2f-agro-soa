package br.com.fiap.agro.soa.rest;

import br.com.fiap.agro.soa.dto.PropriedadeRequest;
import br.com.fiap.agro.soa.dto.PropriedadeResponse;
import br.com.fiap.agro.soa.service.PropriedadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * API REST do CRUD de {@link br.com.fiap.agro.soa.domain.Propriedade}.
 *
 * <p>Status HTTP: 200 (consulta/atualização ok), 201 (criação, com header Location),
 * 204 (remoção ok), 404 (não encontrado) e 400 (validação) — estes dois últimos via
 * {@link GlobalExceptionHandler}.</p>
 */
@Tag(name = "Propriedades", description = "CRUD de propriedades rurais (API REST)")
@RestController
@RequestMapping("/api/propriedades")
public class PropriedadeController {

    private final PropriedadeService service;

    public PropriedadeController(PropriedadeService service) {
        this.service = service;
    }

    /** GET /api/propriedades — lista todas. */
    @Operation(summary = "Lista todas as propriedades")
    @GetMapping
    public List<PropriedadeResponse> listar() {
        return service.listar();
    }

    /** GET /api/propriedades/{id} — busca por id (404 se não existir). */
    @Operation(summary = "Busca uma propriedade por id")
    @GetMapping("/{id}")
    public PropriedadeResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    /** POST /api/propriedades — cria (201 Created + Location). */
    @Operation(summary = "Cria uma nova propriedade")
    @PostMapping
    public ResponseEntity<PropriedadeResponse> criar(@Valid @RequestBody PropriedadeRequest req) {
        PropriedadeResponse criada = service.criar(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.id())
                .toUri();
        return ResponseEntity.created(location).body(criada);
    }

    /** PUT /api/propriedades/{id} — atualiza (200 ok, 404 se não existir). */
    @Operation(summary = "Atualiza uma propriedade existente")
    @PutMapping("/{id}")
    public PropriedadeResponse atualizar(@PathVariable Long id,
                                         @Valid @RequestBody PropriedadeRequest req) {
        return service.atualizar(id, req);
    }

    /** DELETE /api/propriedades/{id} — remove (204 No Content, 404 se não existir). */
    @Operation(summary = "Remove uma propriedade")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
