package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Produto;
import br.com.franca.ShirtVirtual.repository.ProdutoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@Controller
@RestController
@RequestMapping("/produtos")
@Api(tags = "Produtos")
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    final String INICIO_LISTAR_ACESSO = "Iniciando a listagem de acesso!";
    final String LISTAGEM_COM_SUCESSO = "Iniciando a listagem de acesso!";
    final String ERRO_DESCRICAO_CADASTRADA = "Já existe um produto com essa descrição.!";

    private ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @ApiOperation("Listagem de produtos cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarProdutos")
    public List<Produto> listarProdutos(){

        return produtoRepository.findAll();

    }

    @ApiOperation("Cadastro de produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cadastro realizado com sucesso", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @PostMapping(value = "/salvarProduto")
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionShirtVirtual {

        logger.info("Cadastro de produto iniciado.");

        if (produto.getTipoUnidade() == null || produto.getTipoUnidade().isEmpty()) {
            logger.error("Cadastro de produto encerrado com erro, tipo da unidade deve ser informada.");
            throw new ExceptionShirtVirtual("Tipo da unidade deve ser informada.");
        }

        if (produto.getNome().length() < 10) {
            logger.error("Cadastro de produto encerrado com erro, nome do produto deve ter mais que 10 caracteres.");
            throw new ExceptionShirtVirtual("Nome do produto deve ter mais que 10 caracteres.");
        }

        if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
            logger.error("Cadastro de produto encerrado com erro, empresa responsável deve ser informada.");
            throw new ExceptionShirtVirtual("Empresa responsável deve ser informada.");
        }

        if (produto.getId() == null) {
            List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getDescricaoProduto().toUpperCase(),
                    produto.getEmpresa().getId());
            if (!produtos.isEmpty()) {
                logger.error("Cadastro de produto encerrado com erro, Já existe um produto com essa descrição." + " Descrição: " +  produto.getDescricaoProduto());
                throw new ExceptionShirtVirtual("Cadastro de produto encerrado com erro, Já existe um produto com essa descrição." + " Descrição: " +  produto.getDescricaoProduto());
            }

            if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
                logger.error("Cadastro de produto encerrado com erro, categoria do produto deve ser imformada.");
                throw new ExceptionShirtVirtual("Categoria do produto deve ser informada.");
            }
            if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
                logger.error("Cadastro de produto encerrado com erro, marca do produto deve ser imformada.");
                throw new ExceptionShirtVirtual("Marca do produto deve ser informada.");
            }

            if (produto.getQtdEstoque() < 1) {
                logger.error("Cadastro de produto encerrado com erro, o produto cadastrado deve ter no minímo 1 no estoque.");
                throw new ExceptionShirtVirtual("O produto cadastrado deve ter no minímo 1 no estoque.");
            }
        }

        Produto produtoSalvo = produtoRepository.save(produto);
        logger.info("Cadastro de produto realizado com sucesso.");
        return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.CREATED);

    }

    @ApiOperation("Deletar produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Delete com sucesso", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarProduto")
    public ResponseEntity <String> deletarProduto(@RequestBody Produto produto){

        produtoRepository.deleteById(produto.getId());

        return new ResponseEntity<String>("Produto removido com sucesso",HttpStatus.OK);
    }

    @ApiOperation("Deletar produto por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Delete efetuado", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Não encontrada")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarProdutoPorId/{id}")
    public ResponseEntity<?> deletarProdutoPorId(@PathVariable("id") Long id) {

        logger.info("Deleção de produto em andamento...");
        produtoRepository.deleteById(id);
        logger.info("Produto deletado");
        return new ResponseEntity("Produto removido com sucesso",HttpStatus.OK);

    }

    @ApiOperation("Buscar produto por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarProdutoPorId/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        logger.info("Inicio de busca de produto por id...");
        Produto produto = produtoRepository.findById(id).orElse(null);

        if (produto == null){
            logger.error("Erro ao buscar produto por id, o código informado não existe ou é inválido");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id);

        }
        return new ResponseEntity<Produto>(produto,HttpStatus.OK);
    }

    @ApiOperation("Buscar produto por nome")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarProdutoPorNome/{nome}")
    public ResponseEntity<List<Produto>> buscarPorDesc(@PathVariable("nome") String nome) throws ExceptionShirtVirtual {

        logger.info("Inicio de busca de produto por nome...");
        List<Produto> produto = produtoRepository.buscarPorDesc(nome);

        if (produto == null){
            logger.error("Erro ao buscar produto por nome, o nome informado não existe ou é inválido");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " nome: "  +  nome);

        }
        return new ResponseEntity<List<Produto>>(produto,HttpStatus.OK);
    }
}