package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.config.tokens.ApiTokenAsass;
import br.com.franca.ShirtVirtual.config.tokens.AsaasApiPagamentoStatus;
import br.com.franca.ShirtVirtual.enums.StatusContaReceber;
import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.*;
import br.com.franca.ShirtVirtual.repository.*;
import br.com.franca.ShirtVirtual.service.ServiceJunoBoleto;
import br.com.franca.ShirtVirtual.service.ServiceSendEmail;
import br.com.franca.ShirtVirtual.service.VendaCompraLojaVirtualService;
import br.com.franca.ShirtVirtual.service.VendaService;
import br.com.franca.ShirtVirtual.utils.dto.*;
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
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Controller
@RestController
@RequestMapping("/VendaCompraLojaVirtual")
@Api(tags = "Venda-Compra-Loja-Virtual")
@Slf4j
public class VendaCompraLojaVirtualController {

    @Autowired
    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;

    @Autowired
    private VendaCompraLojaVirtualService vendaCompraLojaVirtualService;

    @Autowired
    private ServiceSendEmail serviceSendEmail;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaFisicaController pessoaFisicaController;

    @Autowired
    private PessoaJuridicaController pessoaJuridicaController;

    @Autowired
    private NotaFiscalVendaRepository notaFiscalVendaRepository;

    @Autowired
    private StatusRastreioRepository statusRastreioRepository;

    @Autowired
    private ContaReceberRepository contaReceberRepository;

    @Autowired
    private ContaReceberController contaReceberController;

    @Autowired
    private ServiceJunoBoleto serviceJunoBoleto;

    @Autowired
    private VendaService vendaService;





    @ApiOperation("Gerar boleto pix -- JUNO")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/gerarBoletoPixJuno")
    public ResponseEntity<String> gerarBoletoPixJuno(@RequestBody @Valid ObjetoPostCarneJuno objetoPostCarneJuno) throws Exception {

        return new ResponseEntity<String>(serviceJunoBoleto.gerarCarneApiJuno(objetoPostCarneJuno), HttpStatus.OK);
    }

    @ApiOperation("cancelar boleto pix -- JUNO")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/cancelarBoletoPixJuno")
    public ResponseEntity<String> cancelarBoletoPixJuno(@RequestBody @Valid String code) throws Exception {

        return new ResponseEntity<String>(serviceJunoBoleto.cancelarBoleto(code), HttpStatus.OK);
    }


    @ApiOperation("Listagem de todas as vendas cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarVendas")
    public List<VendaCompraLojaVirtual> listarVendas() {

        log.debug("Iniciando a listagem de acessos");
        log.info("Lista extraida com sucesso");

        return vendaCompraLojaVirtualRepository.findAll();

    }

    @ApiOperation("Cadastro de venda")
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @PostMapping(value = "/cadastrarVenda")
    public ResponseEntity<VendaCompraLojaVirtualDTO> cadastrarVenda(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionShirtVirtual, MessagingException, IOException {

        log.info("Inicio do cadastro de uma venda");

        vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        PessoaFisica pessoaFisica = pessoaFisicaController.salvarPessoaFisica(vendaCompraLojaVirtual.getPessoa()).getBody();
        vendaCompraLojaVirtual.setPessoa(pessoaFisica);

        vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
        vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
        vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);

        vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
        vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
        vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);

        //Associa a nota fiscal a empresa
        vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

        //Associacao item produto a uma venda
        for (int item = 0; item < vendaCompraLojaVirtual.getItemVendaLojas().size(); item++) {
            vendaCompraLojaVirtual.getItemVendaLojas().get(item).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            vendaCompraLojaVirtual.getItemVendaLojas().get(item).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
        }

        //Salva primeiro os dados basicos  da venda
        vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);
        log.info("Dados basicos salvo");


        //associa a venda salva anteriormente no banco com a nota fiscal
        vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
        log.info("Status rastreio atualizado");

        //persiste novamente a nota fiscal para amarrar a venda
        notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());
        log.info("Nota fiscal amarrada a venda");

        VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
        compraLojaVirtualDTO.setValorTotalVendaLoja(vendaCompraLojaVirtual.getValorTotalVendaLoja());
        compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());

        compraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
        compraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());

        compraLojaVirtualDTO.setValorTotalDescontoVendaLoja(vendaCompraLojaVirtual.getValorTotalDescontoVendaLoja());
        compraLojaVirtualDTO.setValorTotalFrete(vendaCompraLojaVirtual.getValorTotalFrete());
        compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());

        for (ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLojas()) {

            ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
            itemVendaDTO.setQuantidade(item.getQuantidade());
            itemVendaDTO.setProduto(item.getProduto());

            compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
            log.info("item adicionado");
        }

        ContaReceber contaReceber = new ContaReceber();

        contaReceber.setDescricao("Venda da loja virtual nº: " + vendaCompraLojaVirtual.getId());
        contaReceber.setDtPagamento(Calendar.getInstance().getTime());
        contaReceber.setDtVencimento(Calendar.getInstance().getTime());
        contaReceber.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
        contaReceber.setPessoa(vendaCompraLojaVirtual.getPessoa());
        contaReceber.setStatusContaReceber(StatusContaReceber.QUITADA);
        contaReceber.setValorDesconto(vendaCompraLojaVirtual.getValorTotalDescontoVendaLoja());
        contaReceber.setValorTotal(vendaCompraLojaVirtual.getValorTotalVendaLoja());

        contaReceberController.cadastroContaReceber(contaReceber);


        /*Emil para o comprador*/
        StringBuilder msgemail = new StringBuilder();
        msgemail.append("Olá, ").append(pessoaFisica.getNome()).append("</br>");
        msgemail.append("Você realizou a compra de nº: ").append(vendaCompraLojaVirtual.getId()).append("</br>");
        msgemail.append("Na loja ").append(vendaCompraLojaVirtual.getEmpresa().getNomeFantasia());
        /*assunto, msg, destino*/
        serviceSendEmail.enviaEmailHtml("Compra Realizada", msgemail.toString(), pessoaFisica.getEmail());

        /*Email para o vendedor*/
        msgemail = new StringBuilder();
        msgemail.append("Você realizou uma venda, nº ").append(vendaCompraLojaVirtual.getId());
        serviceSendEmail.enviaEmailHtml("Venda Realizada", msgemail.toString(), vendaCompraLojaVirtual.getEmpresa().getEmail());

        return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.CREATED);

    }

    @ApiOperation("Buscar venda por id que estão ativas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/BuscaVendaAtiva/{idVenda}")
    public ResponseEntity<VendaCompraLojaVirtualDTO> BuscaVendaAtiva(@PathVariable("idVenda") Long idVenda) throws ExceptionShirtVirtual {

        log.info("Inicio de busca da venda por id...");

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.BuscaVendaAtiva(idVenda);

        if (vendaCompraLojaVirtual == null) {
            vendaCompraLojaVirtual = new VendaCompraLojaVirtual();
        }

        VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
        compraLojaVirtualDTO.setValorTotalVendaLoja(vendaCompraLojaVirtual.getValorTotalVendaLoja());
        compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());

        compraLojaVirtualDTO.setEnderecoEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
        compraLojaVirtualDTO.setEnderecoCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());

        compraLojaVirtualDTO.setValorTotalDescontoVendaLoja(vendaCompraLojaVirtual.getValorTotalDescontoVendaLoja());
        compraLojaVirtualDTO.setValorTotalFrete(vendaCompraLojaVirtual.getValorTotalFrete());
        compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());

        for (ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLojas()) {

            ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
            itemVendaDTO.setQuantidade(item.getQuantidade());
            itemVendaDTO.setProduto(item.getProduto());

            compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
        }

        return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);

    }

    @ApiOperation("Buscar venda por id cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorCliente/{idCliente}")
    public ResponseEntity<List<VendaCompraLojaVirtual>> buscarContaPagarPorPessoa(@PathVariable("idCliente") Long idCliente) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de contas a pagar por codigo pessoa");
        List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository.buscarVendaPorCliente(idCliente);

        if (vendaCompraLojaVirtuals == null) {
            log.error("Erro ao buscar venda por cliente, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: " + idCliente);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<VendaCompraLojaVirtual>>(vendaCompraLojaVirtuals, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por forma de pagamanto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorFormaPagamento/{idFormaPagamento}")
    public ResponseEntity<List<VendaCompraLojaVirtual>> buscarVendaPorFormaPagamento(@PathVariable("idFormaPagamento") Long idFormaPagamento) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de venda por forma de pagamento");
        List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository.buscarVendaPorFormaPagamento(idFormaPagamento);

        if (vendaCompraLojaVirtuals == null) {
            log.error("Erro ao buscar venda por forma de pagamanto, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: " + idFormaPagamento);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<VendaCompraLojaVirtual>>(vendaCompraLojaVirtuals, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por id de nota fiscal")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorNotaFiscalVenda/{idNotaFiscalvenda}")
    public ResponseEntity<List<VendaCompraLojaVirtual>> buscarVendaPorNotaFiscalVenda(@PathVariable("idNotaFiscalvenda") Long idNotaFiscalvenda) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de venda por forma de pagamento");
        List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository.buscarVendaPorNotaFiscalVenda(idNotaFiscalvenda);

        if (vendaCompraLojaVirtuals == null) {
            log.error("Erro ao buscar venda por forma de pagamanto, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: " + idNotaFiscalvenda);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<VendaCompraLojaVirtual>>(vendaCompraLojaVirtuals, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por id de nota fiscal")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorEmpresa/{idEmpresa}")
    public ResponseEntity<List<VendaCompraLojaVirtual>> buscarVendaPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de venda por forma de pagamento");
        List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository.buscarVendaPorEmpresa(idEmpresa);

        if (vendaCompraLojaVirtuals == null) {
            log.error("Erro ao buscar venda por forma de pagamanto, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: " + idEmpresa);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<VendaCompraLojaVirtual>>(vendaCompraLojaVirtuals, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por cpf cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorCpfCliente/{cpfCliente}")
    public ResponseEntity<List<VendaCompraLojaVirtual>> buscarVendaPorCpfCliente(@PathVariable("cpfCliente") String cpfCliente) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de venda por forma de pagamento");
        List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository.buscarVendaPorCpfCliente(cpfCliente.toUpperCase().trim());

        if (vendaCompraLojaVirtuals == null) {
            log.error("Erro ao buscar venda por cpf, cpf inválido ou inexistente");
            throw new ExceptionShirtVirtual("O cpf informado não existe. " + " id: " + cpfCliente);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<VendaCompraLojaVirtual>>(vendaCompraLojaVirtuals, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por email cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorEmailCliente/{emailCliente}")
    public ResponseEntity<List<VendaCompraLojaVirtual>> buscarVendaPorEmailCliente(@PathVariable("emailCliente") String emailCliente) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de venda por forma de pagamento");
        List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository.buscarVendaPorEmailCliente(emailCliente.toUpperCase().trim());

        if (vendaCompraLojaVirtuals == null) {
            log.error("Erro ao buscar venda por email, email inválido ou inexistente");
            throw new ExceptionShirtVirtual("Erro ao buscar venda por email, email inválido ou inexistente. " + " id: " + emailCliente);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<VendaCompraLojaVirtual>>(vendaCompraLojaVirtuals, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por email cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorNumeroNota/{numeroNota}")
    public ResponseEntity<List<VendaCompraLojaVirtual>> buscarVendaPorNumeroNota(@PathVariable("numeroNota") String numeroNota) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de venda por forma de pagamento");
        List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository.buscarVendaPorNumeroNota(numeroNota.toUpperCase().trim());

        if (vendaCompraLojaVirtuals == null) {
            log.error("Erro ao buscar venda por email, email inválido ou inexistente");
            throw new ExceptionShirtVirtual("Erro ao buscar venda por email, email inválido ou inexistente. " + " id: " + numeroNota);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<VendaCompraLojaVirtual>>(vendaCompraLojaVirtuals, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por email cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorCnpjEmpresa/{cnpjEmpresa}")
    public ResponseEntity<List<VendaCompraLojaVirtual>> buscarVendaPorCnpjEmpresa(@PathVariable("cnpjEmpresa") String cnpjEmpresa) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de venda por forma de pagamento");
        List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository.buscarVendaPorCnpjEmpresa(cnpjEmpresa.toUpperCase().trim());

        if (vendaCompraLojaVirtuals == null) {
            log.error("Erro ao buscar venda por email, email inválido ou inexistente");
            throw new ExceptionShirtVirtual("Erro ao buscar venda por email, email inválido ou inexistente. " + " id: " + cnpjEmpresa);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<VendaCompraLojaVirtual>>(vendaCompraLojaVirtuals, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por email cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorBairroEntrega/{bairroEntrega}")
    public ResponseEntity<List<VendaCompraLojaVirtual>> buscarVendaPorBairroEntrega(@PathVariable("bairroEntrega") String bairroEntrega) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de venda por forma de pagamento");
        List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository.buscarVendaPorBairroEntrega(bairroEntrega.toUpperCase().trim());

        if (vendaCompraLojaVirtuals == null) {
            log.error("Erro ao buscar venda por email, email inválido ou inexistente");
            throw new ExceptionShirtVirtual("Erro ao buscar venda por email, email inválido ou inexistente. " + " id: " + bairroEntrega);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<VendaCompraLojaVirtual>>(vendaCompraLojaVirtuals, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por nome cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscaVendaPorNomeCliente/{nomeCliente}")
    public ResponseEntity<List<VendaCompraLojaVirtual>> buscaVendaPorNomeCliente(@PathVariable("nomeCliente") String nomeCliente) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de venda por forma de pagamento");
        List<VendaCompraLojaVirtual> vendaCompraLojaVirtuals = vendaCompraLojaVirtualRepository.buscaVendaPorNomeCliente(nomeCliente.toUpperCase().trim());

        if (vendaCompraLojaVirtuals == null) {
            log.error("Erro ao buscar venda por nome, nome inválido ou inexistente");
            throw new ExceptionShirtVirtual("Erro ao buscar venda por email, email inválido ou inexistente. " + " id: " + nomeCliente);


        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<VendaCompraLojaVirtual>>(vendaCompraLojaVirtuals, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por id produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorProduto/{idProduto}")
    public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaPorProduto(@PathVariable("idProduto") Long idProduto) {

        List<VendaCompraLojaVirtual> compraLojaVirtual = vendaCompraLojaVirtualRepository.buscarVendaPorProduto(idProduto);

        if (compraLojaVirtual == null) {
            compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
        }

        List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

        for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {

            VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

            compraLojaVirtualDTO.setValorTotalVendaLoja(vcl.getValorTotalVendaLoja());
            compraLojaVirtualDTO.setPessoa(vcl.getPessoa());

            compraLojaVirtualDTO.setEnderecoEntrega(vcl.getEnderecoEntrega());
            compraLojaVirtualDTO.setEnderecoCobranca(vcl.getEnderecoCobranca());

            compraLojaVirtualDTO.setValorTotalDescontoVendaLoja(vcl.getValorTotalDescontoVendaLoja());
            compraLojaVirtualDTO.setFormaPagamento(vcl.getFormaPagamento());
            compraLojaVirtualDTO.setDtVenda(vcl.getDtVenda());
            compraLojaVirtualDTO.setDtEntrega(vcl.getDtEntrega());
            compraLojaVirtualDTO.setDiasEntrega(vcl.getDiasEntrega());
            compraLojaVirtualDTO.setValorTotalFrete(vcl.getValorTotalFrete());
            compraLojaVirtualDTO.setId(vcl.getId());

            for (ItemVendaLoja item : vcl.getItemVendaLojas()) {

                ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
                itemVendaDTO.setQuantidade(item.getQuantidade());
                itemVendaDTO.setProduto(item.getProduto());

                compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
            }

            compraLojaVirtualDTOList.add(compraLojaVirtualDTO);

        }

        return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por data ")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarVendaPorData/{dataInicio}/{dataFim}")
    public ResponseEntity<List<VendaCompraLojaVirtualDTO>> buscarVendaPorData(@PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim") String dataFim) throws ParseException, ParseException {

        List<VendaCompraLojaVirtual> compraLojaVirtual = null;


        SimpleDateFormat dataFormatada = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = dataFormatada.parse(dataInicio);
        Date d2 = dataFormatada.parse(dataFim);

        compraLojaVirtual = vendaCompraLojaVirtualRepository.buscarVendaPorData(d1, d2);

        if (compraLojaVirtual == null) {
            compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
        }

        List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

        for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {

            VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

            compraLojaVirtualDTO.setValorTotalVendaLoja(vcl.getValorTotalVendaLoja());
            compraLojaVirtualDTO.setPessoa(vcl.getPessoa());

            compraLojaVirtualDTO.setEnderecoEntrega(vcl.getEnderecoEntrega());
            compraLojaVirtualDTO.setEnderecoCobranca(vcl.getEnderecoCobranca());

            compraLojaVirtualDTO.setValorTotalDescontoVendaLoja(vcl.getValorTotalDescontoVendaLoja());
            compraLojaVirtualDTO.setFormaPagamento(vcl.getFormaPagamento());
            compraLojaVirtualDTO.setDtVenda(vcl.getDtVenda());
            compraLojaVirtualDTO.setDtEntrega(vcl.getDtEntrega());
            compraLojaVirtualDTO.setDiasEntrega(vcl.getDiasEntrega());
            compraLojaVirtualDTO.setValorTotalFrete(vcl.getValorTotalFrete());
            compraLojaVirtualDTO.setId(vcl.getId());

            for (ItemVendaLoja item : vcl.getItemVendaLojas()) {

                ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
                itemVendaDTO.setQuantidade(item.getQuantidade());
                itemVendaDTO.setProduto(item.getProduto());

                compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
            }

            compraLojaVirtualDTOList.add(compraLojaVirtualDTO);

        }

        return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por id produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/consultaVendaDinamica/{valor}/{tipoconsulta}")
    public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaDinamica(@PathVariable("valor") String valor, @PathVariable("tipoconsulta") String tipoconsulta) {

        List<VendaCompraLojaVirtual> compraLojaVirtual = null;

        if (tipoconsulta.equalsIgnoreCase("POR-ID-PRODUTO")) {
            compraLojaVirtual = vendaCompraLojaVirtualRepository.buscarVendaPorProduto(Long.parseLong(valor));
        } else if (tipoconsulta.equalsIgnoreCase("POR-NOME-PRODUTO")) {
            compraLojaVirtual = vendaCompraLojaVirtualRepository.buscarVendaPorNomeProduto(valor.toUpperCase().trim());
        } else if (tipoconsulta.equalsIgnoreCase("POR-NOME-CLIENTE")) {
            compraLojaVirtual = vendaCompraLojaVirtualRepository.buscaVendaPorNomeCliente(valor.toUpperCase().trim());
        } else if (tipoconsulta.equalsIgnoreCase("POR-ENDERECO-COBRANCA")) {
            compraLojaVirtual = vendaCompraLojaVirtualRepository.buscaVendaPorNomeCliente(valor.toUpperCase().trim());
        } else if (tipoconsulta.equalsIgnoreCase("POR-ENDERECO-ENTREGA")) {
            compraLojaVirtual = vendaCompraLojaVirtualRepository.buscaVendaPorEnderecoCobranca(valor.toUpperCase().trim());
        } else if (tipoconsulta.equalsIgnoreCase("POR-ENDERECO-COBRANCA")) {
            compraLojaVirtual = vendaCompraLojaVirtualRepository.buscaVendaPorEnderecoEntrega(valor.toUpperCase().trim());
        } else if (tipoconsulta.equalsIgnoreCase("POR-ESTADO-COBRANCA")) {
            compraLojaVirtual = vendaCompraLojaVirtualRepository.buscaVendaPorEstadoEntrega(valor.toUpperCase().trim());
        } else if (tipoconsulta.equalsIgnoreCase("POR-ESTADO-COBRANCA")) {
            compraLojaVirtual = vendaCompraLojaVirtualRepository.buscaVendaPorEstadoCobranca(valor.toUpperCase().trim());
        }

        if (compraLojaVirtual == null) {
            compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
        }

        List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();

        for (VendaCompraLojaVirtual vcl : compraLojaVirtual) {

            VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

            compraLojaVirtualDTO.setValorTotalVendaLoja(vcl.getValorTotalVendaLoja());
            compraLojaVirtualDTO.setPessoa(vcl.getPessoa());

            compraLojaVirtualDTO.setEnderecoEntrega(vcl.getEnderecoEntrega());
            compraLojaVirtualDTO.setEnderecoCobranca(vcl.getEnderecoCobranca());

            compraLojaVirtualDTO.setValorTotalDescontoVendaLoja(vcl.getValorTotalDescontoVendaLoja());
            compraLojaVirtualDTO.setFormaPagamento(vcl.getFormaPagamento());
            compraLojaVirtualDTO.setDtVenda(vcl.getDtVenda());
            compraLojaVirtualDTO.setDtEntrega(vcl.getDtEntrega());
            compraLojaVirtualDTO.setDiasEntrega(vcl.getDiasEntrega());
            compraLojaVirtualDTO.setValorTotalFrete(vcl.getValorTotalFrete());
            compraLojaVirtualDTO.setId(vcl.getId());

            for (ItemVendaLoja item : vcl.getItemVendaLojas()) {

                ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
                itemVendaDTO.setQuantidade(item.getQuantidade());
                itemVendaDTO.setProduto(item.getProduto());

                compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
            }

            compraLojaVirtualDTOList.add(compraLojaVirtualDTO);

        }

        return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);
    }

    @ApiOperation("Deletar venda")
    @ApiResponses({
            @ApiResponse(code = 200, message = "a deletado com sucesso", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarVendaCompraLojaVirtual")
    public ResponseEntity<String> deletarAcesso(@RequestBody VendaCompraLojaVirtual vendaCompraLojaVirtual) {

        vendaCompraLojaVirtualRepository.deleteById(vendaCompraLojaVirtual.getId());

        return new ResponseEntity<String>("Acesso removido com sucesso", HttpStatus.OK);
    }

    @ApiOperation("Deletar venda por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso excluido com sucesso", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrado para deleção")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarVendaCompraLojaVirtualPorId/{idVenda}")
    public ResponseEntity<?> deletarVendaCompraLojaVirtualPorId(@PathVariable("idVenda") Long idVenda) {


        vendaCompraLojaVirtualRepository.deleteById(idVenda);
        return new ResponseEntity("Venda removido com sucesso", HttpStatus.OK);

    }

    @ApiOperation("Deletar venda total por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso excluido com sucesso", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrado para deleção")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarVendaTotal/{idVenda}")
    public ResponseEntity<?> deletarVendaTotal(@PathVariable("idVenda") Long idVenda) {


        vendaCompraLojaVirtualService.deletarVendaTotal(idVenda);
        return new ResponseEntity("Venda removido com sucesso", HttpStatus.OK);

    }

    @ApiOperation("Deletar venda total por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso excluido com sucesso", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrado para deleção")
    })
    @ResponseBody
    @PutMapping(value = "/cancelarNotaFiscal/{idVenda}")
    public ResponseEntity<?> cancelarNotaFiscal(@PathVariable("idVenda") Long idVenda) {


        vendaCompraLojaVirtualService.cancelarNotaFiscal(idVenda);
        return new ResponseEntity("Venda cancelada com sucesso", HttpStatus.OK);

    }


    @ApiOperation("Consulta venda por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = " não encontrado ")
    })
    @ResponseBody
    @PutMapping(value = "/consultaVenda/{idVenda}")
    public ResponseEntity<?> consultaVenda(@PathVariable("idVenda") Long idVenda) {

        log.info("Inicio de consulta da venda por id...");

        VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualRepository.findByIdExclusao(idVenda);

        if (compraLojaVirtual == null) {
            compraLojaVirtual = new VendaCompraLojaVirtual();
        }

        VendaCompraLojaVirtualDTO compraLojaVirtualDTO = vendaService.consultaVenda(compraLojaVirtual);

        return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);


    }
}

