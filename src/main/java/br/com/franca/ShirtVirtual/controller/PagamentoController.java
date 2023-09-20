package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.config.tokens.ApiTokenIntegracaoJuno;
import br.com.franca.ShirtVirtual.model.AccessTokenJunoAPI;
import br.com.franca.ShirtVirtual.model.BoletoJuno;
import br.com.franca.ShirtVirtual.model.VendaCompraLojaVirtual;
import br.com.franca.ShirtVirtual.repository.BoletoJunoRepository;
import br.com.franca.ShirtVirtual.repository.VendaCompraLojaVirtualRepository;
import br.com.franca.ShirtVirtual.service.HostIgnoringClient;
import br.com.franca.ShirtVirtual.service.ServiceJunoBoleto;
import br.com.franca.ShirtVirtual.service.VendaService;
import br.com.franca.ShirtVirtual.utils.ValidaCpf;
import br.com.franca.ShirtVirtual.utils.dto.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Controller
public class PagamentoController implements Serializable {

    private static final long serialVersionUID = 1L;

    private VendaCompraLojaVirtualRepository vd_Cp_Loja_virt_repository;
    private VendaService vendaService;
    private ServiceJunoBoleto serviceJunoBoleto;
    private BoletoJunoRepository boletoJunoRepository;

    @Autowired
    public PagamentoController(VendaCompraLojaVirtualRepository vd_Cp_Loja_virt_repository, VendaService vendaService, ServiceJunoBoleto serviceJunoBoleto, BoletoJunoRepository boletoJunoRepository) {
        this.vd_Cp_Loja_virt_repository = vd_Cp_Loja_virt_repository;
        this.vendaService = vendaService;
        this.serviceJunoBoleto = serviceJunoBoleto;
        this.boletoJunoRepository = boletoJunoRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "**/pagamento/{idVendaCompra}")
    public ModelAndView pagamento(@PathVariable(value = "idVendaCompra", required = false) String idVendaCompra) {


        ModelAndView modelAndView = new ModelAndView("pagamento");
        VendaCompraLojaVirtual compraLojaVirtual = vd_Cp_Loja_virt_repository.findByIdExclusao(Long.parseLong(idVendaCompra));

        if (compraLojaVirtual == null) {
            modelAndView.addObject("venda", new VendaCompraLojaVirtualDTO());
        }else {
            modelAndView.addObject("venda", vendaService.consultaVenda(compraLojaVirtual));
        }

        return modelAndView;
    }
    @RequestMapping(method = RequestMethod.POST, value = "**/finalizarCompraCartao")
    public ResponseEntity<String> finalizarCompraCartao(
            @RequestParam("cardHash") String cardHash,
            @RequestParam("cardNumber") String cardNumber,
            @RequestParam("holderName") String holderName,
            @RequestParam("securityCode") String securityCode,
            @RequestParam("expirationMonth") String expirationMonth,
            @RequestParam("expirationYear") String expirationYear,
            @RequestParam("idVendaCampo") Long idVendaCampo,
            @RequestParam("cpf") String cpf,
            @RequestParam("qtdparcela") Integer qtdparcela,
            @RequestParam("cep") String cep,
            @RequestParam("rua") String rua,
            @RequestParam("numero") String numero,
            @RequestParam("estado") String estado,
            @RequestParam("cidade") String cidade) throws Exception{

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository.
                findById(idVendaCampo).orElse(null);

        if (vendaCompraLojaVirtual == null) {
            return new ResponseEntity<String>("Código da venda não existe!", HttpStatus.OK);
        }

        String cpfLimpo =  cpf.replaceAll("\\.", "").replaceAll("\\-", "");

        if (!ValidaCpf.isCPF(cpfLimpo)) {
            return new ResponseEntity<String>("CPF informado é inválido.", HttpStatus.OK);
        }


        if (qtdparcela > 12 || qtdparcela <= 0) {
            return new ResponseEntity<String>("Quantidade de parcelar deve ser de  1 até 12.", HttpStatus.OK);
        }


        if (vendaCompraLojaVirtual.getValorTotalVendaLoja().doubleValue() <= 0) {
            return new ResponseEntity<String>("Valor da venda não pode ser Zero(0).", HttpStatus.OK);
        }

        AccessTokenJunoAPI accessTokenJunoAPI = serviceJunoBoleto.obterTokenApiJuno();

        if (accessTokenJunoAPI == null) {
            return new ResponseEntity<String>("Autorização bancária não foi encontrada.", HttpStatus.OK);
        }

        CobrancaJunoAPI cobrancaJunoAPI = new CobrancaJunoAPI();
        cobrancaJunoAPI.getCharge().setPixKey(ApiTokenIntegracaoJuno.CHAVE_BOLETO_PIX_JUNO);
        cobrancaJunoAPI.getCharge().setDescription("Pagamento da venda: " + vendaCompraLojaVirtual.getId() + " para o cliente: " + vendaCompraLojaVirtual.getPessoa().getNome());

        if (qtdparcela == 1) {
            cobrancaJunoAPI.getCharge().setAmount(vendaCompraLojaVirtual.getValorTotalVendaLoja().floatValue());
        }else {
            BigDecimal valorParcela = vendaCompraLojaVirtual.getValorTotalVendaLoja().divide(BigDecimal.valueOf(qtdparcela), RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
            cobrancaJunoAPI.getCharge().setAmount(valorParcela.floatValue());
        }

        cobrancaJunoAPI.getCharge().setInstallments(qtdparcela);

        Calendar dataVencimento = Calendar.getInstance();
        dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyy-MM-dd");
        cobrancaJunoAPI.getCharge().setDueDate(dateFormater.format(dataVencimento.getTime()));

        cobrancaJunoAPI.getCharge().setFine(BigDecimal.valueOf(1.00));
        cobrancaJunoAPI.getCharge().setInterest(BigDecimal.valueOf(1.00));
        cobrancaJunoAPI.getCharge().setMaxOverdueDays(7);
        cobrancaJunoAPI.getCharge().getPaymentTypes().add("CREDIT_CARD");

        cobrancaJunoAPI.getBilling().setName(holderName);
        cobrancaJunoAPI.getBilling().setDocument(cpfLimpo);
        cobrancaJunoAPI.getBilling().setEmail(vendaCompraLojaVirtual.getPessoa().getEmail());
        cobrancaJunoAPI.getBilling().setPhone(vendaCompraLojaVirtual.getPessoa().getTelefone());


        Client client = new HostIgnoringClient("https://api.juno.com.br/").hostIgnoringClient();
        WebResource webResource = client.resource("https://api.juno.com.br/charges");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(cobrancaJunoAPI);

        ClientResponse clientResponse = webResource
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracaoJuno.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .post(ClientResponse.class, json);

        String stringRetorno = clientResponse.getEntity(String.class);

        if (clientResponse.getStatus() != 200) {

            ErroResponseApiJuno jsonRetornoErro = objectMapper.
                    readValue(stringRetorno, new TypeReference<ErroResponseApiJuno>() {});

            return new ResponseEntity<String>(jsonRetornoErro.listaErro(), HttpStatus.OK);

        }

        clientResponse.close();

        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        BoletoGeradoApiJuno jsonRetorno = objectMapper.
                readValue(stringRetorno, new TypeReference<BoletoGeradoApiJuno>() { });

        int recorrencia = 1;

        List<BoletoJuno> boletosJuno = new ArrayList<BoletoJuno>();

        for (ConteudoBoletoJuno c : jsonRetorno.get_embedded().getCharges()) {

            BoletoJuno boletoJuno = new BoletoJuno();

            boletoJuno.setChargeICartao(c.getId());
            boletoJuno.setCheckoutUrl(c.getCheckoutUrl());
            boletoJuno.setCode(c.getCode());
            boletoJuno.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyy-MM-dd").parse(c.getDueDate())));
            boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            boletoJuno.setIdChrBoleto(c.getId());
            boletoJuno.setIdPix(c.getPix().getId());
            boletoJuno.setImageInBase64(c.getPix().getImageInBase64());
            boletoJuno.setInstallmentLink(c.getInstallmentLink());
            boletoJuno.setLink(c.getLink());
            boletoJuno.setPayloadInBase64(c.getPix().getPayloadInBase64());
            boletoJuno.setQuitado(false);
            boletoJuno.setRecorrencia(recorrencia);
            boletoJuno.setValor(new BigDecimal(c.getAmount()).setScale(2, RoundingMode.HALF_UP));
            boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

            boletoJuno = boletoJunoRepository.saveAndFlush(boletoJuno);

            boletosJuno.add(boletoJuno);
            recorrencia ++;

        }

        if (boletosJuno == null || (boletosJuno != null && boletosJuno.isEmpty())) {
            return new ResponseEntity<String>("O registro financeiro não pode ser criado para pagamento", HttpStatus.OK);
        }

        /**------------------------ REALIZANDO PAGAMENTO POR CARTÃO -------------------------**/

        BoletoJuno boletoJunoQuitacao = boletosJuno.get(0);


        PagamentoCartaoCredito pagamentoCartaoCredito = new PagamentoCartaoCredito();
        pagamentoCartaoCredito.setChargeId(boletoJunoQuitacao.getChargeICartao());
        pagamentoCartaoCredito.getCreditCardDetails().setCreditCardHash(cardHash);
        pagamentoCartaoCredito.getBilling().setEmail(vendaCompraLojaVirtual.getPessoa().getEmail());
        pagamentoCartaoCredito.getBilling().getAddress().setState(estado);
        pagamentoCartaoCredito.getBilling().getAddress().setNumber(numero);
        pagamentoCartaoCredito.getBilling().getAddress().setCity(cidade);
        pagamentoCartaoCredito.getBilling().getAddress().setStreet(rua);
        pagamentoCartaoCredito.getBilling().getAddress().setPostCode(cep.replaceAll("\\-", "").replaceAll("\\.", ""));

        Client clientCartao = new HostIgnoringClient("https://api.juno.com.br/").hostIgnoringClient();
        WebResource webResourceCartao = clientCartao.resource("https://api.juno.com.br/payments");

        ObjectMapper objectMapperCartao = new ObjectMapper();
        String jsonCartao = objectMapperCartao.writeValueAsString(pagamentoCartaoCredito);

        System.out.println("--------Envio dados pagamento cartão-----------: "+ jsonCartao);

        ClientResponse clientResponseCartao = webResourceCartao
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracaoJuno.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .post(ClientResponse.class, jsonCartao);

        String stringRetornoCartao = clientResponseCartao.getEntity(String.class);

        System.out.println("--------Retorno dados pagamento cartão-----------: "+ stringRetornoCartao);

        if (clientResponseCartao.getStatus() != 200) {

            ErroResponseApiJuno erroResponseApiJuno = objectMapper.
                    readValue(stringRetornoCartao, new TypeReference<ErroResponseApiJuno>() {} );

            for (BoletoJuno boletoJuno : boletosJuno) {
                serviceJunoBoleto.cancelarBoleto(boletoJuno.getCode());
            }

            
            return new ResponseEntity<String>(erroResponseApiJuno.listaErro(), HttpStatus.OK);
        }

        RetornoPagamentoCartaoJuno retornoPagamentoCartaoJuno = objectMapperCartao.
                readValue(stringRetornoCartao, new TypeReference<RetornoPagamentoCartaoJuno>() { });

        if (retornoPagamentoCartaoJuno.getPayments().size() <= 0) {

            for (BoletoJuno boletoJuno : boletosJuno) {
                serviceJunoBoleto.cancelarBoleto(boletoJuno.getCode());
            }

            return new ResponseEntity<String>("Nenhum pagamento foi retornado para processar.", HttpStatus.OK);
        }

        PaymentsCartaoCredito cartaoCredito = retornoPagamentoCartaoJuno.getPayments().get(0);

        if (!cartaoCredito.getStatus().equalsIgnoreCase("CONFIRMED")) {
            for (BoletoJuno boletoJuno : boletosJuno) {
                serviceJunoBoleto.cancelarBoleto(boletoJuno.getCode());
            }
        }

        if (cartaoCredito.getStatus().equalsIgnoreCase("DECLINED")) {
            return new ResponseEntity<String>("Pagamento rejeito pela análise de risco", HttpStatus.OK);
        }else  if (cartaoCredito.getStatus().equalsIgnoreCase("FAILED")) {
            return new ResponseEntity<String>("Pagamento não realizado por falha", HttpStatus.OK);
        }
        else  if (cartaoCredito.getStatus().equalsIgnoreCase("NOT_AUTHORIZED")) {
            return new ResponseEntity<String>("Pagamento não autorizado pela instituição responsável pleo cartão de crédito, no caso, a emissora do seu cartão.", HttpStatus.OK);
        }
        else  if (cartaoCredito.getStatus().equalsIgnoreCase("CUSTOMER_PAID_BACK")) {
            return new ResponseEntity<String>("Pagamento estornado a pedido do cliente.", HttpStatus.OK);
        }
        else  if (cartaoCredito.getStatus().equalsIgnoreCase("BANK_PAID_BACK")) {
            return new ResponseEntity<String>("Pagamento estornado a pedido do banco.", HttpStatus.OK);
        }
        else  if (cartaoCredito.getStatus().equalsIgnoreCase("PARTIALLY_REFUNDED")) {
            return new ResponseEntity<String>("Pagamento parcialmente estornado.", HttpStatus.OK);
        }
        else  if (cartaoCredito.getStatus().equalsIgnoreCase("CONFIRMED")) {

            for (BoletoJuno boletoJuno : boletosJuno) {
                boletoJunoRepository.quitarBoletoById(boletoJuno.getId());
            }

            vd_Cp_Loja_virt_repository.updateFinalizaVenda(vendaCompraLojaVirtual.getId());

            return new ResponseEntity<String>("sucesso", HttpStatus.OK);
        }

        return new ResponseEntity<String>("Nenhuma operação realizada!",HttpStatus.OK);
    }
}