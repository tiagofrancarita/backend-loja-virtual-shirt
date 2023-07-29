package br.com.franca.ShirtVirtual.enums;

public enum StatusVendaLojaVirtual {

    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada"),
    ABANDONO_CARRINHO("Carrinho abandonado");

   private String descricao = "";


    StatusVendaLojaVirtual(String valor) {
        this.descricao = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }
}
