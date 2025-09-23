package PicPay.Simplificado.model.entity;

import PicPay.Simplificado.model.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Saldo")
class SaldoTest {

    private Saldo saldo;
    private User usuario;
    private BigDecimal valorInicial;

    @BeforeEach
    void setUp() {
        // ARRANGE
        usuario = new User();
        usuario.setId(1L);
        usuario.setNomeCompleto("João Silva");
        usuario.setTipo(TipoUsuario.COMUM);

        valorInicial = new BigDecimal("1000.00");
        saldo = new Saldo(usuario, valorInicial);
    }

    @Test
    @DisplayName("Deve retornar true quando saldo é suficiente")
    void deveRetornarTrueQuandoSaldoSuficiente() {
        // ARRANGE
        BigDecimal valorTransferencia = new BigDecimal("500.00");

        // ACT
        boolean resultado = saldo.temSaldoSuficiente(valorTransferencia);

        // ASSERT
        assertTrue(resultado, "Deve retornar true quando saldo é suficiente");
    }

    @Test
    @DisplayName("Deve retornar true quando valor é igual ao saldo")
    void deveRetornarTrueQuandoValorIgualAoSaldo() {
        // ARRANGE
        BigDecimal valorIgualAoSaldo = new BigDecimal("1000.00");

        // ACT
        boolean resultado = saldo.temSaldoSuficiente(valorIgualAoSaldo);

        // ASSERT
        assertTrue(resultado, "Deve retornar true quando valor é igual ao saldo");
    }

    @Test
    @DisplayName("Deve retornar false quando saldo é insuficiente")
    void deveRetornarFalseQuandoSaldoInsuficiente() {
        // ARRANGE
        BigDecimal valorMaiorQueSaldo = new BigDecimal("1500.00");

        // ACT
        boolean resultado = saldo.temSaldoSuficiente(valorMaiorQueSaldo);

        // ASSERT
        assertFalse(resultado, "Deve retornar false quando saldo é insuficiente");
    }

    @Test
    @DisplayName("Deve debitar valor corretamente quando saldo é suficiente")
    void deveDebitarValorCorretamente() {
        // ARRANGE
        BigDecimal valorDebito = new BigDecimal("300.00");
        BigDecimal saldoEsperado = valorInicial.subtract(valorDebito); // 1000 - 300 = 700

        // ACT
        saldo.debitar(valorDebito);

        // ASSERT
        assertEquals(saldoEsperado, saldo.getValor(),
            "Valor do saldo deve ser reduzido corretamente");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar debitar valor maior que saldo")
    void deveLancarExcecaoAoDebitarValorMaiorQueSaldo() {
        // ARRANGE
        BigDecimal valorMaiorQueSaldo = new BigDecimal("1500.00");

        // ACT & ASSERT
        IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> saldo.debitar(valorMaiorQueSaldo),
            "Deve lançar exceção quando tentar debitar valor maior que saldo"
        );

        assertEquals("Saldo insuficiente", excecao.getMessage());

        // Verificar que o saldo não foi alterado
        assertEquals(valorInicial, saldo.getValor(),
            "Saldo não deve ser alterado quando há exceção");
    }

    @Test
    @DisplayName("Deve debitar valor igual ao saldo (zerando o saldo)")
    void deveDebitarValorIgualAoSaldo() {
        // ARRANGE
        BigDecimal valorIgualAoSaldo = new BigDecimal("1000.00");

        // ACT
        saldo.debitar(valorIgualAoSaldo);

        // ASSERT
        assertEquals(BigDecimal.ZERO, saldo.getValor(),
            "Saldo deve ficar zero quando débito é igual ao saldo");
    }

    @Test
    @DisplayName("Deve creditar valor positivo corretamente")
    void deveCreditarValorPositivo() {
        // ARRANGE
        BigDecimal valorCredito = new BigDecimal("500.00");
        BigDecimal saldoEsperado = valorInicial.add(valorCredito); // 1000 + 500 = 1500

        // ACT
        saldo.creditar(valorCredito);

        // ASSERT
        assertEquals(saldoEsperado, saldo.getValor(),
            "Valor do saldo deve ser aumentado corretamente");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar creditar valor zero")
    void deveLancarExcecaoAoCreditarValorZero() {
        // ARRANGE
        BigDecimal valorZero = BigDecimal.ZERO;

        // ACT & ASSERT
        IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> saldo.creditar(valorZero),
            "Deve lançar exceção quando tentar creditar valor zero"
        );

        assertEquals("Valor deve ser positivo", excecao.getMessage());

        // Verificar que o saldo não foi alterado
        assertEquals(valorInicial, saldo.getValor(),
            "Saldo não deve ser alterado quando há exceção");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar creditar valor negativo")
    void deveLancarExcecaoAoCreditarValorNegativo() {
        // ARRANGE
        BigDecimal valorNegativo = new BigDecimal("-100.00");

        // ACT & ASSERT
        IllegalArgumentException excecao = assertThrows(
            IllegalArgumentException.class,
            () -> saldo.creditar(valorNegativo),
            "Deve lançar exceção quando tentar creditar valor negativo"
        );

        assertEquals("Valor deve ser positivo", excecao.getMessage());

        // Verificar que o saldo não foi alterado
        assertEquals(valorInicial, saldo.getValor(),
            "Saldo não deve ser alterado quando há exceção");
    }

    /**
     * TESTE para construtor com valor inicial
     */
    @Test
    @DisplayName("Deve criar saldo com valor inicial correto")
    void deveCriarSaldoComValorInicial() {
        // ARRANGE
        BigDecimal valorEsperado = new BigDecimal("2500.00");

        // ACT
        Saldo novoSaldo = new Saldo(usuario, valorEsperado);

        // ASSERT
        assertEquals(valorEsperado, novoSaldo.getValor(),
            "Saldo deve ser criado com valor inicial correto");
        assertEquals(usuario, novoSaldo.getUser(),
            "Usuário deve estar associado corretamente");
        assertNotNull(novoSaldo.getCreatedAt(),
            "Data de criação deve ser definida");
        assertNotNull(novoSaldo.getUpdatedAt(),
            "Data de atualização deve ser definida");
    }

    @Test
    @DisplayName("Deve criar saldo com valor zero quando não especificado")
    void deveCriarSaldoComValorZero() {
        // ACT
        Saldo saldoZero = new Saldo();

        // ASSERT
        assertEquals(BigDecimal.ZERO, saldoZero.getValor(),
            "Saldo deve ser criado com valor zero por padrão");
        assertNotNull(saldoZero.getCreatedAt(),
            "Data de criação deve ser definida");
        assertNotNull(saldoZero.getUpdatedAt(),
            "Data de atualização deve ser definida");
    }

    @Test
    @DisplayName("Deve atualizar data de modificação ao debitar")
    void deveAtualizarDataAoDebitar() throws InterruptedException {
        // ARRANGE
        LocalDateTime dataAntes = saldo.getUpdatedAt();
        Thread.sleep(1); // Aguardar 1ms para garantir diferença na data

        // ACT
        saldo.debitar(new BigDecimal("100.00"));

        // ASSERT
        assertTrue(saldo.getUpdatedAt().isAfter(dataAntes),
            "Data de atualização deve ser posterior à data anterior");
    }

    @Test
    @DisplayName("Deve atualizar data de modificação ao creditar")
    void deveAtualizarDataAoCreditar() throws InterruptedException {
        // ARRANGE
        LocalDateTime dataAntes = saldo.getUpdatedAt();
        Thread.sleep(1); // Aguardar 1ms para garantir diferença na data

        // ACT
        saldo.creditar(new BigDecimal("100.00"));

        // ASSERT
        assertTrue(saldo.getUpdatedAt().isAfter(dataAntes),
            "Data de atualização deve ser posterior à data anterior");
    }
}
