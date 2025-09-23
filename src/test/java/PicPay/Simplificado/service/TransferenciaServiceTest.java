package PicPay.Simplificado.service;

import PicPay.Simplificado.model.entity.Saldo;
import PicPay.Simplificado.model.entity.Transferencia;
import PicPay.Simplificado.model.entity.User;
import PicPay.Simplificado.model.enums.TipoUsuario;
import PicPay.Simplificado.repository.SaldoRepository;
import PicPay.Simplificado.repository.TransferenciaRepository;
import PicPay.Simplificado.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

// ESSA CLASSE TESTA A LÓGICA DE NEGÓCIO DA SERVICE DE TRANSFERÊNCIA
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do Serviço de Transferência")
class TransferenciaServiceTest {

    // Mocks das dependências (objetos simulados)
    @Mock
    private SaldoRepository saldoRepository;

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    // Classe que será testada (com as dependências injetadas automaticamente)
    @InjectMocks
    private TransferenciaService transferenciaService;

    // Dados de teste que serão reutilizados
    private User usuarioComum;
    private User lojista;
    private User recebedor;
    private Saldo saldoPagador;
    private Saldo saldoRecebedor;
    private BigDecimal valorTransferencia;

    @BeforeEach
    void setUp() {
        // ARRANGE - Preparar os dados de teste

        usuarioComum = new User();
        usuarioComum.setId(1L);
        usuarioComum.setNomeCompleto("João Silva");
        usuarioComum.setEmail("joao@email.com");
        usuarioComum.setTipo(TipoUsuario.COMUM);
        usuarioComum.setCreatedAt(LocalDateTime.now());

        lojista = new User();
        lojista.setId(2L);
        lojista.setNomeCompleto("Loja do João");
        lojista.setEmail("loja@email.com");
        lojista.setTipo(TipoUsuario.LOJISTA);
        lojista.setCreatedAt(LocalDateTime.now());

        recebedor = new User();
        recebedor.setId(3L);
        recebedor.setNomeCompleto("Maria Santos");
        recebedor.setEmail("maria@email.com");
        recebedor.setTipo(TipoUsuario.COMUM);
        recebedor.setCreatedAt(LocalDateTime.now());

        saldoPagador = new Saldo();
        saldoPagador.setId(1L);
        saldoPagador.setUser(usuarioComum);
        saldoPagador.setValor(new BigDecimal("1000.00"));

        // Criar saldo do recebedor (R$ 500)
        saldoRecebedor = new Saldo();
        saldoRecebedor.setId(2L);
        saldoRecebedor.setUser(recebedor);
        saldoRecebedor.setValor(new BigDecimal("500.00"));

        valorTransferencia = new BigDecimal("100.00");
    }

    @Test
    @DisplayName("Deve realizar transferência com sucesso quando todos os dados estão corretos")
    void deveRealizarTransferenciaComSucesso() {
        // ARRANGE - Preparar (configurar os mocks)
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioComum));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(recebedor));
        when(saldoRepository.findByUser_Id(1L)).thenReturn(Optional.of(saldoPagador));
        when(saldoRepository.findByUser_Id(3L)).thenReturn(Optional.of(saldoRecebedor));


        Transferencia transferenciaEsperada = new Transferencia(valorTransferencia, usuarioComum, recebedor);
        transferenciaEsperada.setId(1L);
        when(transferenciaRepository.save(any(Transferencia.class))).thenReturn(transferenciaEsperada);

        // ACT
        Transferencia resultado = transferenciaService.realizarTransferencia(
            valorTransferencia, 1L, 3L
        );

        // ASSERT
        assertNotNull(resultado, "A transferência não deve ser null");
        assertEquals(valorTransferencia, resultado.getValor(), "O valor deve estar correto");
        assertEquals(usuarioComum, resultado.getPagador(), "O pagador deve estar correto");
        assertEquals(recebedor, resultado.getRecebedor(), "O recebedor deve estar correto");

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).findById(3L);
        verify(saldoRepository).findByUser_Id(1L);
        verify(saldoRepository).findByUser_Id(3L);
        verify(saldoRepository, times(2)).save(any(Saldo.class)); // Salva pagador e recebedor
        verify(transferenciaRepository).save(any(Transferencia.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando lojista tenta fazer transferência")
    void deveLancarExcecaoQuandoLojistaTentaTransferir() {
        // ARRANGE
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(lojista));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(recebedor));

        // ACT/ASSERT
        IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> transferenciaService.realizarTransferencia(valorTransferencia, 2L, 3L),
            "Deveria lançar IllegalArgumentException para lojista"
        );

        assertEquals("Lojista não pode realizar transferências.", excecao.getMessage());

        verify(saldoRepository, never()).findByUser_Id(anyLong());
        verify(saldoRepository, never()).save(any(Saldo.class));
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pagador tem saldo insuficiente")
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        // ARRANGE
        saldoPagador.setValor(new BigDecimal("50.00"));
        BigDecimal valorMaiorQueSaldo = new BigDecimal("100.00");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioComum));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(recebedor));
        when(saldoRepository.findByUser_Id(1L)).thenReturn(Optional.of(saldoPagador));

        // ACT/ASSERT
        IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> transferenciaService.realizarTransferencia(valorMaiorQueSaldo, 1L, 3L)
        );

        assertEquals("Saldo insuficiente para transferência.", excecao.getMessage());

        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pagador não é encontrado")
    void deveLancarExcecaoQuandoPagadorNaoEncontrado() {
        // ARRANGE
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT/ASSERT
        IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> transferenciaService.realizarTransferencia(valorTransferencia, 999L, 3L)
        );

        assertEquals("Pagador não encontrado", excecao.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando recebedor não é encontrado")
    void deveLancarExcecaoQuandoRecebedorNaoEncontrado() {
        // ARRANGE
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioComum));
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT/ASSERT
        IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> transferenciaService.realizarTransferencia(valorTransferencia, 1L, 999L)
        );

        assertEquals("Recebedor não encontrado", excecao.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando pagador e recebedor são a mesma pessoa")
    void deveLancarExcecaoQuandoPagadorERecebedorSaoIguais() {
        // ARRANGE
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioComum));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioComum));

        // ACT/ASSERT
        IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> transferenciaService.realizarTransferencia(valorTransferencia, 1L, 1L)
        );

        assertEquals("Pagador e recebedor devem ser diferentes.", excecao.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo do pagador não é encontrado")
    void deveLancarExcecaoQuandoSaldoPagadorNaoEncontrado() {
        // ARRANGE
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioComum));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(recebedor));
        when(saldoRepository.findByUser_Id(1L)).thenReturn(Optional.empty());

        // ACT/ASSERT
        IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> transferenciaService.realizarTransferencia(valorTransferencia, 1L, 3L)
        );

        assertEquals("Saldo do pagador não encontrado.", excecao.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar corretamente os saldos após transferência")
    void deveAtualizarSaldosCorretamente() {
        // ARRANGE
        BigDecimal saldoInicialPagador = new BigDecimal("1000.00");
        BigDecimal saldoInicialRecebedor = new BigDecimal("500.00");
        BigDecimal valorTransferencia = new BigDecimal("100.00");

        saldoPagador.setValor(saldoInicialPagador);
        saldoRecebedor.setValor(saldoInicialRecebedor);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioComum));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(recebedor));
        when(saldoRepository.findByUser_Id(1L)).thenReturn(Optional.of(saldoPagador));
        when(saldoRepository.findByUser_Id(3L)).thenReturn(Optional.of(saldoRecebedor));
        when(transferenciaRepository.save(any(Transferencia.class)))
            .thenReturn(new Transferencia(valorTransferencia, usuarioComum, recebedor));

        // ACT
        transferenciaService.realizarTransferencia(valorTransferencia, 1L, 3L);

        // ASSERT
        assertEquals(
            saldoInicialPagador.subtract(valorTransferencia),
            saldoPagador.getValor(),
            "Saldo do pagador deve ser reduzido"
        );
        assertEquals(
            saldoInicialRecebedor.add(valorTransferencia),
            saldoRecebedor.getValor(),
            "Saldo do recebedor deve ser aumentado"
        );
    }
}
