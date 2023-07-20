package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.service.ConsultaCepService;
import br.com.franca.ShirtVirtual.service.PessoaFisicaService;
import br.com.franca.ShirtVirtual.service.PessoaJuridicaService;
import br.com.franca.ShirtVirtual.utils.dto.CepDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/consultas")
@Api(tags = "Consultas-Externas")
public class ConsultaCepController {


    private ConsultaCepService consultaCepService;

    @Autowired
    public ConsultaCepController(ConsultaCepService consultaCepService) {
        this.consultaCepService = consultaCepService;
    }

    @ApiOperation("Consulta externa de CEP - VIACEP")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ConsultaCnpjController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")

    })
    @ResponseBody
    @GetMapping(value = "/consultaCep/{cep}")
    public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep")String cep) {

      CepDTO cepDTO = consultaCepService.consultaCEP(cep);

        return new ResponseEntity<CepDTO>(cepDTO, HttpStatus.OK);
    }
}