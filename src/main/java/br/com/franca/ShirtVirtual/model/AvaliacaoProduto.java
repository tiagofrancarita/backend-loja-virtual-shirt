package br.com.franca.ShirtVirtual.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "avaliacao_produto")
@SequenceGenerator(name = "seq_avaliacao_produto", sequenceName = "seq_avaliacao_produto", allocationSize = 1, initialValue = 1)
public class AvaliacaoProduto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_avaliacao_produto")
    private Long id;

    @NotNull(message = "Descrição de avaliação deve ser preenchida")
    @Column(name = "descricao_avaliacao_produto", nullable = false)
    private String descricaoAvaliacaoProduto;

    @Range(min = 0, max = 10, message = "Informe a nota do produto, minimo: 0 maximo: 10")
    @NotNull(message = "Nota de avaliação deve ser preenchida")
    @Column(name = "nota_avaliacao_produto", nullable = false)
    private Integer notaAvaliacaoProduto;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(targetEntity = PessoaFisica.class)
    @JoinColumn(name = "pessoa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "pessoa_fk"))
    private PessoaFisica pessoa;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "produto_fk"))
    private Produto produto;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(targetEntity = PessoaJuridica.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

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

    public PessoaFisica getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaFisica pessoa) {
        this.pessoa = pessoa;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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
        if (!(o instanceof AvaliacaoProduto)) return false;
        AvaliacaoProduto that = (AvaliacaoProduto) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}