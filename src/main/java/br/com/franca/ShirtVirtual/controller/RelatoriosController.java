package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.repository.AcessoRepository;
import br.com.franca.ShirtVirtual.service.RelatoriosService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RestController
@RequestMapping("/relatorios")
@Api(tags = "Relatorios")
@Slf4j
public class RelatoriosController {

    private RelatoriosService relatoriosService;
    private AcessoRepository acessoRepository;

    @Autowired
    public RelatoriosController(RelatoriosService relatoriosService, AcessoRepository acessoRepository) {
        this.relatoriosService = relatoriosService;
        this.acessoRepository = acessoRepository;
    }



    @GetMapping("/relatorios/relatorioAcesso")
    public ResponseEntity<InputStreamResource> relatorioAcesso() throws IOException {
        List<Acesso> acesos = acessoRepository.findAll();
        // Obtenha os dados dos clientes do banco de dados ou de onde estiverem armazenados.

        ByteArrayInputStream stream = relatoriosService.generateExcelReport(acesos);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=relatorio_acessos.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(stream));
    }
}