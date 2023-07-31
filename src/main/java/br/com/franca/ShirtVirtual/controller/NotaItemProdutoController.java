package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.NotaItemProduto;
import br.com.franca.ShirtVirtual.repository.NotaItemProdutoRepository;
import br.com.franca.ShirtVirtual.service.NotaItemProdutoService;
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
@RequestMapping("/NotaItemProduto")
@Api(tags = "Nota-Item-Produto")
@Slf4j
public class NotaItemProdutoController {

    private NotaItemProdutoRepository notaItemProdutoRepository;
    private NotaItemProdutoService notaItemProdutoService;

    @Autowired
    public NotaItemProdutoController(NotaItemProdutoRepository notaItemProdutoRepository, NotaItemProdutoService notaItemProdutoService) {
        this.notaItemProdutoRepository = notaItemProdutoRepository;
        this.notaItemProdutoService = notaItemProdutoService;
    }

    @ApiOperation("Listagem de todas Nota Item produto cadastrado")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = NotaItemProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarNotaItemProduto")
    public List<NotaItemProduto> listarNotaItemProduto(){
        log.debug("Iniciando a listagem de nota item produto");
        log.info("Lista extraida com sucesso");
        return notaItemProdutoRepository.findAll();

    }

    @ApiOperation("Buscar nota item produto fiscal por id produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = NotaItemProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaItemProdutoPorId/{idNotaItem}")
    public ResponseEntity<NotaItemProduto> buscarNotaItemProdutoPorId(@PathVariable("idNotaItem") Long idNotaItem) throws ExceptionShirtVirtual {

        NotaItemProduto notaItemProduto = notaItemProdutoRepository.findById(idNotaItem).orElse(null);

        if (notaItemProduto == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  idNotaItem);

        }
        return new ResponseEntity<NotaItemProduto>(notaItemProduto, HttpStatus.OK);
    }

    @ApiOperation("Buscar nota item produto por id produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso encontrada", response = NotaItemProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorProduto/{idProduto}")
    public ResponseEntity<List<NotaItemProduto>> buscarNotaFiscalCompraPorDesc(@PathVariable("idProduto") Long idProduto) {

        List<NotaItemProduto> notaItemProdutos = notaItemProdutoRepository.buscaNotaItemPorProduto(idProduto);

        return new ResponseEntity<List<NotaItemProduto>>(notaItemProdutos,HttpStatus.OK);
    }

    @ApiOperation("Buscar nota item produto por id produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso encontrada", response = NotaItemProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaItemPorProdutoNota/{idProduto}")
    public ResponseEntity<List<NotaItemProduto>> buscarNotaItemPorProdutoNota(@PathVariable("idProduto") Long idProduto) {

        List<NotaItemProduto> notaItemProdutos = notaItemProdutoRepository.buscaNotaItemPorProduto(idProduto);

        return new ResponseEntity<List<NotaItemProduto>>(notaItemProdutos,HttpStatus.OK);
    }

    @ApiOperation("Buscar nota item produto por id nota fiscal")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = NotaItemProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaItemPorProdutoPorNotaFiscal/{idNotaFiscal}")
    public ResponseEntity<List<NotaItemProduto>> buscarNotaItemPorProdutoPorNotaFiscal(@PathVariable("idNotaFiscal") Long idNotaFiscal) {

        List<NotaItemProduto> notaItemProdutos = notaItemProdutoRepository.buscarNotaItemPorNotaFiscal(idNotaFiscal);

        return new ResponseEntity<List<NotaItemProduto>>(notaItemProdutos,HttpStatus.OK);
    }

    @ApiOperation("Buscar nota item produto por id empresa")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = NotaItemProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaItemPorProdutoPorEmpresa/{idEmpresa}")
    public ResponseEntity<List<NotaItemProduto>> buscarNotaItemPorProdutoPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa) {

        List<NotaItemProduto> notaItemProdutos = notaItemProdutoRepository.buscarNotaItemPorEmpresa(idEmpresa);

        return new ResponseEntity<List<NotaItemProduto>>(notaItemProdutos,HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/cadastroNotaItemProduto")
    @ApiOperation("Cadastro de uma nota item produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED", response = NotaItemProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    public ResponseEntity <NotaItemProduto> cadastroNotaItemProduto(@RequestBody @Valid NotaItemProduto notaItemProduto) throws ExceptionShirtVirtual {

        log.info("Inicio do cadastro de nota item produto");

        if (notaItemProduto.getId() == null) {

            if (notaItemProduto.getProduto() == null || notaItemProduto.getProduto().getId() <= 0){
                log.error("Cadastro de nota item produto encerrado com erro, é necessário associar um produto a nota");
                throw new ExceptionShirtVirtual("Cadastro de nota item produto encerrado com erro, é necessário associar um produto a nota");
            }

            if (notaItemProduto.getEmpresa() == null || notaItemProduto.getEmpresa().getId() <= 0){
                log.error("Cadastro de nota item produto encerrado com erro, é necessário associar uma empresa responsável a nota");
                throw new ExceptionShirtVirtual("Cadastro de nota item produto encerrado com erro, é necessário associar uma empresa responsável a nota");
            }

            if (notaItemProduto.getNotaFiscalCompra() == null || notaItemProduto.getNotaFiscalCompra().getId() <= 0){
                log.error("Cadastro de nota item produto encerrado com erro, é necessário associar uma nota fiscal de compra a nota");
                throw new ExceptionShirtVirtual("Cadastro de nota item produto encerrado com erro, é necessário associar uma nota fiscal de compra a nota");
            }

            List<NotaItemProduto> notaExistente = notaItemProdutoRepository.buscarNotaItemPorProdutoNota(notaItemProduto.getProduto().getId(), notaItemProduto.getNotaFiscalCompra().getId());

            if (!notaExistente.isEmpty()){
                log.error("Cadastro de nota item produto encerrado com erro, já existe o produto associado a esta nota");
                throw new ExceptionShirtVirtual("Cadastro de nota item produto encerrado com erro, já existe o produto associado a esta nota");

            }

        }

        if (notaItemProduto.getQuantidade() <=0){
            log.error("Cadastro de nota item produto encerrado com erro, a quantidade do produto deve ser maior que 0");
            throw new ExceptionShirtVirtual("Cadastro de nota item produto encerrado com erro, a quantidade do produto deve ser maior que 0");
        }

        NotaItemProduto notaItemProdutoCadastrada = notaItemProdutoRepository.save(notaItemProduto);
        log.info("Cadastro realizado com sucesso.");
        return new ResponseEntity<NotaItemProduto>(notaItemProdutoCadastrada, HttpStatus.CREATED);

    }

    @ApiOperation("Deletar nota item produto por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = NotaItemProdutoController.class),
            @ApiResponse(code = 403, message = "ACESS DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @DeleteMapping(value = "/deleteNotaItemProduto/{id}")
    public ResponseEntity<?> deleteNotaItemProduto(@PathVariable("id") Long id) {


        notaItemProdutoRepository.deleteById(id);
        return new ResponseEntity("Nota item produto removido com sucesso",HttpStatus.OK);

    }
}