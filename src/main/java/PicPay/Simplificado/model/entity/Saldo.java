package PicPay.Simplificado.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "saldos")
public class Saldo {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Valor do saldo não pode ser nulo")
    @DecimalMin(value = "0.0", message = "Saldo não pode ser negativo")
    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    // Relacionamento: Um saldo pertence a UM usuário
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Construtor padrão obrigatório para JPA
    public Saldo() {
        this.valor = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Saldo(User user) {
        this.user = user;
        this.valor = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Saldo(User user, BigDecimal valorInicial) {
        this.user = user;
        this.valor = valorInicial;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Métodos de negócio
    public boolean temSaldoSuficiente(BigDecimal valorTransferencia) {
        return this.valor.compareTo(valorTransferencia) >= 0;
    }

    public void debitar(BigDecimal valorDebito) {
        if (!temSaldoSuficiente(valorDebito)) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        this.valor = this.valor.subtract(valorDebito);
        this.updatedAt = LocalDateTime.now();
    }

    public void creditar(BigDecimal valorCredito) {
        if (valorCredito.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        this.valor = this.valor.add(valorCredito);
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
