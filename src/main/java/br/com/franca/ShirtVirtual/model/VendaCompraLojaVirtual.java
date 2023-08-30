package br.com.franca.ShirtVirtual.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vd_cp_loja_virt")
@SequenceGenerator(name = "seq_vd_cp_loja_virt", sequenceName = "seq_vd_cp_loja_virt", allocationSize = 1, initialValue = 1)
public class VendaCompraLojaVirtual implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vd_cp_loja_virt")
    private Long id;

    @NotNull(message = "A pessoa compradora deve ser informado")
    @ManyToOne(targetEntity = PessoaFisica.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
    private PessoaFisica pessoa;

    @NotNull(message = "O endereço de entrega deve ser informado na nota")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "endereco_entrega_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "endereco_entrega_fk"))
    private Endereco enderecoEntrega;

    @NotNull(message = "O endereço de entrega deve ser informado na nota")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "endereco_cobranca_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "endereco_cobranca_fk"))
    private Endereco enderecoCobranca;

    @Range(min = 1, message = "O valor total da venda deve ser no minimo R$ 1.00")
    @Column(name = "valor_total_venda_loja", nullable = false)
    private BigDecimal valorTotalVendaLoja;

    @NotNull(message = "O valor total de desconto deve ser informado na nota")
    @Column(name = "valor_total_desconto_venda_loja", nullable = false)
    private BigDecimal valorTotalDescontoVendaLoja = BigDecimal.valueOf(0.00);

    @NotNull(message = "Forma de pagamento deve ser informado na nota")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "forma_pagamento_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "forma_pagamento_fk"))
    private FormaPagamento formaPagamento;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Numero nota fiscal de venda deve ser informado na nota")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_fiscal_venda_id", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "nota_fiscal_venda_fk"))
    private NotaFiscalVenda notaFiscalVenda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cupom_desconto_id", foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "cupom_desconto_fk"))
    private CupomDesonto cupomDesonto;

    @Range(min = 1, message = "O valor total do frete deve ser no minimo R$ 1.00")
    @Column(name = "valor_total_frete", nullable = false)
    private BigDecimal valorTotalFrete = BigDecimal.valueOf(0.00);

    @Range(min = 1, message = "A quantidade de dias de entrega deve ser no minimo 1 dia.")
    @Column(name = "dias_entrega", nullable = false)
    private Integer diasEntrega;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "A data da venda deve ser informado na nota")
    @Temporal(TemporalType.DATE)
    @Column(name = "data_venda", nullable = false)
    private Date dtVenda;

    @NotNull(message = "A data de entrega deve ser informado na nota")
    @Temporal(TemporalType.DATE)
    @Column(name = "data_entrega", nullable = false)
    private Date dtEntrega;


    @NotNull(message = "A empresa deve ser informada na nota")
    @ManyToOne(targetEntity = PessoaJuridica.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "vendaCompraLojaVirtual", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemVendaLoja> itemVendaLojas = new ArrayList<ItemVendaLoja>();

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = Boolean.TRUE;

    @Column(name = "codigo_etiqueta")
    private String codigoEtiqueta;

    @Column(name = "url_impressao_etiqueta")
    private String urlImpressaoEtiqueta;

    @Column(name = "servico_transportadora")
    private String servicoTransportadora;

    @Column(name = "url_rastreio")
    private String urlRastreio;

    public String getUrlRastreio() {
        return urlRastreio;
    }


    public void setUrlRastreio(String urlRastreio) {
        this.urlRastreio = urlRastreio;
    }

    public String getServicoTransportadora() {
        return servicoTransportadora;
    }

    public void setServicoTransportadora(String servicoTransportadora) {
        this.servicoTransportadora = servicoTransportadora;
    }

    public String getUrlImpressaoEtiqueta() {
        return urlImpressaoEtiqueta;
    }

    public void setUrlImpressaoEtiqueta(String urlImpressaoEtiqueta) {
        this.urlImpressaoEtiqueta = urlImpressaoEtiqueta;
    }

    public String getCodigoEtiqueta() {
        return codigoEtiqueta;
    }

    public void setCodigoEtiqueta(String codigoEtiqueta) {
        this.codigoEtiqueta = codigoEtiqueta;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<ItemVendaLoja> getItemVendaLojas() {
        return itemVendaLojas;
    }

    public void setItemVendaLojas(List<ItemVendaLoja> itemVendaLojas) {
        this.itemVendaLojas = itemVendaLojas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PessoaFisica getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaFisica pessoa) {
        this.pessoa = pessoa;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public Endereco getEnderecoCobranca() {
        return enderecoCobranca;
    }

    public void setEnderecoCobranca(Endereco enderecoCobranca) {
        this.enderecoCobranca = enderecoCobranca;
    }

    public BigDecimal getValorTotalVendaLoja() {
        return valorTotalVendaLoja;
    }

    public void setValorTotalVendaLoja(BigDecimal valorTotalVendaLoja) {
        this.valorTotalVendaLoja = valorTotalVendaLoja;
    }

    public BigDecimal getValorTotalDescontoVendaLoja() {
        return valorTotalDescontoVendaLoja;
    }

    public void setValorTotalDescontoVendaLoja(BigDecimal valorTotalDescontoVendaLoja) {
        this.valorTotalDescontoVendaLoja = valorTotalDescontoVendaLoja;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public NotaFiscalVenda getNotaFiscalVenda() {
        return notaFiscalVenda;
    }

    public void setNotaFiscalVenda(NotaFiscalVenda notaFiscalVenda) {
        this.notaFiscalVenda = notaFiscalVenda;
    }

    public CupomDesonto getCupomDesonto() {
        return cupomDesonto;
    }

    public void setCupomDesonto(CupomDesonto cupomDesonto) {
        this.cupomDesonto = cupomDesonto;
    }

    public BigDecimal getValorTotalFrete() {
        return valorTotalFrete;
    }

    public void setValorTotalFrete(BigDecimal valorTotalFrete) {
        this.valorTotalFrete = valorTotalFrete;
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

    public PessoaJuridica getEmpresa() {
        return empresa;
    }

    public void setEmpresa(PessoaJuridica empresa) {
        this.empresa = empresa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VendaCompraLojaVirtual)) return false;
        VendaCompraLojaVirtual that = (VendaCompraLojaVirtual) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}