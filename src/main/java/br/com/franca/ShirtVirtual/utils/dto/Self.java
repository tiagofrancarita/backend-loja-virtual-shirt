package br.com.franca.ShirtVirtual.utils.dto;

import java.io.Serializable;

public class Self implements Serializable{


    private static final long serialVersionUID = 1L;

    private String href;

    public void setHref(String href) {
        this.href = href;
    }

    public String getHref() {
        return href;
    }

}