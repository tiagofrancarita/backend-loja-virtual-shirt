package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Endereco;
import br.com.franca.ShirtVirtual.model.PessoaFisica;
import br.com.franca.ShirtVirtual.model.VendaCompraLojaVirtual;
import br.com.franca.ShirtVirtual.repository.EnderecoRepository;
import br.com.franca.ShirtVirtual.repository.NotaFiscalVendaRepository;
import br.com.franca.ShirtVirtual.repository.VendaCompraLojaVirtualRepository;
import br.com.franca.ShirtVirtual.service.ServiceSendEmail;
import br.com.franca.ShirtVirtual.service.VendaCompraLojaVirtualService;
import br.com.franca.ShirtVirtual.utils.dto.VendaCompraLojaVirtualDTO;
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
import java.util.List;

@Controller
@RestController
@RequestMapping("/VendaCompraLojaVirtual")
@Api(tags = "Venda-Compra-Loja-Virtual")
@Slf4j
public class VendaCompraLojaVirtualController {

    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
    private VendaCompraLojaVirtualService vendaCompraLojaVirtualService;
    private ServiceSendEmail serviceSendEmail;
    private EnderecoRepository enderecoRepository;
    private PessoaFisicaController pessoaFisicaController;
    private PessoaJuridicaController pessoaJuridicaController;
    private NotaFiscalVendaRepository notaFiscalVendaRepository;




    @Autowired
    public VendaCompraLojaVirtualController(VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository, VendaCompraLojaVirtualService vendaCompraLojaVirtualService, ServiceSendEmail serviceSendEmail, EnderecoRepository enderecoRepository, PessoaFisicaController pessoaFisicaController, PessoaJuridicaController pessoaJuridicaController, NotaFiscalVendaRepository notaFiscalVendaRepository) {
        this.vendaCompraLojaVirtualRepository = vendaCompraLojaVirtualRepository;
        this.vendaCompraLojaVirtualService = vendaCompraLojaVirtualService;
        this.serviceSendEmail = serviceSendEmail;
        this.enderecoRepository = enderecoRepository;
        this.pessoaFisicaController = pessoaFisicaController;
        this.pessoaJuridicaController = pessoaJuridicaController;
        this.notaFiscalVendaRepository = notaFiscalVendaRepository;
    }

    @ApiOperation("Listagem de todas as vendas cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarVendas")
    public List<VendaCompraLojaVirtual> listarVendas(){

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
       // pessoaJuridicaController.salvarPessoaJuridica()


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

        //Salva primeiro os dados basicos  da venda
        vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.saveAndFlush(vendaCompraLojaVirtual);

        //associa a venda salva anteriormente no banco com a nota fiscal
        vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);


        //persiste novamente a nota fiscal para amarrar a venda
        notaFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());

        VendaCompraLojaVirtualDTO vendaCompraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
        vendaCompraLojaVirtualDTO.setValorTotalVendaLoja(vendaCompraLojaVirtual.getValorTotalVendaLoja());


        // Após a venda ser salva no banco é enviado um e-mail para o cliente
            StringBuilder html = new StringBuilder();
            html.append("<h2>").append("Venda:" + vendaCompraLojaVirtual.getId())
            .append("Data da venda" + vendaCompraLojaVirtual.getDtVenda());
            html.append("<p> Tem entrega estimada para ").append(vendaCompraLojaVirtual.getDiasEntrega()).append("</p>")
            .append("Data estimada de entrega:").append(vendaCompraLojaVirtual.getDtEntrega())
            .append("Numero nota fiscal").append(vendaCompraLojaVirtual.getNotaFiscalVenda().getNumeroNotaFiscalVenda());

            serviceSendEmail.enviaEmailHtml("Venda:" + vendaCompraLojaVirtual.getId(),html.toString(),vendaCompraLojaVirtual.getPessoa().getEmail());
            log.info("Cadastro de venda realizado com sucesso, e-mail enviado para o cliente");

        return new ResponseEntity<VendaCompraLojaVirtualDTO>(vendaCompraLojaVirtualDTO, HttpStatus.CREATED);

    }
}
