package br.com.franca.ShirtVirtual.model;

import br.com.franca.ShirtVirtual.enums.StatusContaReceber;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "conta_receber")
@SequenceGenerator(name = "seq_conta_receber", sequenceName = "seq_conta_receber", allocationSize = 1, initialValue = 1)
public class ContaReceber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_conta_receber")
    private Long id;

    @ManyToOne(targetEntity = PessoaFisica.class)
    @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
    private PessoaFisica pessoa;

    @NotNull(message = "Favor informar o campo descrição da conta a receber.")
    @Column(name = "descricao_conta_receber", nullable = false)
    private String descricao;

    @NotNull(message = "Favor informar o campo status da conta a receber.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_conta_receber", nullable = false)
    private StatusContaReceber statusContaReceber;

    @NotNull(message = "Favor informar o campo data de vencimento da conta a receber.")
    @Temporal(TemporalType.DATE)
    @Column(name = "data_vencimento_conta_receber", nullable = false)
    private Date dtVencimento;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_pagamento_conta_receber")
    private Date dtPagamento;

    @NotNull(message = "Favor informar a data de cadastro da conta a receber.")
    @Column(name = "data_cadastro_conta_receber", nullable = false)
    private LocalDate dtCadastro = LocalDate.now();

    @NotNull(message = "Favor informar o campo valor total da conta a receber.")
    @Column(name = "valor_total_conta_receber", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "valor_desconto_conta_receber", nullable = false)
    private BigDecimal valorDesconto = BigDecimal.valueOf(0.00);

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusContaReceber getStatusContaReceber() {
        return statusContaReceber;
    }

    public void setStatusContaReceber(StatusContaReceber statusContaReceber) {
        this.statusContaReceber = statusContaReceber;
    }

    public Date getDtVencimento() {
        return dtVencimento;
    }

    public void setDtVencimento(Date dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public Date getDtPagamento() {
        return dtPagamento;
    }

    public void setDtPagamento(Date dtPagamento) {
        this.dtPagamento = dtPagamento;
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

    public PessoaJuridica getEmpresa() {
        return empresa;
    }

    public void setEmpresa(PessoaJuridica empresa) {
        this.empresa = empresa;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContaReceber)) return false;
        ContaReceber that = (ContaReceber) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}