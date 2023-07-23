package br.com.franca.ShirtVirtual.utils.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class VendaCompraLojaVirtualDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal valorTotalVendaLoja;

    public BigDecimal getValorTotalVendaLoja() {
        return valorTotalVendaLoja;
    }

    public void setValorTotalVendaLoja(BigDecimal valorTotalVendaLoja) {
        this.valorTotalVendaLoja = valorTotalVendaLoja;
    }
}
