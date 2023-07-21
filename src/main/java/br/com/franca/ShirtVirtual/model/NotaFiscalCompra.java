package br.com.franca.ShirtVirtual.model;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "nota_fiscal_compra")
@SequenceGenerator(name = "seq_nota_fiscal_compra", sequenceName = "seq_nota_fiscal_compra", allocationSize = 1, initialValue = 1)
public class NotaFiscalCompra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_nota_fiscal_compra")
    private Long id;

    @NotNull(message = "O campo numero da nota fiscal deve ser preenchido.")
    @Column(name = "numero_nota_fiscal", nullable = false)
    private String numeroNota;

    @NotNull(message = "O campo serie da nota fiscal deve ser preenchido")
    @Column(name = "serie_nota_fiscal", nullable = false)
    private String serieNota;

    @NotNull(message = "A descrição da nota deve ser preenchida")
    @Column(name = "descricao_observacao_nota", nullable = false)
    private String descricaoObs;

    @Min(value = 1, message = "O valor informado deve ser no minimo R$1,00")
    @NotNull(message = "O valor total da nota deve ser preenchido")
    @Column(name = "valor_total_nota_compra", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "valor_desconto_nota_compra")
    private BigDecimal valorDesconto = BigDecimal.valueOf(0.00);

    @Min(value = 1, message = "O valor informado deve ser no minimo R$1,00")
    @NotNull(message = "O valor de ICMS deve ser preenchido")
    @Column(name = "valor_icms_nota_compra", nullable = false)
    private BigDecimal valorIcms = BigDecimal.valueOf(0.00);

    @NotNull(message = "A data da compra deve ser preenchida")
    @Column(name = "data_compra", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataCompra;

    @NotNull(message = "Deve ser associada uma pessoa juridica a nota fiscal")
    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
    private PessoaJuridica pessoa;

    @NotNull(message = "Deverá ser associado uma conta a pagar a nota fiscal")
    @ManyToOne
    @JoinColumn(name = "conta_pagar_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "conta_pagar_fk"))
    private ContaPagar contaPagar;

    @NotNull(message = "Uma empresa deverá ser associada a nota fiscal")
    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
    }

    public String getSerieNota() {
        return serieNota;
    }

    public void setSerieNota(String serieNota) {
        this.serieNota = serieNota;
    }

    public String getDescricaoObs() {
        return descricaoObs;
    }

    public void setDescricaoObs(String descricaoObs) {
        this.descricaoObs = descricaoObs;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorIcms() {
        return valorIcms;
    }

    public void setValorIcms(BigDecimal valorIcms) {
        this.valorIcms = valorIcms;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public PessoaJuridica getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaJuridica pessoa) {
        this.pessoa = pessoa;
    }

    public ContaPagar getContaPagar() {
        return contaPagar;
    }

    public void setContaPagar(ContaPagar contaPagar) {
        this.contaPagar = contaPagar;
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
        if (!(o instanceof NotaFiscalCompra)) return false;
        NotaFiscalCompra that = (NotaFiscalCompra) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}