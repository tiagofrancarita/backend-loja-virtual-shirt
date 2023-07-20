package br.com.franca.ShirtVirtual.enums;

public enum StatusContaPagar {

    COBRANCA ("Cobrança"),
    VENCIDA ("Vencida"),
    EM_ABERTO ("Em aberto"),
    QUITADA ("Quitada");

    private String statusContaPagar;

    StatusContaPagar(String statusContaPagar) {
        this.statusContaPagar = statusContaPagar;
    }

    public String getTipoContaReceber() {
        return statusContaPagar;
    }

    @Override
    public String toString() {
        return this.statusContaPagar;
    }
}