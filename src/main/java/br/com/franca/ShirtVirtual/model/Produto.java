package br.com.franca.ShirtVirtual.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Produto")
@SequenceGenerator(name = "seq_produto", sequenceName = "seq_produto", allocationSize = 1, initialValue = 1)
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_produto")
    private Long id;

    @NotNull(message = "tipo unidade do produto deve ser informado.")
    @Column(name = "tipo_unidade", nullable = false)
    private String tipoUnidade;

    @Size(min = 10, message = "Nome do produto deve ter pelo menos 10 letras")
    @NotNull(message = "Nome do produto deve ser informado.")
    @Column(name = "nome_produto", nullable = false)
    private String nome;

    @NotNull(message = "Descrição do produto deve ser informado.")
    @Column(name = "descricao_produto", columnDefinition = "text", length = 2000, nullable = false)
    private String descricaoProduto;

    @NotNull(message = "Peso do produto deve ser informado.")
    @Column(name = "peso_produto", nullable = false)
    private Double peso;

    @NotNull(message = "Largura do produto deve ser informado.")
    @Column(name = "largura_produto", nullable = false)
    private Double largura;

    @NotNull(message = "Altura do produto deve ser informado.")
    @Column(name = "altura_produto", nullable = false)
    private Double altura;

    @NotNull(message = "Profundidade do produto deve ser informado.")
    @Column(name = "profundidade_produto", nullable = false)
    private Double profundidade;

    @Range(min = 1, message = "O valor informado deve ser no minimo R$1,00")
    @NotNull(message = "Valor da venda produto deve ser informado.")
    @Column(name = "valor_total_venda", nullable = false)
    private BigDecimal valorTotalVenda = BigDecimal.ZERO;

    @Column(name = "quantidade_estoque_produto", nullable = false)
    private Integer qtdEstoque =0;

    @Column(name = "quantidade_alerta_estoque_produto", nullable = false)
    private Integer qtdAlertaEstoque = 0;

    @Column(name = "link_youtube_produto")
    private String linkYT;

    @Column(name = "alerta_quantidade_estoque", nullable = false)
    private Boolean alertaQtdEstoque = true;

    @Column(name = "quantidade_clique_produto")
    private Integer qtdClickProduto = 0;

    @NotNull(message = "A empresa responsável deve ser informada.")
    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private PessoaJuridica empresa;

    @NotNull(message = "A categoria do produto deve ser informada.")
    @ManyToOne(targetEntity = CategoriaProduto.class)
    @JoinColumn(name = "categoria_produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "categoria_produto_id_fk"))
    private CategoriaProduto categoriaProduto;

    @NotNull(message = "A marca do produto deve ser informada.")
    @ManyToOne(targetEntity = MarcaProduto.class)
    @JoinColumn(name = "marca_produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "marca_produto_id_fk"))
    private MarcaProduto marcaProduto;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "produto", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImagemProduto> imagens = new ArrayList<ImagemProduto>();

    public List<ImagemProduto> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemProduto> imagens) {
        this.imagens = imagens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoUnidade() {
        return tipoUnidade;
    }

    public void setTipoUnidade(String tipoUnidade) {
        this.tipoUnidade = tipoUnidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getLargura() {
        return largura;
    }

    public void setLargura(Double largura) {
        this.largura = largura;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(Double profundidade) {
        this.profundidade = profundidade;
    }

    public BigDecimal getValorTotalVenda() {
        return valorTotalVenda;
    }

    public void setValorTotalVenda(BigDecimal valorTotalVenda) {
        this.valorTotalVenda = valorTotalVenda;
    }

    public Integer getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(Integer qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public Integer getQtdAlertaEstoque() {
        return qtdAlertaEstoque;
    }

    public void setQtdAlertaEstoque(Integer qtdAlertaEstoque) {
        this.qtdAlertaEstoque = qtdAlertaEstoque;
    }

    public String getLinkYT() {
        return linkYT;
    }

    public void setLinkYT(String linkYT) {
        this.linkYT = linkYT;
    }

    public Boolean getAlertaQtdEstoque() {
        return alertaQtdEstoque;
    }

    public void setAlertaQtdEstoque(Boolean alertaQtdEstoque) {
        this.alertaQtdEstoque = alertaQtdEstoque;
    }

    public Integer getQtdClickProduto() {
        return qtdClickProduto;
    }

    public void setQtdClickProduto(Integer qtdClickProduto) {
        this.qtdClickProduto = qtdClickProduto;
    }

    public PessoaJuridica getEmpresa() {
        return empresa;
    }

    public void setEmpresa(PessoaJuridica empresa) {
        this.empresa = empresa;
    }

    public CategoriaProduto getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public MarcaProduto getMarcaProduto() {
        return marcaProduto;
    }

    public void setMarcaProduto(MarcaProduto marcaProduto) {
        this.marcaProduto = marcaProduto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto)) return false;
        Produto produto = (Produto) o;
        return id.equals(produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}