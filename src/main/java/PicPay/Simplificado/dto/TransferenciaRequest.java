package PicPay.Simplificado.dto;

import java.math.BigDecimal;

public class TransferenciaRequest {
    private BigDecimal value;
    private Long payer;
    private Long payee;

    // Construtor padrão necessário para deserialização JSON
    public TransferenciaRequest() {
    }

    public TransferenciaRequest(BigDecimal value, Long payer, Long payee) {
        this.value = value;
        this.payer = payer;
        this.payee = payee;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Long getPayer() {
        return payer;
    }

    public void setPayer(Long payer) {
        this.payer = payer;
    }

    public Long getPayee() {
        return payee;
    }

    public void setPayee(Long payee) {
        this.payee = payee;
    }
}
