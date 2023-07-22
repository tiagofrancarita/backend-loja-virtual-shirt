package br.com.franca.ShirtVirtual.utils.dto;

import java.io.Serializable;

public class AvaliacaoProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String descricaoAvaliacaoProduto;

    private Integer notaAvaliacaoProduto;

    private Long produto;

    private Long pessoaFisica;

    private Long pessoaJuridica;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricaoAvaliacaoProduto() {
        return descricaoAvaliacaoProduto;
    }

    public void setDescricaoAvaliacaoProduto(String descricaoAvaliacaoProduto) {
        this.descricaoAvaliacaoProduto = descricaoAvaliacaoProduto;
    }

    public Integer getNotaAvaliacaoProduto() {
        return notaAvaliacaoProduto;
    }

    public void setNotaAvaliacaoProduto(Integer notaAvaliacaoProduto) {
        this.notaAvaliacaoProduto = notaAvaliacaoProduto;
    }

    public Long getProduto() {
        return produto;
    }

    public void setProduto(Long produto) {
        this.produto = produto;
    }

    public Long getPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(Long pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    public Long getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(Long pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }
}
