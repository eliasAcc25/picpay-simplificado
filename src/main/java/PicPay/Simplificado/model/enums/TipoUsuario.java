package PicPay.Simplificado.model.enums;

public enum TipoUsuario {
    COMUM("Usuário Comum"),
    LOJISTA("Lojista");

    private String descricao;

    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
