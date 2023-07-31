package br.com.franca.ShirtVirtual.exceptions;

import br.com.franca.ShirtVirtual.service.ServiceSendEmail;
import br.com.franca.ShirtVirtual.utils.dto.ObjetoErroDto;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
@ControllerAdvice
public class ControleExcecao extends ResponseEntityExceptionHandler {

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @ExceptionHandler(ExceptionShirtVirtual.class)
    public ResponseEntity<Object> handleExceptionCustom (ExceptionShirtVirtual ex) {

        ObjetoErroDto objetoErroDTO = new ObjetoErroDto();

        objetoErroDTO.setError(ex.getMessage());
        objetoErroDTO.setCode(HttpStatus.OK.toString());

        return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.OK);
    }



    /*Captura execeçoes do projeto*/
    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {

        ObjetoErroDto objetoErroDTO = new ObjetoErroDto();

        String msg = "";

        if (ex instanceof MethodArgumentNotValidException) {

            List<ObjectError> list = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

            for (ObjectError objectError : list) {
                msg += objectError.getDefaultMessage() + "\n";
            }
        }
        else if (ex instanceof HttpMessageNotReadableException) {

            msg = "Não está sendo enviado dados para o BODY corpo da requisição";

        }else {
            msg = ex.getMessage();
        }

        objetoErroDTO.setError(msg);
        objetoErroDTO.setCode(status.value() + " ==> " + status.getReasonPhrase());

        ex.printStackTrace();

        try {

            serviceSendEmail.enviaEmailHtml("Erro na loja virtual",
                    ExceptionUtils.getStackTrace(ex),
                    "tiagofranca.ritaa@outlook.com");

        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /*Captura erro na parte de banco*/
    @ExceptionHandler({DataIntegrityViolationException.class,
            ConstraintViolationException.class, SQLException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex){

        ObjetoErroDto objetoErroDTO = new ObjetoErroDto();

        String msg = "";

        if (ex instanceof DataIntegrityViolationException) {
            msg = "Erro de integridade no banco: " +  ((DataIntegrityViolationException) ex).getCause().getCause().getMessage();
        }else
        if (ex instanceof ConstraintViolationException) {
            msg = "Erro de chave estrangeira: " + ((ConstraintViolationException) ex).getCause().getCause().getMessage();
        }else
        if (ex instanceof SQLException) {
            msg = "Erro de SQL do Banco: " + ((SQLException) ex).getCause().getCause().getMessage();
        }else {
            msg = ex.getMessage();
        }

        objetoErroDTO.setError(msg);
        objetoErroDTO.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());

        ex.printStackTrace();


        try {

            serviceSendEmail.enviaEmailHtml("Erro na loja virtual",
                    ExceptionUtils.getStackTrace(ex),
                    "tiagofranca.ritaa@outlook.com");

        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Object>(objetoErroDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
