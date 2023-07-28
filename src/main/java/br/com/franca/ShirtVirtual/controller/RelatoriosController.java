package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.model.NotaFiscalCompra;
import br.com.franca.ShirtVirtual.repository.AcessoRepository;
import br.com.franca.ShirtVirtual.repository.NotaFiscalCompraRepository;
import br.com.franca.ShirtVirtual.service.NotaFiscalCompraService;
import br.com.franca.ShirtVirtual.service.RelatoriosService;
import br.com.franca.ShirtVirtual.utils.dto.RelatorioProdCompraNotaFiscalDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
@RequestMapping("/relatorios")
@Api(tags = "Relatorios")
@Slf4j
public class RelatoriosController {

    private RelatoriosService relatoriosService;
    private AcessoRepository acessoRepository;
    private NotaFiscalCompraService notaFiscalCompraService;
    private NotaFiscalCompraRepository notaFiscalCompraRepository;


    @Autowired
    public RelatoriosController(RelatoriosService relatoriosService, AcessoRepository acessoRepository, NotaFiscalCompraService notaFiscalCompraService) {
        this.relatoriosService = relatoriosService;
        this.acessoRepository = acessoRepository;
        this.notaFiscalCompraService = notaFiscalCompraService;

    }



    @GetMapping("/relatorios/relatorioAcesso")
    public ResponseEntity<InputStreamResource> relatorioAcesso() throws IOException {

        List<Acesso> acesos = acessoRepository.findAll();

        ByteArrayInputStream stream = relatoriosService.gerarExcelRelatorioAcesso(acesos);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=relatorio_acessos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(stream));
    }

    @ResponseBody
    @GetMapping(value = "/relatorioProdCompradoNotaFiscalExcel")
    public ResponseEntity<InputStreamResource> relatorioProdCompradoNotaFiscalExcel() throws IOException {

        List<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.findAll();

        ByteArrayInputStream stream = relatoriosService.gerarExcelRelatorioNotaExcel(notaFiscalCompras);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=relatorio_acessos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(stream));
    }


    @ResponseBody
    @PostMapping(value = "/relatorios/relatorioProdCompradoNotaFiscal")
    public ResponseEntity<List<RelatorioProdCompraNotaFiscalDTO>> relatorioProdCompradoNotaFiscal
            (@Valid @RequestBody RelatorioProdCompraNotaFiscalDTO obejtoRequisicaoRelatorioProdCompraNotaFiscalDto){

        List<RelatorioProdCompraNotaFiscalDTO> retorno = new ArrayList<RelatorioProdCompraNotaFiscalDTO>();

        retorno = notaFiscalCompraService.gerarRelatorioProdCompraNota(obejtoRequisicaoRelatorioProdCompraNotaFiscalDto);

        return new ResponseEntity<List<RelatorioProdCompraNotaFiscalDTO>>(retorno, HttpStatus.OK);

    }
}