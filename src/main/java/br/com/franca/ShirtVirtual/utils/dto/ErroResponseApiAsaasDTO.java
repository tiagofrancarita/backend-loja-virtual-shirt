package br.com.franca.ShirtVirtual.utils.dto;

import java.util.ArrayList;
import java.util.List;

public class ErroResponseApiAsaasDTO {

    private List<ObjetoErroResponseApiAsaasDTO> errors = new ArrayList<ObjetoErroResponseApiAsaasDTO>();


    public void setErrors(List<ObjetoErroResponseApiAsaasDTO> errors) {
        this.errors = errors;
    }

    public List<ObjetoErroResponseApiAsaasDTO> getErrors() {
        return errors;
    }

    public String listaErros() {

        StringBuilder builder = new StringBuilder();

        for (ObjetoErroResponseApiAsaasDTO error : errors) {
            builder.append(error.getDescription()).append(" - Code: ").append(error.getCode()).append("\n");
        }

        return builder.toString();
    }

}
