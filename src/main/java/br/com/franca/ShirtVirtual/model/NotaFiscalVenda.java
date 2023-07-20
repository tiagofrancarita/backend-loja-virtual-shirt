package br.com.franca.ShirtVirtual.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "nota_fiscal_venda")
@SequenceGenerator(name = "seq_nota_fiscal_venda", sequenceName = "seq_nota_fiscal_venda", allocationSize = 1, initialValue = 1)
public class NotaFiscalVenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_nota_fiscal_venda")
    private Long id;

    @Column(name = "numero_nota_fiscal_venda", nullable = false)
    private String numeroNotaFiscalVenda;

    @Column(name = "serie_nota_fiscal_venda", nullable = false)
    private String serieNotaFiscalVenda;

    @Column(name = "tipo_nota_fiscal_venda", nullable = false)
    private String tipoNotaFiscalVenda;

    @Column(name = "xml_nota_fiscal_venda", columnDefinition = "text", nullable = false)
    private String xmlNotaFiscalVenda;

    @Column(name = "pdf_nota_fiscal_venda", columnDefinition = "text", nullable = false)
    private String pdfNotaFiscalVenda;

    @OneToOne
    @JoinColumn(name = "vd_cp_loja_virt_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "vd_cp_loja_virt_fk"))
    private VendaCompraLojaVirtual vendaCompraLojaVirtual;

    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_id_fk"))
    private Pessoa empresa;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroNotaFiscalVenda() {
        return numeroNotaFiscalVenda;
    }

    public void setNumeroNotaFiscalVenda(String numeroNotaFiscalVenda) {
        this.numeroNotaFiscalVenda = numeroNotaFiscalVenda;
    }

    public String getSerieNotaFiscalVenda() {
        return serieNotaFiscalVenda;
    }

    public void setSerieNotaFiscalVenda(String serieNotaFiscalVenda) {
        this.serieNotaFiscalVenda = serieNotaFiscalVenda;
    }

    public String getTipoNotaFiscalVenda() {
        return tipoNotaFiscalVenda;
    }

    public void setTipoNotaFiscalVenda(String tipoNotaFiscalVenda) {
        this.tipoNotaFiscalVenda = tipoNotaFiscalVenda;
    }

    public String getXmlNotaFiscalVenda() {
        return xmlNotaFiscalVenda;
    }

    public void setXmlNotaFiscalVenda(String xmlNotaFiscalVenda) {
        this.xmlNotaFiscalVenda = xmlNotaFiscalVenda;
    }

    public String getPdfNotaFiscalVenda() {
        return pdfNotaFiscalVenda;
    }

    public void setPdfNotaFiscalVenda(String pdfNotaFiscalVenda) {
        this.pdfNotaFiscalVenda = pdfNotaFiscalVenda;
    }

    public VendaCompraLojaVirtual getVendaCompraLojaVirtual() {
        return vendaCompraLojaVirtual;
    }

    public void setVendaCompraLojaVirtual(VendaCompraLojaVirtual vendaCompraLojaVirtual) {
        this.vendaCompraLojaVirtual = vendaCompraLojaVirtual;
    }

    public Pessoa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Pessoa empresa) {
        this.empresa = empresa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotaFiscalVenda)) return false;
        NotaFiscalVenda that = (NotaFiscalVenda) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}