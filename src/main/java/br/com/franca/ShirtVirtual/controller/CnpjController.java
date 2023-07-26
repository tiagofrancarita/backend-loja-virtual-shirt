package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.utils.GeradorCnpjValido;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
@RequestMapping("/geradorCnpj")
@Api(tags = "Gerador-Cnpj")
@Slf4j
public class CnpjController {

    private GeradorCnpjValido geradorCnpjValido;

    @Autowired
    public CnpjController(GeradorCnpjValido geradorCnpjValido) {
        this.geradorCnpjValido = geradorCnpjValido;
    }

    @GetMapping("/gerarCnpj")
    public String gerarCnpj() {

        return geradorCnpjValido.cnpj(false);
    }
}

/*
GeraCpfCnpj gerador = new GeraCpfCnpj();
		String cpf = gerador.cpf(true);
		System.out.printf("CPF: %s, Valido: %s\n", cpf, gerador.isCPF(cpf));

		String cnpj = gerador.cnpj(false);
		System.out.printf("CNPJ: %s, Valido: %s\n", cnpj, gerador.isCNPJ(cnpj));

		String rg = gerador.rg(true);
		System.out.printf("RG: %s", rg);
 */