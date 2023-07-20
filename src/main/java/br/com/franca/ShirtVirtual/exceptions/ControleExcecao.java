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

    private ServiceSendEmail serviceSendEmail;

    @Autowired
    public ControleExcecao(ServiceSendEmail serviceSendEmail) {
        this.serviceSendEmail = serviceSendEmail;
    }

    @ExceptionHandler(ExceptionShirtVirtual.class)
    public ResponseEntity<Object> handleCuston (ExceptionShirtVirtual ex){

        ObjetoErroDto objetoErroDto = new ObjetoErroDto();
        objetoErroDto.setError(ex.getMessage());
        objetoErroDto.setCode(HttpStatus.OK.toString());

        return new ResponseEntity<Object>(objetoErroDto, HttpStatus.OK);

    }

    @ExceptionHandler
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ObjetoErroDto objetoErroDto = new ObjetoErroDto();
        String msgErro = "";

        if (ex instanceof MethodArgumentNotValidException){
            List<ObjectError> listError = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

            for (ObjectError objectError : listError){
                msgErro += objectError.getDefaultMessage() + "\n";
            }
        }

      else if (ex instanceof HttpMessageNotReadableException){

            msgErro= "O corpo da requisição a ser enviada não pode estar vazio.";


        }
        else{
            msgErro = ex.getMessage();
        }

        objetoErroDto.setError(msgErro);
        objetoErroDto.setCode(status.value() + " ==> " +  status.getReasonPhrase()) ;

        ex.printStackTrace();

        try {

            serviceSendEmail.enviaEmailHtml("Erro inesperado", ExceptionUtils.getStackTrace(ex),"tiago.franca.dev@outlook.com");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (MessagingException e) {

            e.printStackTrace();
        }


        return new ResponseEntity<Object>(objetoErroDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConstraintViolationException.class,
                    SQLException.class})
    protected ResponseEntity<Object> handleExceptionDataIntegry(Exception ex){

        ObjetoErroDto objetoErroDto = new ObjetoErroDto();
        String msgErro = "";

        if (ex instanceof DataIntegrityViolationException){
            msgErro =((DataIntegrityViolationException)ex).getCause().getCause().getMessage();
        }else{
            msgErro = ex.getMessage();
        }

        if (ex instanceof ConstraintViolationException){
            msgErro =((ConstraintViolationException)ex).getCause().getCause().getMessage();
        }else{
            msgErro = ex.getMessage();
        }

        if (ex instanceof SQLException){
            msgErro =((SQLException)ex).getCause().getCause().getMessage();
        }else{
            msgErro = ex.getMessage();
        }

        objetoErroDto.setError(msgErro);
        objetoErroDto.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString()) ;

        ex.printStackTrace();

        try {

            serviceSendEmail.enviaEmailHtml("Erro inesperado", ExceptionUtils.getStackTrace(ex),"tiago.franca.dev@outlook.com");

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (MessagingException e) {

            e.printStackTrace();
        }

        return new ResponseEntity<Object>(objetoErroDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}