package PicPay.Simplificado.controller;

import PicPay.Simplificado.dto.TransferenciaRequest;
import PicPay.Simplificado.model.entity.Transferencia;
import PicPay.Simplificado.service.TransferenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Controller de Transferência")
class TransferControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransferenciaService transferenciaService;

    @InjectMocks
    private TransferController transferController;

    private ObjectMapper objectMapper;
    private TransferenciaRequest request;
    private Transferencia transferenciaEsperada;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transferController).build();
        objectMapper = new ObjectMapper();

        // ARRANGE
        request = new TransferenciaRequest();
        request.setValue(new BigDecimal("100.00"));
        request.setPayer(1L);
        request.setPayee(2L);

        transferenciaEsperada = new Transferencia();
        transferenciaEsperada.setId(1L);
        transferenciaEsperada.setValor(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Deve processar transferência com sucesso via POST /transfer")
    void deveProcessarTransferenciaComSucesso() throws Exception {
        // ARRANGE
        when(transferenciaService.realizarTransferencia(
            any(BigDecimal.class), anyLong(), anyLong()
        )).thenReturn(transferenciaEsperada);

        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // Verifica status 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.valor").value(100.00));
    }

    @Test
    @DisplayName("Deve retornar BadRequest quando dados são inválidos")
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        // ARRANGE
        when(transferenciaService.realizarTransferencia(
            any(BigDecimal.class), anyLong(), anyLong()
        )).thenThrow(new IllegalArgumentException("Saldo insuficiente"));

        // ACT/ASSERT
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()) // Verifica status 400
                .andExpect(content().string("Saldo insuficiente"));
    }

    @Test
    @DisplayName("Deve retornar InternalServerError quando há erro inesperado")
    void deveRetornarInternalServerErrorQuandoErroInesperado() throws Exception {
        // ARRANGE
        when(transferenciaService.realizarTransferencia(
            any(BigDecimal.class), anyLong(), anyLong()
        )).thenThrow(new RuntimeException("Erro no banco de dados"));

        // ACT/ASSERT
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError()) // Verifica status 500
                .andExpect(content().string("Erro interno do servidor"));
    }

    @Test
    @DisplayName("Deve retornar BadRequest quando JSON é malformado")
    void deveRetornarBadRequestQuandoJsonMalformado() throws Exception {
        // ARRANGE
        String jsonInvalido = "{ invalid json }";

        // ACT/ASSERT
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest()); // Verifica status 400
    }

    @Test
    @DisplayName("Deve processar mesmo com campos nulos (validação no service)")
    void deveProcessarMesmoComCamposNulos() throws Exception {
        // ARRANGE
        TransferenciaRequest requestIncompleta = new TransferenciaRequest();

        when(transferenciaService.realizarTransferencia(
            any(), any(), any()
        )).thenThrow(new IllegalArgumentException("Dados obrigatórios ausentes"));

        // ACT/ASSERT
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestIncompleta)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dados obrigatórios ausentes"));
    }

    @Test
    @DisplayName("Deve retornar UnsupportedMediaType para Content-Type incorreto")
    void deveRetornarUnsupportedMediaTypeParaContentTypeIncorreto() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.TEXT_PLAIN) // Content-Type errado
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnsupportedMediaType()); // Verifica status 415
    }

    @Test
    @DisplayName("Deve retornar MethodNotAllowed para métodos HTTP incorretos")
    void deveRetornarMethodNotAllowedParaMetodoIncorreto() throws Exception {
        // ACT/ASSERT
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .get("/transfer")) // GET em vez de POST
                .andExpect(status().isMethodNotAllowed()); // Verifica status 405
    }
}
