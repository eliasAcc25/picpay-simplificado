package PicPay.Simplificado.model.entity;

import PicPay.Simplificado.model.enums.StatusTransferencia;
import PicPay.Simplificado.model.enums.TipoUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencias")
public class Transferencia {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Valor da transferência é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    // Relacionamento: Uma transferência tem UM pagador (quem envia)
    @ManyToOne
    @JoinColumn(name = "pagador_id", nullable = false)
    @NotNull(message = "Pagador é obrigatório")
    private User pagador;

    // MUITAS transferências podem ter o MESMO usuário como pagador
    // Um USUÁRIO pode fazer várias transferências
    // Uma transferência tem apenas um pagador
    @ManyToOne
    @JoinColumn(name = "recebedor_id", nullable = false)
    @NotNull(message = "Recebedor é obrigatório")
    private User recebedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusTransferencia status;

    @Column(name = "data_transferencia", nullable = false)
    private LocalDateTime dataTransferencia;

    @Column(name = "mensagem_erro")
    private String mensagemErro;

    @Column(name = "autorizada_externamente")
    private Boolean autorizadaExternamente;

    @Column(name = "notificacao_enviada")
    private Boolean notificacaoEnviada;

    public Transferencia() {
        this.dataTransferencia = LocalDateTime.now();
        this.status = StatusTransferencia.PENDENTE;
        this.autorizadaExternamente = false;
        this.notificacaoEnviada = false;
    }

    public Transferencia(BigDecimal valor, User pagador, User recebedor) {
        this.valor = valor;
        this.pagador = pagador;
        this.recebedor = recebedor;
        this.dataTransferencia = LocalDateTime.now();
        this.status = StatusTransferencia.PENDENTE;
        this.autorizadaExternamente = false;
        this.notificacaoEnviada = false;
    }

    // Métodos de negócio
    public boolean isPendente() {
        return this.status == StatusTransferencia.PENDENTE;
    }

    public boolean isAutorizada() {
        return this.status == StatusTransferencia.AUTORIZADA;
    }

    public boolean isRejeitada() {
        return this.status == StatusTransferencia.REJEITADA;
    }

    public void autorizar() {
        this.status = StatusTransferencia.AUTORIZADA;
    }

    public void rejeitar(String motivo) {
        this.status = StatusTransferencia.REJEITADA;
        this.mensagemErro = motivo;
    }

    public void marcarComoErro(String mensagem) {
        this.status = StatusTransferencia.ERRO;
        this.mensagemErro = mensagem;
    }

    public void marcarAutorizacaoExterna() {
        this.autorizadaExternamente = true;
    }

    public void marcarNotificacaoEnviada() {
        this.notificacaoEnviada = true;
    }

    // Validação de regra de negócio
    public boolean pagadorPodeTransferir() {
        // Lojista não pode transferir, só receber
        return !pagador.getTipo().equals(TipoUsuario.LOJISTA);
    }

    public boolean mesmaPessoa() {
        return pagador.getId().equals(recebedor.getId());
    }

    // Getters e Setters
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

    public User getPagador() {
        return pagador;
    }

    public void setPagador(User pagador) {
        this.pagador = pagador;
    }

    public User getRecebedor() {
        return recebedor;
    }

    public void setRecebedor(User recebedor) {
        this.recebedor = recebedor;
    }

    public StatusTransferencia getStatus() {
        return status;
    }

    public void setStatus(StatusTransferencia status) {
        this.status = status;
    }

    public LocalDateTime getDataTransferencia() {
        return dataTransferencia;
    }

    public void setDataTransferencia(LocalDateTime dataTransferencia) {
        this.dataTransferencia = dataTransferencia;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public Boolean getAutorizadaExternamente() {
        return autorizadaExternamente;
    }

    public void setAutorizadaExternamente(Boolean autorizadaExternamente) {
        this.autorizadaExternamente = autorizadaExternamente;
    }

    public Boolean getNotificacaoEnviada() {
        return notificacaoEnviada;
    }

    public void setNotificacaoEnviada(Boolean notificacaoEnviada) {
        this.notificacaoEnviada = notificacaoEnviada;
    }
}
