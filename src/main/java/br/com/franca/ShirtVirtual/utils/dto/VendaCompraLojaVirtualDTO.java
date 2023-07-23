package br.com.franca.ShirtVirtual.utils.dto;

import br.com.franca.ShirtVirtual.model.Endereco;
import br.com.franca.ShirtVirtual.model.FormaPagamento;
import br.com.franca.ShirtVirtual.model.Pessoa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendaCompraLojaVirtualDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private BigDecimal valorTotalVendaLoja;

    private Pessoa pessoa;

    private Endereco enderecoCobranca;

    private Endereco enderecoEntrega;

    private BigDecimal valorTotalDescontoVendaLoja;

    private FormaPagamento formaPagamento;

    private BigDecimal valorTotalFrete;

    private Integer diasEntrega;

    private Date dtVenda;

    private Date dtEntrega;

    List<ItemVendaDTO> itensVendaLojaDTO = new ArrayList<ItemVendaDTO>();

    private List<ItemVendaDTO> itemVendaLoja = new ArrayList<ItemVendaDTO>();

    public List<ItemVendaDTO> getItensVendaLojaDTO() {
        return itensVendaLojaDTO;
    }

    public void setItensVendaLojaDTO(List<ItemVendaDTO> itensVendaLojaDTO) {
        this.itensVendaLojaDTO = itensVendaLojaDTO;
    }

    public List<ItemVendaDTO> getItemVendaLoja() {
        return itemVendaLoja;
    }

    public void setItemVendaLoja(List<ItemVendaDTO> itemVendaLoja) {
        this.itemVendaLoja = itemVendaLoja;
    }

    public Integer getDiasEntrega() {
        return diasEntrega;
    }

    public void setDiasEntrega(Integer diasEntrega) {
        this.diasEntrega = diasEntrega;
    }

    public Date getDtVenda() {
        return dtVenda;
    }

    public void setDtVenda(Date dtVenda) {
        this.dtVenda = dtVenda;
    }

    public Date getDtEntrega() {
        return dtEntrega;
    }

    public void setDtEntrega(Date dtEntrega) {
        this.dtEntrega = dtEntrega;
    }

    public BigDecimal getValorTotalFrete() {
        return valorTotalFrete;
    }

    public void setValorTotalFrete(BigDecimal valorTotalFrete) {
        this.valorTotalFrete = valorTotalFrete;
    }


    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal getValorTotalDescontoVendaLoja() {
        return valorTotalDescontoVendaLoja;
    }

    public void setValorTotalDescontoVendaLoja(BigDecimal valorTotalDescontoVendaLoja) {
        this.valorTotalDescontoVendaLoja = valorTotalDescontoVendaLoja;
    }

    public Endereco getEnderecoCobranca() {
        return enderecoCobranca;
    }

    public void setEnderecoCobranca(Endereco enderecoCobranca) {
        this.enderecoCobranca = enderecoCobranca;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public BigDecimal getValorTotalVendaLoja() {
        return valorTotalVendaLoja;
    }

    public void setValorTotalVendaLoja(BigDecimal valorTotalVendaLoja) {
        this.valorTotalVendaLoja = valorTotalVendaLoja;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
