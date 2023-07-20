package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.service.ConsultaCnpjService;
import br.com.franca.ShirtVirtual.utils.dto.ConsultaCnpjDTO;
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
public class ConsultaCnpjController {

    private ConsultaCnpjService consultaCnpjService;

    @Autowired
    public ConsultaCnpjController(ConsultaCnpjService consultaCnpjService) {
        this.consultaCnpjService = consultaCnpjService;
    }

    @ResponseBody
    @GetMapping(value = "/consultaCnpj/{cnpj}")
    @ApiOperation("Realiza consulta externa de CNPJ - RECEITAWS")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ConsultaCnpjController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    public ResponseEntity<ConsultaCnpjDTO> consultaCep(@PathVariable("cnpj")String cnpj) {

        ConsultaCnpjDTO cnpjDTO = consultaCnpjService.consultaCnpjReceitaWS(cnpj);

        return new ResponseEntity<ConsultaCnpjDTO>(cnpjDTO, HttpStatus.OK);
    }
}