package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.CupomDesonto;
import br.com.franca.ShirtVirtual.repository.CupomDescontoRepository;
import br.com.franca.ShirtVirtual.service.CupomDescontoService;
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
@RequestMapping("/CupomDesconto")
@Api(tags = "Cupom-Desconto")
@Slf4j
public class CupomDescontoController {

    private static final String ERRO_DESCRICAO_CADASTRADA = "Descrição já cadastrada";

    private CupomDescontoService cupomDescontoService;
    private CupomDescontoRepository cupomDescontoRepository;


    @Autowired
    public CupomDescontoController(CupomDescontoService cupomDescontoService, CupomDescontoRepository cupomDescontoRepository) {
        this.cupomDescontoService = cupomDescontoService;
        this.cupomDescontoRepository = cupomDescontoRepository;
    }


    @ApiOperation("Listagem de todos os cupons de desconto cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = CupomDescontoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarCupomDesconto")
    public List<CupomDesonto> listarCupomDesconto(){

        log.info("Iniciando a listagem de acessos");

        try {
            log.info("Listagem extraida com sucesso");
            return cupomDescontoRepository.findAll();

        }catch (Exception e){
            e.printStackTrace();
            log.error("Erro ao extrair listagem de cupom desconto" + e.getMessage());
            return null;
        }

    }

    @ResponseBody
    @PostMapping(value = "/cadastrarCupomDesconto")
    @ApiOperation("Cadastro Cupom Desconto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cadastro criado com sucesso", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    public ResponseEntity<CupomDesonto> cadastrarCupomDesconto(@RequestBody CupomDesonto cupomDesonto) throws ExceptionShirtVirtual {

        log.info("Inicio do cadastro de cupom de desconto");
        if (cupomDesonto.getId() == null){
            List<CupomDesonto> cuponsDesconto = cupomDescontoRepository.buscarAcessoDescricao(cupomDesonto.getCodDesc().toUpperCase());

            if (!cuponsDesconto.isEmpty()){
                log.error(ERRO_DESCRICAO_CADASTRADA);
                throw new ExceptionShirtVirtual(ERRO_DESCRICAO_CADASTRADA +  "Descrição:  " +  cupomDesonto.getCodDesc());

            }
        }
        CupomDesonto cupomSalvo = cupomDescontoRepository.save(cupomDesonto);
        log.info("Cadastro realizado com sucesso.");
        return new ResponseEntity<CupomDesonto>(cupomSalvo, HttpStatus.CREATED);

    }

    @ApiOperation("Buscar cupom pela descrição")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Sucesso", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarCupomPorDescricao/{codDesc}")
    public ResponseEntity<List<CupomDesonto>> buscarCupomPorDescricao(@PathVariable("codDesc") String codDesc) {

        List<CupomDesonto> cupomDesontos = cupomDescontoRepository.buscarAcessoDescricao(codDesc.toUpperCase().trim());

        return new ResponseEntity<List<CupomDesonto>>(cupomDesontos,HttpStatus.OK);
    }
}