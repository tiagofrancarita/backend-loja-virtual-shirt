package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.model.MarcaProduto;
import br.com.franca.ShirtVirtual.repository.AcessoRepository;
import br.com.franca.ShirtVirtual.repository.MarcaProdutoRepository;
import br.com.franca.ShirtVirtual.service.AcessoService;
import br.com.franca.ShirtVirtual.service.MarcaProdutoService;
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
@RequestMapping("/marcaProdutos")
@Api(tags = "Marca-Produto")
@Slf4j
public class MarcaProdutoController {


    final String INICIO_LISTAR_ACESSO = "Iniciando a listagem de acesso!";
    final String LISTAGEM_COM_SUCESSO = "Iniciando a listagem de acesso!";
    final String ERRO_DESCRICAO_CADASTRADA = "Já existe um acesso com essa descrição.!";

    private MarcaProdutoService marcaProdutoService;
    private MarcaProdutoRepository marcaProdutoRepository;

    @Autowired
    public MarcaProdutoController(MarcaProdutoService marcaProdutoService, MarcaProdutoRepository marcaProdutoRepository) {
        this.marcaProdutoService = marcaProdutoService;
        this.marcaProdutoRepository = marcaProdutoRepository;
    }

    @ApiOperation("Listagem de todas as marcas cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = MarcaProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarMarcaProduto")
    public List<MarcaProduto> listarMarcaProduto(){

        log.debug("Iniciando a listagem de acessos");
        log.info("Lista extraida com sucesso");
        return marcaProdutoRepository.findAll();

    }

    @ResponseBody
    @PostMapping(value = "/cadastrarMarcaProduto")
    @ApiOperation("Cadastro de uma marca")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cadastro criado com sucesso", response = MarcaProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    public ResponseEntity <MarcaProduto> cadastrarMarcaProduto(@RequestBody MarcaProduto marcaProduto) throws ExceptionShirtVirtual {

        log.info("Inicio do cadastro de marca");
        if (marcaProduto.getId() == null){
            List<MarcaProduto> marcaProdutos = marcaProdutoRepository.buscarMarcaProdutoDescricao(marcaProduto.getNomeDesc().toUpperCase());

            if (!marcaProdutos.isEmpty()){
                log.error(ERRO_DESCRICAO_CADASTRADA);
                throw new ExceptionShirtVirtual(ERRO_DESCRICAO_CADASTRADA +  "Descrição:  " +  marcaProduto.getNomeDesc(), HttpStatus.UNPROCESSABLE_ENTITY);

            }
        }
        MarcaProduto marcaCadastrada = marcaProdutoRepository.save(marcaProduto);
        log.info("Cadastro realizado com sucesso.");
        return new ResponseEntity<MarcaProduto>(marcaCadastrada, HttpStatus.CREATED);

    }

    @ApiOperation("Deletar marca produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Marca deletado com sucesso", response = MarcaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarMarcaProduto")
    public ResponseEntity <String> deletarMarcaProduto(@RequestBody MarcaProduto marcaProduto){

        marcaProdutoRepository.deleteById(marcaProduto.getId());

        return new ResponseEntity<String>("Marca removida com sucesso",HttpStatus.OK);
    }

    @ApiOperation("Deletar marca produto por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Marca excluido com sucesso", response = MarcaProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Marca não encontrado para deleção")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarMarcaProdutoPorId/{id}")
    public ResponseEntity<?> deletarMarcaProdutoPorId(@PathVariable("id") Long id) {

        marcaProdutoRepository.deleteById(id);
        return new ResponseEntity("Marca removido com sucesso",HttpStatus.OK);

    }

    @ApiOperation("Buscar marca produto por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Marca encontrada", response = MarcaProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Marca não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarMarcaProdutoPorId/{id}")
    public ResponseEntity<MarcaProduto> buscarMarcaProdutoPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        MarcaProduto marcaProduto = marcaProdutoRepository.findById(id).orElse(null);

        if (marcaProduto == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<MarcaProduto>(marcaProduto,HttpStatus.OK);
    }

    @ApiOperation("Buscar marca produto pela descrição")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Marca encontrada", response = MarcaProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Marca não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarMarcaProdutoDescricao/{descricao}")
    public ResponseEntity<List<MarcaProduto>> buscarMarcaProdutoDescricao(@PathVariable("descricao") String descricao) {

        List<MarcaProduto> marcaProdutos = marcaProdutoRepository.buscarMarcaProdutoDescricao(descricao.toUpperCase().trim());

        return new ResponseEntity<List<MarcaProduto>>(marcaProdutos,HttpStatus.OK);
    }
}