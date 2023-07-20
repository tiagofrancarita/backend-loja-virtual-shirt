package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.repository.AcessoRepository;
import br.com.franca.ShirtVirtual.service.AcessoService;
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
@RequestMapping("/acessos")
@Api(tags = "Acessos")
@Slf4j
public class AcessoController {


    final String INICIO_LISTAR_ACESSO = "Iniciando a listagem de acesso!";
    final String LISTAGEM_COM_SUCESSO = "Iniciando a listagem de acesso!";
    final String ERRO_DESCRICAO_CADASTRADA = "Já existe um acesso com essa descrição.!";

    private AcessoService acessoService;
    private AcessoRepository acessoRepository;

    @Autowired
    public AcessoController(AcessoService acessoService,AcessoRepository acessoRepository) {
        this.acessoService = acessoService;
        this.acessoRepository = acessoRepository;
    }

    @ApiOperation("Listagem de todos os acessos cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarAcessos")
    public List<Acesso> listarAcessos(){
        log.debug("Iniciando a listagem de acessos");
        log.info("Lista extraida com sucesso");
        return acessoRepository.findAll();

    }

    @ResponseBody
    @PostMapping(value = "/cadastrarAcesso")
    @ApiOperation("Cadastro de um acesso / perfil para usuarios")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cadastro criado com sucesso", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    public ResponseEntity <Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExceptionShirtVirtual {

        log.info("Inicio do cadastro de acesso / perfi....");
        if (acesso.getId() == null){
            List<Acesso> acessos = acessoRepository.buscarAcessoDescricao(acesso.getDescAcesso().toUpperCase());

            if (!acessos.isEmpty()){
                log.error(ERRO_DESCRICAO_CADASTRADA);
                throw new ExceptionShirtVirtual(ERRO_DESCRICAO_CADASTRADA +  "Descrição:  " +  acesso.getDescAcesso());

            }
        }
        Acesso acessoSalvo = acessoService.salvarAcesso(acesso);
        log.info("Cadastro realizado com sucesso.");
        return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.CREATED);

    }

    @ApiOperation("Deletar acesso")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso deletado com sucesso", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarAcesso")
    public ResponseEntity <String> deletarAcesso(@RequestBody Acesso acesso){

        acessoRepository.deleteById(acesso.getId());

        return new ResponseEntity<String>("Acesso removido com sucesso",HttpStatus.OK);
    }

    @ApiOperation("Deletar acesso por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso excluido com sucesso", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrado para deleção")
    })
    @ResponseBody
    @DeleteMapping(value = "/deleteAcessoPorId/{id}")
    public ResponseEntity<?> deleteAcessoPorId(@PathVariable("id") Long id) {


        acessoRepository.deleteById(id);
        return new ResponseEntity("Acesso removido com sucesso",HttpStatus.OK);

    }

    @ApiOperation("Buscar acesso por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso encontrada", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarAcessoPorId/{id}")
    public ResponseEntity<Acesso> buscarAcessoPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        Acesso acesso = acessoRepository.findById(id).orElse(null);

        if (acesso == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id);

        }
        return new ResponseEntity<Acesso>(acesso,HttpStatus.OK);
    }

    @ApiOperation("Buscar acesso pela descrição")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso encontrada", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarPorDesc/{desc}")
    public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable("desc") String desc) {

        List<Acesso> acesso = acessoRepository.buscarAcessoDescricao(desc);

        return new ResponseEntity<List<Acesso>>(acesso,HttpStatus.OK);
    }
}