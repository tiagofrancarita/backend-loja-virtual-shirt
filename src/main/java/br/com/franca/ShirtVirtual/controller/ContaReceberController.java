package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.ContaReceber;
import br.com.franca.ShirtVirtual.repository.ContaReceberRepository;
import br.com.franca.ShirtVirtual.service.ContaReceberService;
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
@RequestMapping("/ContasReceber")
@Api(tags = "Contas-Receber")
@Slf4j
public class ContaReceberController {

    private ContaReceberRepository contaReceberRepository;
    private ContaReceberService contaReceberService;

    final String ERRO_DESCRICAO_CADASTRADA = "Já existe um acesso com essa descrição.!";

    @Autowired
    public ContaReceberController(ContaReceberRepository contaReceberRepository, ContaReceberService contaReceberService) {
        this.contaReceberRepository = contaReceberRepository;
        this.contaReceberService = contaReceberService;
    }

    @ApiOperation("Listagem de contas a receber cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaReceberController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarContasReceber")
    public List<ContaReceber> listarContasReceber(){
        log.info("Inicio da listagem de contas a receber");
        return contaReceberRepository.findAll();

    }

    @ResponseBody
    @PostMapping(value = "/cadastroContaReceber")
    @ApiOperation("Cadastro de uma conta a receber")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cadastro criado com sucesso", response = ContaReceberController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    public ResponseEntity <ContaReceber> cadastroContaReceber(@RequestBody ContaReceber contaReceber) throws ExceptionShirtVirtual {

        log.info("Inicio do cadastro de conta a receber");

        if (contaReceber.getEmpresa() == null || contaReceber.getEmpresa().getId() <= 0) {
            log.error("Cadastro de conta a receber encerrado com erro, empresa responsável deve ser informada.");
            throw new ExceptionShirtVirtual("Empresa responsável deve ser informada.");
        }

        if (contaReceber.getPessoa() == null || contaReceber.getPessoa().getId() <= 0) {
            log.error("Cadastro de conta a receber encerrado com erro, pessoa responsável deve ser informada.");
            throw new ExceptionShirtVirtual("Pessoa responsável deve ser informada.");
        }


        if (!contaReceberService.verificarDescricaoNoMesCorrente(contaReceber.getDtCadastro())){
            log.error("Cadastro de conta a receber encerrado com erro, a data de cadastro não é referente ao mês corrente");
            throw new ExceptionShirtVirtual("Cadastro de conta a receber encerrado com erro, a data de cadastro não é referente ao mês corrente");
        }

        if (contaReceber.getId() == null){
            List<ContaReceber> contasReceber = contaReceberRepository.buscarContaReceberDescricao(contaReceber.getDescricao().toUpperCase().trim());
            if (!contasReceber.isEmpty()) {
                log.error("Cadastro de conta a receber encerrado com erro, já existe uma conta com essa descrição");
                throw new ExceptionShirtVirtual("Cadastro de conta a receber encerrado com erro, já existe uma conta com essa descrição" + contaReceber.getDescricao());
            }
        }

        ContaReceber contaReceberCadastrada = contaReceberRepository.save(contaReceber);
        log.info("Cadastro realizado com sucesso.");
        return new ResponseEntity<ContaReceber>(contaReceberCadastrada, HttpStatus.CREATED);

    }

    @ApiOperation("Buscar conta a receber por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaReceberController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarContaReceberId/{id}")
    public ResponseEntity<ContaReceber> buscarContaReceberId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de contas a pagar por id");
        ContaReceber contaReceber = contaReceberRepository.findById(id).orElse(null);

        if (contaReceber == null){
            log.error("Erro ao buscar conta a receber, codigo inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<ContaReceber>(contaReceber, HttpStatus.OK);
    }

    @ApiOperation("Buscar conta receber por descricao")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaReceberController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarContaReceberPorDescricao/{descricao}")
    public ResponseEntity<List<ContaReceber>> buscarContaReceberPorDescricao(@PathVariable("descricao") String descricao) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de contas a pagar por descricao");
        List<ContaReceber> contaReceber = contaReceberRepository.buscarContaReceberDescricao(descricao.toUpperCase().trim());

        if (contaReceber == null){
            log.error("Erro ao buscar conta a receber, descricao inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " nome: "  +  descricao);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<ContaReceber>>(contaReceber,HttpStatus.OK);
    }

    @ApiOperation("Buscar conta receber por id pessoa")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaReceberController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarContaReceberPorPessoa/{idPessoa}")
    public ResponseEntity<List<ContaReceber>> buscarContaReceberPorPessoa(@PathVariable("idPessoa") Long idPessoa) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de contas a receber por pessoa");
        List<ContaReceber> contaReceber = contaReceberRepository.buscarContaReceberPorPessoa(idPessoa);

        if (contaReceber == null){
            log.error("Erro ao buscar conta a receber, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " idPessoa: "  +  idPessoa);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<ContaReceber>>(contaReceber,HttpStatus.OK);
    }

    @ApiOperation("Buscar conta receber por id empresa")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaReceberController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarContaReceberPorPessoa/{idEmpresa}")
    public ResponseEntity<List<ContaReceber>> buscarContaReceberPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de contas a receber por empresa");
        List<ContaReceber> contaReceber = contaReceberRepository.buscarContaReceberPorPessoa(idEmpresa);

        if (contaReceber == null){
            log.error("Erro ao buscar conta a receber, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " idPessoa: "  +  idEmpresa);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<ContaReceber>>(contaReceber,HttpStatus.OK);
    }

    @ApiOperation("Deletar conta a pagar")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Delete com sucesso", response = ContaReceberController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarContaReceber")
    public ResponseEntity <String> deletarContaReceber(@RequestBody ContaReceber contaReceber){

        log.info("Deleção em andamento");
        contaReceberRepository.deleteById(contaReceber.getId());
        log.info("Deleção realizada com sucesso");
        return new ResponseEntity<String>("Conta a pagar removido com sucesso",HttpStatus.OK);
    }

    @ApiOperation("Deletar conta a pagar por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Delete efetuado", response = ContaReceberController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Não encontrada")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarContaReceberPorId/{id}")
    public ResponseEntity<?> deletarContaReceberPorId(@PathVariable("id") Long id) {

        log.info("Deleçao de conta a receber por id");
        contaReceberRepository.deleteById(id);
        log.info("Deleçao de conta a receber por id realizada com sucesso");
        return new ResponseEntity("Conta a pagar removida com sucesso",HttpStatus.OK);

    }
}