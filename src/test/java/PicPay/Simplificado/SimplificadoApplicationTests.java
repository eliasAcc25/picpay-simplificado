package PicPay.Simplificado;

import PicPay.Simplificado.dto.TransferenciaRequest;
import PicPay.Simplificado.model.entity.Saldo;
import PicPay.Simplificado.model.entity.User;
import PicPay.Simplificado.model.enums.TipoUsuario;
import PicPay.Simplificado.repository.SaldoRepository;
import PicPay.Simplificado.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


 // TESTE DE INTEGRAÇÃO COMPLETO

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureWebMvc
@DisplayName("Testes de Integração Completos")
class SimplificadoApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SaldoRepository saldoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private User usuarioPagador;
    private User usuarioRecebedor;
    private User lojista;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Limpar dados
        saldoRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Criar usuários de teste
        criarUsuariosDeTest();
    }

    private void criarUsuariosDeTest() {
        // Criar usuário pagador com CPF único para teste
        usuarioPagador = new User();
        usuarioPagador.setNomeCompleto("João Pagador");
        usuarioPagador.setCpfOuCnpj("11122233401"); // CPF único para teste
        usuarioPagador.setEmail("joao.teste@pagador.com");
        usuarioPagador.setSenha("senha123");
        usuarioPagador.setTipo(TipoUsuario.COMUM);
        usuarioPagador = usuarioRepository.save(usuarioPagador);

        // Criar saldo para pagador
        Saldo saldoPagador = new Saldo(usuarioPagador, new BigDecimal("1000.00"));
        saldoRepository.save(saldoPagador);

        // Criar usuário recebedor com CPF único
        usuarioRecebedor = new User();
        usuarioRecebedor.setNomeCompleto("Maria Recebedora");
        usuarioRecebedor.setCpfOuCnpj("44455566609"); // CPF único para teste
        usuarioRecebedor.setEmail("maria.teste@recebedora.com");
        usuarioRecebedor.setSenha("senha456");
        usuarioRecebedor.setTipo(TipoUsuario.COMUM);
        usuarioRecebedor = usuarioRepository.save(usuarioRecebedor);

        // Criar saldo para recebedor
        Saldo saldoRecebedor = new Saldo(usuarioRecebedor, new BigDecimal("500.00"));
        saldoRepository.save(saldoRecebedor);

        // Criar lojista com CNPJ único
        lojista = new User();
        lojista.setNomeCompleto("Loja do Teste");
        lojista.setCpfOuCnpj("77788899000195"); // CNPJ único para teste
        lojista.setEmail("loja.teste@teste.com");
        lojista.setSenha("senha789");
        lojista.setTipo(TipoUsuario.LOJISTA);
        lojista = usuarioRepository.save(lojista);

        // Criar saldo para lojista
        Saldo saldoLojista = new Saldo(lojista, new BigDecimal("0.00"));
        saldoRepository.save(saldoLojista);
    }


     // TESTE DE INTEGRAÇÃO 1: BEM SUCEDIDA

    @Test
    @DisplayName("Deve realizar transferência completa com sucesso")
    @Transactional
    void deveRealizarTransferenciaCompletaComSucesso() throws Exception {
        // ARRANGE
        TransferenciaRequest request = new TransferenciaRequest();
        request.setValue(new BigDecimal("100.00"));
        request.setPayer(usuarioPagador.getId());
        request.setPayee(usuarioRecebedor.getId());

        // ACT/ASSERT
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(100.00))
                .andExpect(jsonPath("$.pagador.id").value(usuarioPagador.getId()))
                .andExpect(jsonPath("$.recebedor.id").value(usuarioRecebedor.getId()));

        // Verificar se os saldos foram atualizados no banco
        Saldo saldoPagadorAtualizado = saldoRepository.findByUser_Id(usuarioPagador.getId()).orElseThrow();
        Saldo saldoRecebedorAtualizado = saldoRepository.findByUser_Id(usuarioRecebedor.getId()).orElseThrow();

        assertEquals(new BigDecimal("900.00"), saldoPagadorAtualizado.getValor(),
            "Saldo do pagador deve ser reduzido");
        assertEquals(new BigDecimal("600.00"), saldoRecebedorAtualizado.getValor(),
            "Saldo do recebedor deve ser aumentado");
    }


     // TESTE DE INTEGRAÇÃO 2: LOGISTICA NÃO TRANSFERE

    @Test
    @DisplayName("Deve rejeitar transferência de lojista")
    void deveRejeitarTransferenciaLojista() throws Exception {
        // ARRANGE
        TransferenciaRequest request = new TransferenciaRequest();
        request.setValue(new BigDecimal("100.00"));
        request.setPayer(lojista.getId());
        request.setPayee(usuarioRecebedor.getId());

        // ACT/ASSERT
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Lojista não pode realizar transferências."));
    }


     // TESTE DE INTEGRAÇÃO 3: SALDO INSUFICIENTE

    @Test
    @DisplayName("Deve rejeitar transferência por saldo insuficiente")
    void deveRejeitarTransferenciaSaldoInsuficiente() throws Exception {
        // ARRANGE
        TransferenciaRequest request = new TransferenciaRequest();
        request.setValue(new BigDecimal("2000.00")); // Maior que o saldo de R$ 1000
        request.setPayer(usuarioPagador.getId());
        request.setPayee(usuarioRecebedor.getId());

        // ACT/ASSERT
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Saldo insuficiente para transferência."));
    }


     // TESTE DE INTEGRAÇÃO 4: USUÁRIO INEXISTENTE

    @Test
    @DisplayName("Deve rejeitar transferência para usuário inexistente")
    void deveRejeitarTransferenciaUsuarioInexistente() throws Exception {
        // ARRANGE
        TransferenciaRequest request = new TransferenciaRequest();
        request.setValue(new BigDecimal("100.00"));
        request.setPayer(usuarioPagador.getId());
        request.setPayee(999L); // ID inexistente

        // ACT/ASSERT
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Recebedor não encontrado"));
    }


     // TESTE DE INTEGRAÇÃO 5: TRANSFERÊNCIA PARA SI MESMO

    @Test
    @DisplayName("Deve rejeitar transferência para si mesmo")
    void deveRejeitarTransferenciaParaSiMesmo() throws Exception {
        // ARRANGE
        TransferenciaRequest request = new TransferenciaRequest();
        request.setValue(new BigDecimal("100.00"));
        request.setPayer(usuarioPagador.getId());
        request.setPayee(usuarioPagador.getId()); // Mesmo usuário

        // ACT/ASSERT
        mockMvc.perform(post("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Pagador e recebedor devem ser diferentes."));
    }

    @Test
    @DisplayName("Contexto da aplicação deve carregar sem erros")
    void contextLoads() {

    }
}
