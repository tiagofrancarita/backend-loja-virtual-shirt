package br.com.franca.ShirtVirtual.utils.dto;

import br.com.franca.ShirtVirtual.model.ItemVendaLoja;
import br.com.franca.ShirtVirtual.model.Produto;

public class ItemVendaDTO extends ItemVendaLoja {

    private Double quantidade;

    private Produto produto;

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
