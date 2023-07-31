package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.NotaFiscalVenda;
import br.com.franca.ShirtVirtual.repository.NotaFiscalVendaRepository;
import br.com.franca.ShirtVirtual.service.NotaFiscalVendaService;
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
@RequestMapping("/NotaFiscalVenda")
@Api(tags = "Nota-Fiscal-Venda")
@Slf4j
public class NotaFiscalVendaController {

    private NotaFiscalVendaRepository notaFiscalVendaRepository;
    private NotaFiscalVendaService notaFiscalVendaService;

    @Autowired
    public NotaFiscalVendaController(NotaFiscalVendaRepository notaFiscalVendaRepository, NotaFiscalVendaService notaFiscalVendaService) {
        this.notaFiscalVendaRepository = notaFiscalVendaRepository;
        this.notaFiscalVendaService = notaFiscalVendaService;
    }

    @ApiOperation("Listagem de todas notas fiscais de venda ")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = NotaFiscalVendaController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarNotaFiscalVenda")
    public List<NotaFiscalVenda> listarNotaFiscalVenda(){

        log.debug("Iniciando a listagem de nota fiscal venda");
        log.info("Lista extraida com sucesso");

        return notaFiscalVendaRepository.findAll();
    }

    @ApiOperation("Buscar nota fiscal venda por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso encontrada", response = NotaFiscalVendaController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalVendaPorId/{id}")
    public ResponseEntity<NotaFiscalVenda> buscarNotaFiscalVendaPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        NotaFiscalVenda notaFiscalVenda = notaFiscalVendaRepository.findById(id).orElse(null);

        if (notaFiscalVenda == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id);

        }
        return new ResponseEntity<NotaFiscalVenda>(notaFiscalVenda,HttpStatus.OK);
    }

    @ApiOperation("Buscar nota fiscal de compra por id venda")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso encontrada", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorIdVenda/{idVenda}")
    public ResponseEntity<List<NotaFiscalVenda>> buscarNotaFiscalCompraPorIdVenda(@PathVariable("idVenda") Long idVenda) {

        List<NotaFiscalVenda> notaFiscalCompra = notaFiscalVendaRepository.buscarNotaFiscalPorIdVenda(idVenda);

        return new ResponseEntity<List<NotaFiscalVenda>>(notaFiscalCompra,HttpStatus.OK);
    }
}
