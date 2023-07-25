package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.ContaPagar;
import br.com.franca.ShirtVirtual.repository.ContaPagarRepository;
import br.com.franca.ShirtVirtual.service.ContaPagarService;
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

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RestController
@RequestMapping("/ContasPagar")
@Api(tags = "Contas-Pagar")
@Slf4j
public class ContaPagarController {

    private ContaPagarRepository contaPagarRepository;
    private ContaPagarService contaPagarService;

    final String ERRO_DESCRICAO_CADASTRADA = "Já existe um acesso com essa descrição.!";

    @Autowired
    public ContaPagarController(ContaPagarRepository contaPagarRepository, ContaPagarService contaPagarService) {
        this.contaPagarRepository = contaPagarRepository;
        this.contaPagarService = contaPagarService;
    }

    @ApiOperation("Listagem de contas a pagar cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaPagarController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarContasPagar")
    public List<ContaPagar> listarContasPagar(){
        log.info("Inicio da listagem de contas a pagar");
        return contaPagarRepository.findAll();

    }

    @ResponseBody
    @PostMapping(value = "/cadastrarContaPagar")
    @ApiOperation("Cadastro de uma conta a pagar")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cadastro criado com sucesso", response = ContaPagarController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    public ResponseEntity <ContaPagar> salvarContaPagar(@RequestBody ContaPagar contaPagar) throws ExceptionShirtVirtual {

        log.info("Inicio do cadastro de conta a pagar");


        if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {
            log.error("Cadastro de conta a pagar encerrado com erro, empresa responsável deve ser informada.");
            throw new ExceptionShirtVirtual("Empresa responsável deve ser informada.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (contaPagar.getPessoa() == null || contaPagar.getPessoa().getId() <= 0) {
            log.error("Cadastro de conta a pagar encerrado com erro, pessoa responsável deve ser informada.");
            throw new ExceptionShirtVirtual("Pessoa responsável deve ser informada.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (contaPagar.getPessoaFornecedor() == null || contaPagar.getPessoaFornecedor().getId() <= 0) {
            log.error("Cadastro de conta a pagar encerrado com erro, fornecedor responsável deve ser informada.");
            throw new ExceptionShirtVirtual("Fornecedor responsável deve ser informada.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!contaPagarService.verificarDescricaoNoMesCorrente(contaPagar.getDtCadastro())){
            log.error("Cadastro de conta a pagar encerrado com erro, a data de cadastro não é referente ao mês corrente");
            throw new ExceptionShirtVirtual("Cadastro de conta a pagar encerrado com erro, a data de cadastro não é referente ao mês corrente", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (contaPagar.getId() == null){
            List<ContaPagar> contasPagar = contaPagarRepository.buscarContaPagarDescricao(contaPagar.getDescricao().toUpperCase().trim());
            if (!contasPagar.isEmpty()) {
                log.error("Cadastro de conta a pagar encerrado com erro, já existe uma conta com essa descrição");
                throw new ExceptionShirtVirtual("Cadastro de conta a pagar encerrado com erro, já existe uma conta com essa descrição" + contaPagar.getDescricao(), HttpStatus.NOT_FOUND);
            }
        }

        // Validar se uma descrição cadastrada  pertence ao mes corrente
        ContaPagar contaCadastrada = contaPagarRepository.save(contaPagar);
        log.info("Cadastro realizado com sucesso.");
        return new ResponseEntity<ContaPagar>(contaCadastrada, HttpStatus.CREATED);

    }

    @ApiOperation("Buscar conta a pagar por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaPagarController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarContaPagarPorId/{id}")
    public ResponseEntity<ContaPagar> buscarContaPagarPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de contas a pagar por id");
        ContaPagar contaPagar = contaPagarRepository.findById(id).orElse(null);

        if (contaPagar == null){
            log.error("Erro ao buscar conta a pagar, codigo inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id, HttpStatus.NOT_FOUND);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<ContaPagar>(contaPagar, HttpStatus.OK);
    }

    @ApiOperation("Buscar conta pagar por descricao")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaPagarController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarContaPagarPorDescricao/{descricao}")
    public ResponseEntity<List<ContaPagar>> buscarContaPagarPorDescricao(@PathVariable("descricao") String descricao) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de contas a pagar por descricao");
        List<ContaPagar> contaPagar = contaPagarRepository.buscarContaPagarDescricao(descricao.toUpperCase().trim());

        if (contaPagar == null){
            log.error("Erro ao buscar conta a pagar, descricao inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " descricao: "  +  descricao, HttpStatus.NOT_FOUND);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<ContaPagar>>(contaPagar,HttpStatus.OK);
    }

    @ApiOperation("Buscar conta pagar por id pessoa")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaPagarController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarContaPagarPorPessoa/{idPessoa}")
    public ResponseEntity<List<ContaPagar>> buscarContaPagarPorPessoa(@PathVariable("idPessoa") Long idPessoa) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de contas a pagar por codigo pessoa");
        List<ContaPagar> contaPagar = contaPagarRepository.buscarContaPagarPorPessoa(idPessoa);

        if (contaPagar == null){
            log.error("Erro ao buscar conta a pagar por pessoa, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  idPessoa, HttpStatus.NOT_FOUND);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<ContaPagar>>(contaPagar,HttpStatus.OK);
    }

    @ApiOperation("Buscar conta pagar por id fornecedor")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaPagarController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarContaPagarPorFornecedor/{idFornecedor}")
    public ResponseEntity<List<ContaPagar>> buscarContaPagarPorFornecedor(@PathVariable("idFornecedor") Long idFornecedor) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de contas a pagar por codigo fornecedor");
        List<ContaPagar> contaPagar = contaPagarRepository.buscarContaPagarPorFornecedor(idFornecedor);

        if (contaPagar == null){
            log.error("Erro ao buscar conta a pagar por fornecedor, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  idFornecedor, HttpStatus.NOT_FOUND);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<ContaPagar>>(contaPagar,HttpStatus.OK);
    }

    @ApiOperation("Buscar conta pagar por id empresa")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ContaPagarController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarContaPagarPorEmpresa/{idEmpresa}")
    public ResponseEntity<List<ContaPagar>> buscarContaPagarPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de contas a pagar por codigo fornecedor");
        List<ContaPagar> contaPagar = contaPagarRepository.buscarContaPagarPorEmpresa(idEmpresa);

        if (contaPagar == null){
            log.error("Erro ao buscar conta a pagar por empresa, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  idEmpresa, HttpStatus.NOT_FOUND);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<ContaPagar>>(contaPagar,HttpStatus.OK);
    }

    @ApiOperation("Deletar conta a pagar")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Delete com sucesso", response = ContaPagarController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarContaPagar")
    public ResponseEntity <String> deletarContaPagar(@RequestBody ContaPagar contaPagar){

        log.info("Deleção em andamento");
        contaPagarRepository.deleteById(contaPagar.getId());
        log.info("Deleção realizada com sucesso");
        return new ResponseEntity<String>("Conta a pagar removido com sucesso",HttpStatus.OK);
    }

    @ApiOperation("Deletar conta a pagar por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Delete efetuado", response = ContaPagarController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Não encontrada")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarContaPagarPorId/{id}")
    public ResponseEntity<?> deletarContaPagarPorId(@PathVariable("id") Long id) {

        log.info("Deleçao de conta a pagar por id");
        contaPagarRepository.deleteById(id);
        log.info("Deleçao de conta a pagar por id realizada com sucesso");
        return new ResponseEntity("Conta a pagar removida com sucesso",HttpStatus.OK);

    }
}