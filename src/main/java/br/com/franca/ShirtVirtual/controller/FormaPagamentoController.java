package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.model.FormaPagamento;
import br.com.franca.ShirtVirtual.repository.FormaPagamentoRepository;
import br.com.franca.ShirtVirtual.service.FormaPagamentoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RestController
@RequestMapping("/formaPagamento")
@Api(tags = "Forma-Pagamento")
@Slf4j
public class FormaPagamentoController {

    private FormaPagamentoRepository formaPagamentoRepository;
    private FormaPagamentoService formaPagamentoService;

    @Autowired
    public FormaPagamentoController(FormaPagamentoRepository formaPagamentoRepository, FormaPagamentoService formaPagamentoService) {
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.formaPagamentoService = formaPagamentoService;
    }

    @ApiOperation("Listagem de todas as formas de pagamento cadastrada")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = FormaPagamentoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarFormaPagamento")
    public List<FormaPagamento> listarFormaPagamento(){

        log.debug("Iniciando a listagem de acessos");
        log.info("Lista extraida com sucesso");
        return formaPagamentoRepository.findAll();

    }

    @ResponseBody
    @PostMapping(value = "/cadastrarFormaPagamento")
    @ApiOperation("Cadastro de uma forma de pagamento")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cadastro criado com sucesso", response = FormaPagamentoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    public ResponseEntity<FormaPagamento> cadastrarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento) throws ExceptionShirtVirtual {

        log.info("Inicio do cadastro de forma de pagamento");
        if (formaPagamento.getId() == null){
            List<FormaPagamento> formaPagamentos = formaPagamentoRepository.buscarFormaPagamentoDescricao(formaPagamento.getDescFormaPagamento().toUpperCase());

            if (!formaPagamentos.isEmpty()){
                log.error("Forma de pagamento ja cadastrada");
                throw new ExceptionShirtVirtual("Forma de pagamento ja cadastrada" +  "Descrição:  " +  formaPagamento.getDescFormaPagamento(), HttpStatus.UNPROCESSABLE_ENTITY);

            }
        }
        FormaPagamento formaPagamentoSalva = formaPagamentoRepository.save(formaPagamento);
        log.info("Cadastro realizado com sucesso.");
        return new ResponseEntity<FormaPagamento>(formaPagamentoSalva, HttpStatus.CREATED);

    }
}