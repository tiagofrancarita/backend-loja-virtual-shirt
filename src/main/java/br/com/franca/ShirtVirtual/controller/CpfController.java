package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.utils.GeradorCpfValido;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/geradorCpf")
@Api(tags = "Gerador-Cpf")
@Slf4j
public class CpfController {


    private GeradorCpfValido geradorCpfValido;

    @Autowired
    public CpfController(GeradorCpfValido geradorCpfValido) {
        this.geradorCpfValido = geradorCpfValido;
    }

    @GetMapping("/gerarCpf")
    public String generateCpf() {
        return geradorCpfValido.generateRandomCpf();
    }
}
