package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@Controller
@RestController
@RequestMapping("/login")
@Api(tags = "Login")
@Slf4j
public class LoginController {

    @PostMapping(value = "/{login}/{senha}")
    void login(@PathVariable("login") String login, @PathVariable("senha") String senha) throws ExceptionShirtVirtual, MessagingException, IOException {


    }

}
