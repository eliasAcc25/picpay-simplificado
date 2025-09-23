package PicPay.Simplificado.model.entity;

import PicPay.Simplificado.model.enums.StatusTransferencia;
import PicPay.Simplificado.model.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Transferencia
 *
 * Estes testes verificam:
 * - Estados da transferência (pendente, autorizada, rejeitada)
 * - Métodos de mudança de estado
 * - Regras de negócio (lojista não pode transferir, mesma pessoa)
 */
@DisplayName("Testes da Entidade Transferencia")
class TransferenciaTest {

    private Transferencia transferencia;
    private User usuarioComum;
    private User lojista;
    private User recebedor;
    private BigDecimal valor;

    @BeforeEach
    void setUp() {
        // ARRANGE - Preparar dados de teste

        // Criar usuário comum
        usuarioComum = new User();
        usuarioComum.setId(1L);
        usuarioComum.setNomeCompleto("João Silva");
        usuarioComum.setTipo(TipoUsuario.COMUM);

        // Criar lojista
        lojista = new User();
        lojista.setId(2L);
        lojista.setNomeCompleto("Loja do João");
        lojista.setTipo(TipoUsuario.LOJISTA);

        // Criar recebedor
        recebedor = new User();
        recebedor.setId(3L);
        recebedor.setNomeCompleto("Maria Santos");
        recebedor.setTipo(TipoUsuario.COMUM);

        valor = new BigDecimal("100.00");
        transferencia = new Transferencia(valor, usuarioComum, recebedor);
    }

    @Test
    @DisplayName("Deve criar transferência com status PENDENTE por padrão")
    void deveCriarTransferenciaComStatusPendente() {
        // ACT & ASSERT
        assertEquals(StatusTransferencia.PENDENTE, transferencia.getStatus(),
            "Status inicial deve ser PENDENTE");
        assertEquals(valor, transferencia.getValor(),
            "Valor deve estar correto");
        assertEquals(usuarioComum, transferencia.getPagador(),
            "Pagador deve estar correto");
        assertEquals(recebedor, transferencia.getRecebedor(),
            "Recebedor deve estar correto");
        assertNotNull(transferencia.getDataTransferencia(),
            "Data da transferência deve ser definida");
        assertFalse(transferencia.getAutorizadaExternamente(),
            "Autorização externa deve ser false por padrão");
        assertFalse(transferencia.getNotificacaoEnviada(),
            "Notificação enviada deve ser false por padrão");
    }

    @Test
    @DisplayName("Deve identificar corretamente transferência pendente")
    void deveIdentificarTransferenciaPendente() {
        // ACT & ASSERT
        assertTrue(transferencia.isPendente(),
            "Deve retornar true para transferência pendente");
        assertFalse(transferencia.isAutorizada(),
            "Deve retornar false para isAutorizada()");
        assertFalse(transferencia.isRejeitada(),
            "Deve retornar false para isRejeitada()");
    }

    @Test
    @DisplayName("Deve identificar corretamente transferência autorizada")
    void deveIdentificarTransferenciaAutorizada() {
        // ARRANGE
        transferencia.autorizar();

        // ACT & ASSERT
        assertFalse(transferencia.isPendente(),
            "Deve retornar false para isPendente()");
        assertTrue(transferencia.isAutorizada(),
            "Deve retornar true para transferência autorizada");
        assertFalse(transferencia.isRejeitada(),
            "Deve retornar false para isRejeitada()");
        assertEquals(StatusTransferencia.AUTORIZADA, transferencia.getStatus(),
            "Status deve ser AUTORIZADA");
    }

    @Test
    @DisplayName("Deve identificar corretamente transferência rejeitada")
    void deveIdentificarTransferenciaRejeitada() {
        // ARRANGE
        String motivo = "Saldo insuficiente";
        transferencia.rejeitar(motivo);

        // ACT & ASSERT
        assertFalse(transferencia.isPendente(),
            "Deve retornar false para isPendente()");
        assertFalse(transferencia.isAutorizada(),
            "Deve retornar false para isAutorizada()");
        assertTrue(transferencia.isRejeitada(),
            "Deve retornar true para transferência rejeitada");
        assertEquals(StatusTransferencia.REJEITADA, transferencia.getStatus(),
            "Status deve ser REJEITADA");
        assertEquals(motivo, transferencia.getMensagemErro(),
            "Mensagem de erro deve estar correta");
    }

    @Test
    @DisplayName("Deve autorizar transferência corretamente")
    void deveAutorizarTransferencia() {
        // ACT
        transferencia.autorizar();

        // ASSERT
        assertEquals(StatusTransferencia.AUTORIZADA, transferencia.getStatus(),
            "Status deve ser alterado para AUTORIZADA");
    }

    @Test
    @DisplayName("Deve rejeitar transferência com motivo")
    void deveRejeitarTransferenciaComMotivo() {
        // ARRANGE
        String motivoRejeicao = "Documento inválido";

        // ACT
        transferencia.rejeitar(motivoRejeicao);

        // ASSERT
        assertEquals(StatusTransferencia.REJEITADA, transferencia.getStatus(),
            "Status deve ser alterado para REJEITADA");
        assertEquals(motivoRejeicao, transferencia.getMensagemErro(),
            "Mensagem de erro deve estar correta");
    }

    @Test
    @DisplayName("Deve marcar transferência como erro")
    void deveMarcarComoErro() {
        // ARRANGE
        String mensagemErro = "Erro de conexão com o banco";

        // ACT
        transferencia.marcarComoErro(mensagemErro);

        // ASSERT
        assertEquals(StatusTransferencia.ERRO, transferencia.getStatus(),
            "Status deve ser alterado para ERRO");
        assertEquals(mensagemErro, transferencia.getMensagemErro(),
            "Mensagem de erro deve estar correta");
    }

    @Test
    @DisplayName("Deve marcar autorização externa")
    void deveMarcarAutorizacaoExterna() {
        // ACT
        transferencia.marcarAutorizacaoExterna();

        // ASSERT
        assertTrue(transferencia.getAutorizadaExternamente(),
            "Autorização externa deve ser marcada como true");
    }

    @Test
    @DisplayName("Deve marcar notificação como enviada")
    void deveMarcarNotificacaoEnviada() {
        // ACT
        transferencia.marcarNotificacaoEnviada();

        // ASSERT
        assertTrue(transferencia.getNotificacaoEnviada(),
            "Notificação enviada deve ser marcada como true");
    }


    @Test
    @DisplayName("Usuário comum deve poder transferir")
    void usuarioComumDevePoderTransferir() {
        // ACT
        boolean podeTransferir = transferencia.pagadorPodeTransferir();

        // ASSERT
        assertTrue(podeTransferir,
            "Usuário comum deve poder realizar transferências");
    }

    @Test
    @DisplayName("Lojista não deve poder transferir")
    void lojistaNaoDevePoderTransferir() {
        // ARRANGE
        Transferencia transferenciaLojista = new Transferencia(valor, lojista, recebedor);

        // ACT
        boolean podeTransferir = transferenciaLojista.pagadorPodeTransferir();

        // ASSERT
        assertFalse(podeTransferir,
            "Lojista não deve poder realizar transferências");
    }

    @Test
    @DisplayName("Deve detectar quando pagador e recebedor são a mesma pessoa")
    void deveDetectarMesmaPessoa() {
        // ARRANGE
        Transferencia transferenciaMesmaPessoa = new Transferencia(valor, usuarioComum, usuarioComum);

        // ACT
        boolean mesmaPessoa = transferenciaMesmaPessoa.mesmaPessoa();

        // ASSERT
        assertTrue(mesmaPessoa,
            "Deve detectar quando pagador e recebedor são a mesma pessoa");
    }

    @Test
    @DisplayName("Deve detectar quando pagador e recebedor são pessoas diferentes")
    void deveDetectarPessoasDiferentes() {
        // ACT
        boolean mesmaPessoa = transferencia.mesmaPessoa();

        // ASSERT
        assertFalse(mesmaPessoa,
            "Deve detectar quando pagador e recebedor são pessoas diferentes");
    }


    @Test
    @DisplayName("Deve criar transferência com construtor padrão")
    void deveCriarTransferenciaComConstrutorPadrao() {
        // ACT
        Transferencia transferenciaVazia = new Transferencia();

        // ASSERT
        assertEquals(StatusTransferencia.PENDENTE, transferenciaVazia.getStatus(),
            "Status deve ser PENDENTE por padrão");
        assertNotNull(transferenciaVazia.getDataTransferencia(),
            "Data da transferência deve ser definida");
        assertFalse(transferenciaVazia.getAutorizadaExternamente(),
            "Autorização externa deve ser false por padrão");
        assertFalse(transferenciaVazia.getNotificacaoEnviada(),
            "Notificação enviada deve ser false por padrão");
    }

    @Test
    @DisplayName("Deve manter integridade ao chamar múltiplas mudanças de estado")
    void deveManterIntegridadeComMultiplasMudancas() {
        // ARRANGE & ACT
        transferencia.autorizar();
        transferencia.marcarAutorizacaoExterna();
        transferencia.marcarNotificacaoEnviada();

        // ASSERT
        assertEquals(StatusTransferencia.AUTORIZADA, transferencia.getStatus(),
            "Status deve permanecer AUTORIZADA");
        assertTrue(transferencia.getAutorizadaExternamente(),
            "Autorização externa deve permanecer true");
        assertTrue(transferencia.getNotificacaoEnviada(),
            "Notificação enviada deve permanecer true");
        assertTrue(transferencia.isAutorizada(),
            "Método isAutorizada() deve retornar true");
    }

    @Test
    @DisplayName("Deve permitir rejeitar transferência após autorização")
    void devePermitirRejeitarAposAutorizacao() {
        // ARRANGE
        transferencia.autorizar();
        String motivoRejeicao = "Fraude detectada";

        // ACT
        transferencia.rejeitar(motivoRejeicao);

        // ASSERT
        assertEquals(StatusTransferencia.REJEITADA, transferencia.getStatus(),
            "Status deve ser alterado para REJEITADA mesmo após autorização");
        assertEquals(motivoRejeicao, transferencia.getMensagemErro(),
            "Mensagem de erro deve estar correta");
        assertTrue(transferencia.isRejeitada(),
            "Método isRejeitada() deve retornar true");
        assertFalse(transferencia.isAutorizada(),
            "Método isAutorizada() deve retornar false após rejeição");
    }
}
