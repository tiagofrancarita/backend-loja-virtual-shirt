package br.com.franca.ShirtVirtual.enums;

public enum StatusContaReceber {

    COBRANCA ("Cobrança"),
    VENCIDA ("Vencida"),
    EM_ABERTO ("Em aberto"),
    QUITADA ("Quitada");

    private String statusContaReceber;

    StatusContaReceber(String statusContaReceber) {
        this.statusContaReceber = statusContaReceber;
    }

    public String getTipoContaReceber() {
        return statusContaReceber;
    }

    @Override
    public String toString() {
        return this.statusContaReceber;
    }
}