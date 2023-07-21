package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Endereco;
import br.com.franca.ShirtVirtual.model.PessoaFisica;
import br.com.franca.ShirtVirtual.model.PessoaJuridica;
import br.com.franca.ShirtVirtual.repository.EnderecoRepository;
import br.com.franca.ShirtVirtual.repository.PessoaJuridicaRepository;
import br.com.franca.ShirtVirtual.service.ConsultaCepService;
import br.com.franca.ShirtVirtual.service.PessoaJuridicaService;
import br.com.franca.ShirtVirtual.utils.ValidaCnpj;
import br.com.franca.ShirtVirtual.utils.dto.CepDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RestController
@RequestMapping("/pessoaJuridica")
@Api(tags = "Pessoa-Juridica")
public class PessoaJuridicaController {

    final String INICIO_LISTAR_ACESSO = "Iniciando a listagem de acesso!";
    final String LISTAGEM_COM_SUCESSO = "Iniciando a listagem de acesso!";
    final String ERRO_DESCRICAO_CADASTRADA = "Já existe um acesso com essa descrição.!";

    private PessoaJuridicaRepository pessoaJuridicaRepository;
    private PessoaJuridicaService pessoaJuridicaService;
    private ConsultaCepService consultaCepService;
    private EnderecoRepository enderecoRepository;

    @Autowired
    public PessoaJuridicaController(PessoaJuridicaRepository pessoaJuridicaRepository, PessoaJuridicaService pessoaJuridicaService, ConsultaCepService consultaCepService, EnderecoRepository enderecoRepository) {
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.pessoaJuridicaService = pessoaJuridicaService;
        this.consultaCepService = consultaCepService;
        this.enderecoRepository = enderecoRepository;
    }

    @ApiOperation("Listagem de todas as pessoa juridicas cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = PessoaJuridicaController.class),
            @ApiResponse(code = 403, message = "Não autoziado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarPessoaJuridica")
    public List<PessoaJuridica> listarPessoaJuridica() {

        return pessoaJuridicaRepository.findAll();

    }

    @ApiOperation("Cadastrar pessoa juridica")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cadastro efetuado", response = PessoaJuridicaController.class),
            @ApiResponse(code = 403, message = "Não autoziado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @PostMapping("/salvarPessoaJuridica")
    public ResponseEntity<PessoaJuridica> salvarPessoaJuridica(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionShirtVirtual {

        if (pessoaJuridica == null) {
            throw new ExceptionShirtVirtual("Pessoa juridica nao pode ser NULL");
        }

        if (pessoaJuridica.getTipoPessoa() == null)
            throw new ExceptionShirtVirtual("Favor informar o tipo. (Juridico ou Fornecedor)");

        if (pessoaJuridica.getId() == null && pessoaJuridicaRepository.existeCNPJ(pessoaJuridica.getCnpj()) != null) {
            throw new ExceptionShirtVirtual("Já existe CNPJ cadastrado com o número: " + pessoaJuridica.getCnpj());
        }

        if (pessoaJuridica.getId() == null && pessoaJuridicaRepository.existeInscricaoEstadual(pessoaJuridica.getInscEstadual()) != null) {
            throw new ExceptionShirtVirtual("Já existe inscrição estadual cadastrada com o número: " + pessoaJuridica.getInscEstadual());
        }

        if (pessoaJuridica.getId() == null && pessoaJuridicaRepository.existeInscricaoMunincipal(pessoaJuridica.getInscricaoMunicipal()) != null) {
            throw new ExceptionShirtVirtual("Já existe inscrição munincipal cadastrada com o número: " + pessoaJuridica.getInscricaoMunicipal());
        }

        if (!ValidaCnpj.isCNPJ(pessoaJuridica.getCnpj())) {
            throw new ExceptionShirtVirtual("Cnpj informado é inválido, favor verificar" + pessoaJuridica.getCnpj());
        }

        if (pessoaJuridica.getId() ==  null || pessoaJuridica.getId() <= 0){

            for (int p = 0 ; p < pessoaJuridica.getEnderecos().size(); p++){

                CepDTO cepDTO = consultaCepService.consultaCEP(pessoaJuridica.getEnderecos().get(p).getCep());
                pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
                pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
                pessoaJuridica.getEnderecos().get(p).setNumero(cepDTO.getComplemento());
                pessoaJuridica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
                pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());

            }
        }else{
            for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {

                Endereco enderecoTemp = (Endereco) enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();

                if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {

                    CepDTO cepDTO = consultaCepService.consultaCEP(pessoaJuridica.getEnderecos().get(p).getCep());

                    pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
                    pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
                    pessoaJuridica.getEnderecos().get(p).setNumero(cepDTO.getComplemento());
                    pessoaJuridica.getEnderecos().get(p).setLogradouro(cepDTO.getLogradouro());
                    pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
                }
            }

        }

        pessoaJuridica = pessoaJuridicaService.salvarPessoaJuridica(pessoaJuridica);

        return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
    }

    @ApiOperation("Consulta pessoa juridica por nome")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = PessoaJuridicaController.class),
            @ApiResponse(code = 403, message = "Não autoziado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/consultaPessoaJuridicaNome/{nome}")
    public ResponseEntity<List<PessoaJuridica>> consultaPessoaJuridicaNome(@PathVariable("nome") String nome){

        List<PessoaJuridica> pessoaJuridicas = pessoaJuridicaRepository.pesquisaPorNomePJ(nome.toUpperCase().trim());

        return new ResponseEntity<List<PessoaJuridica>>(pessoaJuridicas, HttpStatus.OK);
    }

    @ApiOperation("Consulta pessoa juridica pelo cnpj")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = PessoaJuridicaController.class),
            @ApiResponse(code = 403, message = "Não autoziado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/consultaPessoaJuridicaCnpj/{cnpj}")
    public ResponseEntity<List<PessoaJuridica>> consultaPessoaFisicaCpf(@PathVariable("cnpj") String cnpj){

        List<PessoaJuridica> pessoaJuridicas = pessoaJuridicaRepository.pesquisaPorCnpjPj(cnpj);

        return new ResponseEntity<List<PessoaJuridica>>(pessoaJuridicas, HttpStatus.OK);
    }

    @ApiOperation("Consulta pessoa juridica pelo e-mail")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = PessoaJuridicaController.class),
            @ApiResponse(code = 403, message = "Não autoziado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/consultaPessoaJuridicaEmail/{email}")
    public ResponseEntity<List<PessoaJuridica>> consultaPessoaFisicaEmail(@PathVariable("email") String email){

        List<PessoaJuridica> pessoaJuridicas = pessoaJuridicaRepository.pesquisaPorEmailPj(email.toUpperCase().trim());

        return new ResponseEntity<List<PessoaJuridica>>(pessoaJuridicas, HttpStatus.OK);
    }

    @ApiOperation("Consulta pessoa juridica pela categoria")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = PessoaJuridicaController.class),
            @ApiResponse(code = 403, message = "Não autoziado"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/consultaPessoaJuridicaCategoria/{categoria}")
    public ResponseEntity<List<PessoaJuridica>> consultaPessoaFisicaCategoria(@PathVariable("categoria") String categoria){

        List<PessoaJuridica> pessoaJuridicas = pessoaJuridicaRepository.pesquisaPorCategoriaPj(categoria.toUpperCase().trim());

        return new ResponseEntity<List<PessoaJuridica>>(pessoaJuridicas, HttpStatus.OK);
    }

    @ApiOperation("Deletar pessoa juridica")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Deleção efetuada", response = PessoaJuridicaController.class),
            @ApiResponse(code = 403, message = "Não autoziado"),
            @ApiResponse(code = 404, message = "Não encontrado")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarPJ")
    public ResponseEntity<String> deletarPJ(@RequestBody PessoaJuridica pessoaJuridica) {

        pessoaJuridicaRepository.deleteById(pessoaJuridica.getId());
        return new ResponseEntity<String>("Cliente excluído com sucesso.",HttpStatus.OK);

    }

    @ApiOperation("Buscar pessoa juridica por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pessoa encontrada", response = PessoaJuridicaController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Pessoa não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarPessoaJuridicaPorId/{id}")
    public ResponseEntity<PessoaJuridica> buscarPessoaJuridicaPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        PessoaJuridica pessoaJuridica = pessoaJuridicaRepository.findById(id).orElse(null);

        if (pessoaJuridica == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id);

        }
        return new ResponseEntity<PessoaJuridica>(pessoaJuridica,HttpStatus.OK);
    }

    @ApiOperation("Deletar pessoa fisica por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "pessoa excluido com sucesso", response = PessoaJuridicaController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "pessoa não encontrado para deleção")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarPessoaJuridicaPorId/{id}")
    public ResponseEntity<?> deletarPessoaJuridicaPorId(@PathVariable("id") Long id) {

        pessoaJuridicaRepository.deleteById(id);
        return new ResponseEntity("Pessoa removido com sucesso",HttpStatus.OK);

    }
}