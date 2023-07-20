package br.com.franca.ShirtVirtual.enums;

public enum FormaPagamento {

    CREDITO ("Crédito"),
    DEBITO ("Débito"),
    DINHEIRO ("Dinheiro"),
    PIX ("Pix"),
    BOLETO ("Boleto");

    private String formaPagamento;

    FormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    @Override
    public String toString() {
        return this.formaPagamento;
    }
}
