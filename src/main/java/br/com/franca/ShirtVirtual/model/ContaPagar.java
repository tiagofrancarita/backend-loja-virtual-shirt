package br.com.franca.ShirtVirtual.model;

import br.com.franca.ShirtVirtual.enums.StatusContaPagar;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "conta_pagar")
@SequenceGenerator(name = "seq_conta_pagar", sequenceName = "seq_conta_pagar", allocationSize = 1, initialValue = 1)
public class ContaPagar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_conta_pagar")
    private Long id;

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
    private PessoaJuridica pessoa;

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "pessoa_forn_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_forn_fk"))
    private PessoaJuridica pessoaFornecedor;

    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

    @NotNull(message = "Favor informar o campo descrição da conta a pagar.")
    @Column(name = "descricao_conta_pagar", nullable = false)
    private String descricao;

    @NotNull(message = "Favor informar o campo status da conta a pagar")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_conta_pagar", nullable = false)
    private StatusContaPagar statusContaPagar;

    @NotNull(message = "Favor informar a data de cadastro da conta a pagar.")
    @Column(name = "data_cadastro_conta_pagar", nullable = false)
    private LocalDate dtCadastro = LocalDate.now();

    @NotNull(message = "Favor informar a data de vencimento da conta a pagar.")
    @Column(name = "data_vencimento_conta_pagar", nullable = false)
    private Date dtVencimento;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_pagamento_conta_pagar")
    private Date dtPagamento;

    @NotNull(message = "Favor informar o valor total da conta a pagar.")
    @Column(name = "valor_total_conta_pagar", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "valor_desconto_conta_pagar", nullable = false)
    private BigDecimal valorDesconto = BigDecimal.valueOf(0.00);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PessoaJuridica getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaJuridica pessoa) {
        this.pessoa = pessoa;
    }

    public PessoaJuridica getPessoaFornecedor() {
        return pessoaFornecedor;
    }

    public void setPessoaFornecedor(PessoaJuridica pessoaFornecedor) {
        this.pessoaFornecedor = pessoaFornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusContaPagar getStatusContaPagar() {
        return statusContaPagar;
    }

    public void setStatusContaPagar(StatusContaPagar statusContaPagar) {
        this.statusContaPagar = statusContaPagar;
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
        if (!(o instanceof ContaPagar)) return false;
        ContaPagar that = (ContaPagar) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}