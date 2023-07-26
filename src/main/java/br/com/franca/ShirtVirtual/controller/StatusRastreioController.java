package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.model.StatusRastreio;
import br.com.franca.ShirtVirtual.repository.StatusRastreioRepository;
import br.com.franca.ShirtVirtual.service.StatusRastreioService;
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

import java.util.List;

@Controller
@RestController
@RequestMapping("/StatusRastreio")
@Api(tags = "Status-Rastreio")
@Slf4j
public class StatusRastreioController {

    private StatusRastreioService statusRastreioService;
    private StatusRastreioRepository statusRastreioRepository;


    @Autowired
    public StatusRastreioController(StatusRastreioService statusRastreioService, StatusRastreioRepository statusRastreioRepository) {
        this.statusRastreioService = statusRastreioService;
        this.statusRastreioRepository = statusRastreioRepository;
    }

    @ApiOperation("Listagem de status rastreio")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = StatusRastreioController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarStatusRastreio")
    public List<StatusRastreio> listarStatusRastreio(){

        log.debug("Iniciando a listagem de status rastreio");
        log.info("Lista extraida com sucesso");

        return statusRastreioRepository.findAll();

    }

    @ApiOperation("Buscar status rastreio por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = StatusRastreioController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarStatusRastreioPorId/{idStatus}")
    public ResponseEntity<List<StatusRastreio>> buscarStatusRastreioPorId(@PathVariable("idStatus") Long idStatus) throws ExceptionShirtVirtual {

        List<StatusRastreio> statusRastreio = statusRastreioRepository.buscarStatusRastreioPorId(idStatus);

        if (statusRastreio == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  idStatus, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<List<StatusRastreio>>(statusRastreio, HttpStatus.OK);
    }

    @ApiOperation("Buscar status rastreio por idVenda")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = StatusRastreioController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarStatusRastreioPorIdVenda/{idVenda}")
    public ResponseEntity<List<StatusRastreio>> buscarStatusRastreioPorIdVenda(@PathVariable("idVenda") Long idVenda) throws ExceptionShirtVirtual {

        List<StatusRastreio> statusRastreio = statusRastreioRepository.buscarStatusRastreioPorVenda(idVenda);

        if (statusRastreio == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  idVenda, HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity<List<StatusRastreio>>(statusRastreio, HttpStatus.OK);
    }
}