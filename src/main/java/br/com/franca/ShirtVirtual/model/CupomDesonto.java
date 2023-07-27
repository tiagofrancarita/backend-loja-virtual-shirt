package br.com.franca.ShirtVirtual.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "cup_desc")
@SequenceGenerator(name = "seq_cup_desc", sequenceName = "seq_cup_desc", allocationSize = 1, initialValue = 1)
public class CupomDesonto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cup_desc")
    private Long id;

    @NotEmpty(message = "Descrição do cupom de desconto é obrigatoria.")
    @Column(name = "codigo_desconto", nullable = false)
    private String codDesc;

    @Column(name = "valor_real_desconto")
    private BigDecimal valorRealDesc;

    @Column(name = "valor_percentual_desconto")
    private BigDecimal valorPercentDesc;

    @NotEmpty(message = "Data de validade do cupom de desconto é obrigatoria.")
    @Temporal(TemporalType.DATE)
    @Column(name = "data_validade_cupom", nullable = false)
    private Date dtValidadeCupom;

    @NotEmpty(message = "Empresa responsavel do cupom de desconto é obrigatoria.")
    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodDesc() {
        return codDesc;
    }

    public void setCodDesc(String codDesc) {
        this.codDesc = codDesc;
    }

    public BigDecimal getValorRealDesc() {
        return valorRealDesc;
    }

    public void setValorRealDesc(BigDecimal valorRealDesc) {
        this.valorRealDesc = valorRealDesc;
    }

    public BigDecimal getValorPercentDesc() {
        return valorPercentDesc;
    }

    public void setValorPercentDesc(BigDecimal valorPercentDesc) {
        this.valorPercentDesc = valorPercentDesc;
    }

    public Date getDtValidadeCupom() {
        return dtValidadeCupom;
    }

    public void setDtValidadeCupom(Date dtValidadeCupom) {
        this.dtValidadeCupom = dtValidadeCupom;
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
        if (!(o instanceof CupomDesonto)) return false;
        CupomDesonto that = (CupomDesonto) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}