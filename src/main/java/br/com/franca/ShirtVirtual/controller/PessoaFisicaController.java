package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.enums.TipoPessoa;
import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.PessoaFisica;
import br.com.franca.ShirtVirtual.repository.PessoaFisicaRepository;
import br.com.franca.ShirtVirtual.service.PessoaFisicaService;
import br.com.franca.ShirtVirtual.utils.ValidaCpf;
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
import java.util.Locale;

@Controller
@RestController
@RequestMapping("/pessoaFisica")
@Api(tags = "Pessoa-Fisica")
public class PessoaFisicaController {

    private PessoaFisicaRepository pessoaFisicaRepository;
    private PessoaFisicaService pessoaFisicaService;

    @Autowired
    public PessoaFisicaController(PessoaFisicaRepository pessoaFisicaRepository, PessoaFisicaService pessoaFisicaService) {
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaFisicaService = pessoaFisicaService;
    }

    @ApiOperation("Listagem de todas as pessoas fisicas cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = PessoaFisicaController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarPessoaFisica")
    public List<PessoaFisica> listarPessoaFisica() {

        return pessoaFisicaRepository.findAll();

    }

    @ApiOperation("Cadastro de pessoa fisica")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cadastro realizado", response = PessoaFisicaController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Erro ao cadastrar")
    })
    @ResponseBody
    @PostMapping("/salvarPessoaFisica")
    public ResponseEntity<PessoaFisica> salvarPessoaFisica(@RequestBody PessoaFisica pessoaFisica) throws ExceptionShirtVirtual {

        if (pessoaFisica == null) {
            throw new ExceptionShirtVirtual("Pessoa fisica nao pode ser NULL");
        }

        if (pessoaFisica.getTipoPessoa() == null){
            pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
        }

        if (pessoaFisica.getId() == null && pessoaFisicaRepository.existeCPF(pessoaFisica.getCpf()) != null) {
            throw new ExceptionShirtVirtual("Já existe CPF cadastrado com o número: " + pessoaFisica.getCpf());
        }

        if (pessoaFisica.getId() == null && pessoaFisicaRepository.existeEmail(pessoaFisica.getEmail()) != null) {
            throw new ExceptionShirtVirtual("Já existe email cadastrado: " + pessoaFisica.getEmail());
        }

        if (!ValidaCpf.isCPF(pessoaFisica.getCpf())) {
            throw new ExceptionShirtVirtual("Cnpj informado é inválido, favor verificar" + pessoaFisica.getCpf());
        }

        pessoaFisica = pessoaFisicaService.salvarPessoaFisica(pessoaFisica);

        return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
    }

    @ApiOperation("Consulta pessoa fisica por nome")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = PessoaFisicaController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/consultaPessoaFisicaNome/{nome}")
    public ResponseEntity<List<PessoaFisica>> consultaPessoaFisicaNome(@PathVariable("nome") String nome){

        List<PessoaFisica> pessoaFisicas = pessoaFisicaRepository.pesquisaPorNomePF(nome.toUpperCase().trim());

        return new ResponseEntity<List<PessoaFisica>>(pessoaFisicas, HttpStatus.OK);
    }

    @ApiOperation("Consulta pessoa fisica por cpf")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = PessoaFisicaController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/consultaPessoaFisicaCpf/{cpf}")
    public ResponseEntity<List<PessoaFisica>> consultaPessoaFisicaCpf(@PathVariable("cpf") String cpf){

        List<PessoaFisica> pessoaFisicas = pessoaFisicaRepository.pesquisaPorCPFPF(cpf);

        return new ResponseEntity<List<PessoaFisica>>(pessoaFisicas, HttpStatus.OK);
    }

    @ApiOperation("Consulta pessoa fisica por e-mail")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = PessoaFisicaController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/consultaPessoaFisicaEmail/{email}")
    public ResponseEntity<List<PessoaFisica>> consultaPessoaFisicaEmail(@PathVariable("email") String email){

        List<PessoaFisica> pessoaFisicas = pessoaFisicaRepository.pesquisaPorEmailPF(email.toUpperCase().trim());

        return new ResponseEntity<List<PessoaFisica>>(pessoaFisicas, HttpStatus.OK);
    }

    @ApiOperation("Deletar pessoa fisica")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deletado com sucesso", response = PessoaFisicaController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Pessoa não encontrada para deleção")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarPF")
    public ResponseEntity<String> deletarPF(@RequestBody PessoaFisica pessoaFisica) {// Recebe o json e converte para objeto

        pessoaFisicaRepository.deleteById(pessoaFisica.getId());
        return new ResponseEntity<String>("Cliente excluído com sucesso.",HttpStatus.OK);

    }

    @ApiOperation("Buscar pessoa fisica por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pessoa encontrada", response = PessoaFisicaController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Pessoa não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarPessoaFisicaPorId/{id}")
    public ResponseEntity<PessoaFisica> buscarPessoaFisicaPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        PessoaFisica pessoaFisica = pessoaFisicaRepository.findById(id).orElse(null);

        if (pessoaFisica == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id);

        }
        return new ResponseEntity<PessoaFisica>(pessoaFisica,HttpStatus.OK);
    }

    @ApiOperation("Deletar pessoa fisica por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "pessoa excluido com sucesso", response = PessoaFisicaController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "pessoa não encontrado para deleção")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarPessoaFisicaPorId/{id}")
    public ResponseEntity<?> deletarPessoaFisicaPorId(@PathVariable("id") Long id) {

        pessoaFisicaRepository.deleteById(id);
        return new ResponseEntity("Pessoa removido com sucesso",HttpStatus.OK);

    }
}