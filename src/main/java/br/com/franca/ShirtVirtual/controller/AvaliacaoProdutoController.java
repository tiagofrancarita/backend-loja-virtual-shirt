package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.AvaliacaoProduto;
import br.com.franca.ShirtVirtual.model.CategoriaProduto;
import br.com.franca.ShirtVirtual.repository.AvaliacaoProdutoRepository;
import br.com.franca.ShirtVirtual.service.AvaliacaoProdutoService;
import br.com.franca.ShirtVirtual.utils.dto.AvaliacaoProdutoDTO;
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
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/avaliacaoProduto")
@Api(tags = "Avaliacao-Produto")
@Slf4j
public class AvaliacaoProdutoController {

    private AvaliacaoProdutoRepository avaliacaoProdutoRepository;
    private AvaliacaoProdutoService avaliacaoProdutoService;

    @Autowired
    public AvaliacaoProdutoController(AvaliacaoProdutoRepository avaliacaoProdutoRepository, AvaliacaoProdutoService avaliacaoProdutoService) {
        this.avaliacaoProdutoRepository = avaliacaoProdutoRepository;
        this.avaliacaoProdutoService = avaliacaoProdutoService;
    }

    @ApiOperation("Cadastro de avaliacao produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED", response = CategoriaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @PostMapping(value = "/cadastroAvaliacaoProduto")
    public ResponseEntity<AvaliacaoProduto> cadastroAvaliacaoProduto(@RequestBody AvaliacaoProduto avaliacaoProduto) throws ExceptionShirtVirtual {

        log.info("Inicio do cadastro de avaliacao produto");
        if (avaliacaoProduto == null  ) {
            log.error("Avaliacao produto não pode ser nulo");
            throw new ExceptionShirtVirtual("Avaliacao produto não pode ser nulo", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(avaliacaoProduto.getEmpresa().getId() == null || (avaliacaoProduto.getEmpresa() != null && avaliacaoProduto.getEmpresa().getId() <= 0)){
            log.error("Deve ser associado uma empresa a avaliacao produto");
            throw new ExceptionShirtVirtual("Campo empresa é obrigatório.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(avaliacaoProduto.getProduto().getId() == null || (avaliacaoProduto.getProduto() != null && avaliacaoProduto.getProduto().getId() <= 0)){
            log.error("Deve ser associado um produto na avaliacao produto");
            throw new ExceptionShirtVirtual("Deve ser associado um produto na avaliacao produto", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(avaliacaoProduto.getPessoa().getId() == null || (avaliacaoProduto.getPessoa() != null && avaliacaoProduto.getPessoa().getId() <= 0)){
            log.error("Deve ser associado uma pessoa na avaliacao produto");
            throw new ExceptionShirtVirtual("Deve ser associado uma pessoa na avaliacao produto", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (avaliacaoProduto.getNotaAvaliacaoProduto() == null){
            log.error("Cadastro de avaliacao produto encerrado com erro, deve ser informada uma nota");
            throw new ExceptionShirtVirtual("Cadastro de avaliacao produto encerrado com erro, deve ser informada uma nota", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        AvaliacaoProduto avaliacaoProdutoSalva = avaliacaoProdutoRepository.save(avaliacaoProduto);
        log.info("Cadastro de avaliação do produto realizada com sucesso");
        return new ResponseEntity<AvaliacaoProduto>(avaliacaoProdutoSalva, HttpStatus.CREATED);

    }

    @ApiOperation("Listagem de todas avaliações produtos cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = AvaliacaoProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarAvaliacaoProduto")
    public List<AvaliacaoProduto> listarAvaliacaoProduto(){

        log.debug("Iniciando a listagem de acessos");
        log.info("Lista extraida com sucesso");

        return avaliacaoProdutoRepository.findAll();

    }

    @ApiOperation("Deletar avaliacao produto por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "avaliacao excluido com sucesso", response = AvaliacaoProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "avaliacao não encontrado para deleção")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarAvaliacaoProdutoPorId/{id}")
    public ResponseEntity<?> deletarAvaliacaoProdutoPorId(@PathVariable("id") Long id) {


        avaliacaoProdutoRepository.deleteById(id);
        return new ResponseEntity("Avaliação de produto removido com sucesso", HttpStatus.OK);

    }

    @ApiOperation("Buscar avaliacao por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso encontrada", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarAvaliacaoPorId/{id}")
    public ResponseEntity<AvaliacaoProdutoDTO> buscarAvaliacaoPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        AvaliacaoProdutoDTO avaliacaoProdutoDTO = avaliacaoProdutoService.buscarAvaliacaoPorId(id);

        if (avaliacaoProdutoDTO == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<AvaliacaoProdutoDTO>(avaliacaoProdutoDTO,HttpStatus.OK);
    }

    @ApiOperation("Buscar avaliacao por descricao")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria encontrada", response = CategoriaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Categoria não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarAvaliacaoProdutoPorDescricao/{desc}")
    public ResponseEntity<List<AvaliacaoProduto>> buscarAvaliacaoProdutoPorDescricao(@PathVariable("desc") String desc) {

        List <AvaliacaoProduto> AvaliacaoProduto =  avaliacaoProdutoRepository.buscarAvaliacaoProdutoPorDescricao(desc.toUpperCase().trim());

        return new ResponseEntity<List<AvaliacaoProduto>>(AvaliacaoProduto, HttpStatus.OK);

    }

    @ApiOperation("Buscar avaliacao por id produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria encontrada", response = CategoriaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Categoria não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarAvaliacaoPorProduto/{idProduto}")
    public ResponseEntity<List<AvaliacaoProduto>> buscarAvaliacaoPorProduto(@PathVariable("idProduto") Long idProduto) {

        List <AvaliacaoProduto> AvaliacaoProduto =  avaliacaoProdutoRepository.buscarAvaliacaoPorProduto(idProduto);

        return new ResponseEntity<List<AvaliacaoProduto>>(AvaliacaoProduto, HttpStatus.OK);

    }

    @ApiOperation("Buscar avaliacao por id empresa")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria encontrada", response = CategoriaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Categoria não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarAvaliacaoProdutoEmpresa/{idEmpresa}")
    public ResponseEntity<List<AvaliacaoProduto>> buscarAvaliacaoProdutoEmpresa(@PathVariable("idEmpresa") Long idEmpresa) {

        List <AvaliacaoProduto> AvaliacaoProduto =  avaliacaoProdutoRepository.buscarAvaliacaoProdutoEmpresa(idEmpresa);

        return new ResponseEntity<List<AvaliacaoProduto>>(AvaliacaoProduto, HttpStatus.OK);

    }

    @ApiOperation("Buscar avaliacao por id cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria encontrada", response = CategoriaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Categoria não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarAvsaliacaoProdutoPorCliente/{idCliente}")
    public ResponseEntity<List<AvaliacaoProduto>> buscarAvsaliacaoProdutoPorCliente(@PathVariable("idCliente") Long idCliente) {

        List <AvaliacaoProduto> AvaliacaoProduto =  avaliacaoProdutoRepository.buscarAvsaliacaoProdutoPorCliente(idCliente);

        return new ResponseEntity<List<AvaliacaoProduto>>(AvaliacaoProduto, HttpStatus.OK);

    }

    @ApiOperation("Buscar avaliacao por id cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Categoria encontrada", response = CategoriaProdutoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Categoria não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarAvaliacaoPorProdutoPessoa/{idProduto}/{idCliente}")
    public ResponseEntity<List<AvaliacaoProduto>> buscarAvaliacaoPorProdutoPessoa(@PathVariable("idProduto") Long idProduto, @PathVariable("idCliente") Long idCliente) {

        List <AvaliacaoProduto> AvaliacaoProduto =  avaliacaoProdutoRepository.buscarAvaliacaoPorProdutoPessoa(idProduto,idCliente);

        return new ResponseEntity<List<AvaliacaoProduto>>(AvaliacaoProduto, HttpStatus.OK);

    }

}