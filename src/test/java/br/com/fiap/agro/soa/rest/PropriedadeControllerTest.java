package br.com.fiap.agro.soa.rest;

import br.com.fiap.agro.soa.dto.PropriedadeResponse;
import br.com.fiap.agro.soa.service.PropriedadeService;
import br.com.fiap.agro.soa.service.RecursoNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes da camada REST ({@link PropriedadeController}) com {@code @WebMvcTest}.
 *
 * <p>Carrega apenas o slice web (controller + exception handler), com o service mockado.
 * Valida status HTTP, headers, respostas JSON e Bean Validation.</p>
 */
@WebMvcTest(PropriedadeController.class)
class PropriedadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropriedadeService service;

    private static final String BASE = "/api/propriedades";

    // --- Fixtures ---

    private PropriedadeResponse exemplo() {
        return new PropriedadeResponse(1L, "João Silva", "Soja",
                -21.17, -47.82, 120.5, "Ribeirão Preto", "SP");
    }

    private String jsonValido() {
        return """
                {
                  "produtor": "João Silva",
                  "cultura": "Soja",
                  "latitude": -21.17,
                  "longitude": -47.82,
                  "areaHa": 120.5,
                  "municipio": "Ribeirão Preto",
                  "uf": "SP"
                }
                """;
    }

    // --- GET /api/propriedades ---

    @Test
    @DisplayName("GET /api/propriedades → 200 com lista JSON")
    void deveListarTodas() throws Exception {
        when(service.listar()).thenReturn(List.of(exemplo()));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].produtor", is("João Silva")))
                .andExpect(jsonPath("$[0].cultura", is("Soja")));
    }

    // --- GET /api/propriedades/{id} ---

    @Test
    @DisplayName("GET /api/propriedades/1 → 200 quando existe")
    void deveBuscarPorIdExistente() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(exemplo());

        mockMvc.perform(get(BASE + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.uf", is("SP")));
    }

    @Test
    @DisplayName("GET /api/propriedades/999 → 404 com corpo de erro padronizado")
    void deveRetornar404QuandoNaoExiste() throws Exception {
        when(service.buscarPorId(999L))
                .thenThrow(new RecursoNaoEncontradoException("Propriedade não encontrada: id=999"));

        mockMvc.perform(get(BASE + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.mensagem", containsString("999")));
    }

    // --- POST /api/propriedades ---

    @Test
    @DisplayName("POST /api/propriedades → 201 Created com header Location")
    void deveCriarComSucesso() throws Exception {
        when(service.criar(any())).thenReturn(exemplo());

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido()))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.produtor", is("João Silva")));
    }

    @Test
    @DisplayName("POST /api/propriedades → 400 quando body vazio (campos obrigatórios)")
    void deveRejeitar400CamposObrigatorios() throws Exception {
        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.campos", aMapWithSize(greaterThan(0))));
    }

    @Test
    @DisplayName("POST /api/propriedades → 400 quando latitude fora da faixa")
    void deveRejeitar400LatitudeForaDaFaixa() throws Exception {
        String json = """
                {
                  "produtor": "Teste",
                  "cultura": "Teste",
                  "latitude": 999.0,
                  "longitude": 0.0,
                  "areaHa": 10.0,
                  "municipio": "Teste",
                  "uf": "SP"
                }
                """;

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.latitude").exists());
    }

    @Test
    @DisplayName("POST /api/propriedades → 400 quando areaHa é negativa")
    void deveRejeitar400AreaNegativa() throws Exception {
        String json = """
                {
                  "produtor": "Teste",
                  "cultura": "Teste",
                  "latitude": -10.0,
                  "longitude": -40.0,
                  "areaHa": -5.0,
                  "municipio": "Teste",
                  "uf": "SP"
                }
                """;

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.areaHa").exists());
    }

    // --- PUT /api/propriedades/{id} ---

    @Test
    @DisplayName("PUT /api/propriedades/1 → 200 atualiza com sucesso")
    void deveAtualizarComSucesso() throws Exception {
        when(service.atualizar(eq(1L), any())).thenReturn(exemplo());

        mockMvc.perform(put(BASE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtor", is("João Silva")));
    }

    @Test
    @DisplayName("PUT /api/propriedades/1 → 400 quando body inválido")
    void deveRejeitar400NoUpdate() throws Exception {
        mockMvc.perform(put(BASE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // --- DELETE /api/propriedades/{id} ---

    @Test
    @DisplayName("DELETE /api/propriedades/1 → 204 No Content")
    void deveRemoverComSucesso() throws Exception {
        doNothing().when(service).remover(1L);

        mockMvc.perform(delete(BASE + "/1"))
                .andExpect(status().isNoContent());
    }
}
