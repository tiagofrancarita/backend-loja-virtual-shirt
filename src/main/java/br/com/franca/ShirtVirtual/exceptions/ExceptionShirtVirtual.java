package br.com.franca.ShirtVirtual.exceptions;

import org.springframework.http.HttpStatus;

public class ExceptionShirtVirtual extends Exception{

    public ExceptionShirtVirtual(String msgErro, HttpStatus responseHttp) {
        super(msgErro);
    }
}
