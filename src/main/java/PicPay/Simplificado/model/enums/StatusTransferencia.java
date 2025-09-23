package PicPay.Simplificado.model.enums;

public enum StatusTransferencia {
    PENDENTE("Pendente"),
    AUTORIZADA("Autorizada"),
    REJEITADA("Rejeitada"),
    ERRO("Erro no processamento");



    private String descricao;

    StatusTransferencia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
