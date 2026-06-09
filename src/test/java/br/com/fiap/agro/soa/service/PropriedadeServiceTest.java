package br.com.fiap.agro.soa.service;

import br.com.fiap.agro.soa.domain.Propriedade;
import br.com.fiap.agro.soa.dto.PropriedadeRequest;
import br.com.fiap.agro.soa.dto.PropriedadeResponse;
import br.com.fiap.agro.soa.repository.PropriedadeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link PropriedadeService}.
 *
 * <p>O repositório é mockado: cada teste valida a lógica de negócio isoladamente,
 * sem dependência de banco de dados ou contexto Spring.</p>
 */
@ExtendWith(MockitoExtension.class)
class PropriedadeServiceTest {

    @Mock
    private PropriedadeRepository repository;

    @InjectMocks
    private PropriedadeService service;

    // --- Fixtures ---

    private Propriedade criarEntidade() {
        Propriedade p = new Propriedade("João Silva", "Soja",
                -21.17, -47.82, 120.5, "Ribeirão Preto", "SP");
        p.setId(1L);
        return p;
    }

    private PropriedadeRequest criarRequest() {
        return new PropriedadeRequest("João Silva", "Soja",
                -21.17, -47.82, 120.5, "Ribeirão Preto", "SP");
    }

    // --- listar() ---

    @Test
    @DisplayName("listar() deve retornar todos os registros convertidos em DTO")
    void deveListar() {
        when(repository.findAll()).thenReturn(List.of(criarEntidade()));

        List<PropriedadeResponse> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("João Silva", resultado.get(0).produtor());
        assertEquals("Soja", resultado.get(0).cultura());
    }

    @Test
    @DisplayName("listar() deve retornar lista vazia quando não há registros")
    void deveListarVazia() {
        when(repository.findAll()).thenReturn(List.of());

        List<PropriedadeResponse> resultado = service.listar();

        assertTrue(resultado.isEmpty());
    }

    // --- buscarPorId() ---

    @Test
    @DisplayName("buscarPorId() deve retornar DTO quando id existe")
    void deveBuscarPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(criarEntidade()));

        PropriedadeResponse resp = service.buscarPorId(1L);

        assertEquals(1L, resp.id());
        assertEquals("Soja", resp.cultura());
        assertEquals("SP", resp.uf());
    }

    @Test
    @DisplayName("buscarPorId() deve lançar RecursoNaoEncontradoException quando id não existe")
    void deveLancarExcecaoIdInexistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.buscarPorId(999L));

        assertTrue(ex.getMessage().contains("999"));
    }

    // --- criar() ---

    @Test
    @DisplayName("criar() deve persistir e retornar DTO com id")
    void deveCriar() {
        when(repository.save(any(Propriedade.class))).thenReturn(criarEntidade());

        PropriedadeResponse resp = service.criar(criarRequest());

        assertNotNull(resp.id());
        assertEquals("João Silva", resp.produtor());
        verify(repository).save(any(Propriedade.class));
    }

    // --- atualizar() ---

    @Test
    @DisplayName("atualizar() deve modificar a entidade existente e retornar DTO")
    void deveAtualizar() {
        Propriedade existente = criarEntidade();
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Propriedade.class))).thenReturn(existente);

        PropriedadeResponse resp = service.atualizar(1L, criarRequest());

        assertEquals("João Silva", resp.produtor());
        verify(repository).findById(1L);
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("atualizar() deve lançar exceção quando id não existe")
    void deveLancarExcecaoAoAtualizarInexistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.atualizar(999L, criarRequest()));

        verify(repository, never()).save(any());
    }

    // --- remover() ---

    @Test
    @DisplayName("remover() deve deletar quando id existe")
    void deveRemover() {
        when(repository.existsById(1L)).thenReturn(true);

        service.remover(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("remover() deve lançar exceção quando id não existe")
    void deveLancarExcecaoAoRemoverInexistente() {
        when(repository.existsById(999L)).thenReturn(false);

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.remover(999L));

        verify(repository, never()).deleteById(anyLong());
    }
}
