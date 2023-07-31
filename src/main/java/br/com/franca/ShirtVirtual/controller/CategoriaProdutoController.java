package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.CategoriaProduto;
import br.com.franca.ShirtVirtual.repository.CategoriaProdutoRepository;
import br.com.franca.ShirtVirtual.service.CategoriaProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RestController
@RequestMapping("/categorias")
@Api(tags = "Categoria-Produto")
public class CategoriaProdutoController {

    private CategoriaProdutoRepository categoriaProdutoRepository;
    private CategoriaProdutoService categoriaProdutoService;


    final String INICIO_LISTAR_ACESSO = "Iniciando a listagem de acesso!";
    final String LISTAGEM_COM_SUCESSO = "Iniciando a listagem de acesso!";
    final String ERRO_DESCRICAO_CADASTRADA = "Já existe um acesso com essa descrição.!";

    @Autowired
    public CategoriaProdutoController(CategoriaProdutoRepository categoriaProdutoRepository, CategoriaProdutoService categoriaProdutoService) {
        this.categoriaProdutoRepository = categoriaProdutoRepository;
        this.categoriaProdutoService = categoriaProdutoService;
    }

    @ApiOperation("Lista todas as categorias cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria encontrada", response = CategoriaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Categoria não encontrada")
    })
    @GetMapping("/listarCategorias")
    public List<CategoriaProduto> listarAcessos(){

        return categoriaProdutoRepository.findAll();

    }

    @ApiOperation("Cadastro de uma nova categoria")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Categoria criada", response = CategoriaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Cadastro não encontrada")
    })
    @ResponseBody
    @PostMapping(value = "/salvarCategoria")
    public ResponseEntity<CategoriaProduto> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionShirtVirtual {


        if (categoriaProduto == null  ) {
            throw new ExceptionShirtVirtual("Categoria não pode ser nulo.");
        }

        if(categoriaProduto.getEmpresa().getId() == null || (categoriaProduto.getEmpresa().getId()  == null)){
            throw new ExceptionShirtVirtual("Campo empresa é obrigatório.");
        }
        if (categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc().toUpperCase())) {
            throw new ExceptionShirtVirtual("Categoria já existe." + categoriaProduto.getNomeDesc());
        }
        CategoriaProduto categoriaProdutoSalva = categoriaProdutoRepository.save(categoriaProduto);

        return new ResponseEntity<CategoriaProduto>(categoriaProdutoSalva, HttpStatus.CREATED);

    }

    @ApiOperation("Deletar Categoria")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria deletada com sucesso", response = CategoriaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Categoria não encontrada para exclusão")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarCategoria")
    public ResponseEntity<String> deletarCategoria(@RequestBody CategoriaProduto categoriaProduto) {

        categoriaProdutoRepository.deleteById(categoriaProduto.getId());
        return new ResponseEntity<String>("Categoria excluída com sucesso.",HttpStatus.OK);

    }

    @ApiOperation("Buscar categoria por descricao")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria encontrada", response = CategoriaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Categoria não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscaCategoriaDesc/{desc}")
    public ResponseEntity<List<CategoriaProduto>> obterCategoriaDesc(@PathVariable("desc") String desc) {

        List <CategoriaProduto> categoriaProdutos =  categoriaProdutoRepository.buscarCategoriaDescricao(desc.toUpperCase().trim());

        return new ResponseEntity<List<CategoriaProduto>>(categoriaProdutos, HttpStatus.OK);

    }

    @ApiOperation("Buscar categoria por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria encontrada", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "categoria não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarCategoriaPorId/{id}")
    public ResponseEntity<CategoriaProduto> buscarCategoriaPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        CategoriaProduto categoriaProdutos = categoriaProdutoRepository.findById(id).orElse(null);

        if (categoriaProdutos == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id);

        }
        return new ResponseEntity<CategoriaProduto>(categoriaProdutos,HttpStatus.OK);
    }
}