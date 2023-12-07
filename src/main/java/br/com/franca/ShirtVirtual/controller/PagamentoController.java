package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.config.tokens.ApiTokenIntegracaoJuno;
import br.com.franca.ShirtVirtual.config.tokens.AsaasApiPagamentoStatus;
import br.com.franca.ShirtVirtual.model.AccessTokenJunoAPI;
import br.com.franca.ShirtVirtual.model.BoletoJuno;
import br.com.franca.ShirtVirtual.model.PessoaFisica;
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
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private VendaCompraLojaVirtualRepository vd_Cp_Loja_virt_repository;

    @Autowired
    private VendaService vendaService;

    @Autowired
    private ServiceJunoBoleto serviceJunoBoleto;

    @Autowired
    private BoletoJunoRepository boletoJunoRepository;

    private Logger logger = LoggerFactory.getLogger(PagamentoController.class);

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
    public ResponseEntity<String> finalizarCompraCartaoAsaas(@RequestParam("cardNumber") String cardNumber, @RequestParam("holderName") String holderName,
            @RequestParam("securityCode") String securityCode, @RequestParam("expirationMonth") String expirationMonth,@RequestParam("expirationYear") String expirationYear,
            @RequestParam("idVendaCampo") Long idVendaCampo, @RequestParam("cpf") String cpf, @RequestParam("qtdparcela") Integer qtdparcela,
            @RequestParam("cep") String cep, @RequestParam("rua") String rua, @RequestParam("numero") String numero, @RequestParam("estado") String estado,
            @RequestParam("cidade") String cidade) throws Exception {

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository.
                findById(idVendaCampo).orElse(null);

        if (vendaCompraLojaVirtual == null) {
            logger.error("--------Codigo de venda não existe-----------");
            return new ResponseEntity<String>("Código da venda não existe!", HttpStatus.OK);

        }

        String cpfLimpo = cpf.replaceAll("\\.", "").replaceAll("\\-", "");

        if (!ValidaCpf.isCPF(cpfLimpo)) {
            logger.error("--------CPF inválido-----------");
            return new ResponseEntity<String>("CPF informado é inválido.", HttpStatus.OK);
        }


        if (qtdparcela > 12 || qtdparcela <= 0) {
            logger.error("--------Quantidade de parcelas infromada é inválida, favor informar parcelas entre 1 e 12-----------");
            return new ResponseEntity<String>("Quantidade de parcelar deve ser de  1 até 12.", HttpStatus.OK);
        }


        if (vendaCompraLojaVirtual.getValorTotalVendaLoja().doubleValue() <= 0) {
            logger.error("-------- Valor da venda não pode ser zero. -----------");
            return new ResponseEntity<String>("Valor da venda não pode ser Zero(0).", HttpStatus.OK);
        }

        List<BoletoJuno> cobrancas = boletoJunoRepository.cobrancaVendaCompra(idVendaCampo);

        for (BoletoJuno boletoJuno : cobrancas) {
            boletoJunoRepository.deleteById(boletoJuno.getId());
            boletoJunoRepository.flush();
            logger.info("--------Cobrança excluida com sucesso-----------");


        }

        logger.info("--------Iniciando geração de cobrança pagamento por cartão de crédito-----------");

        ObjetoPostCarneJuno objetoPostCarneJuno = new ObjetoPostCarneJuno();
        objetoPostCarneJuno.setPayerCpfCnpj(cpfLimpo);
        objetoPostCarneJuno.setPayerName(holderName);
        objetoPostCarneJuno.setPayerPhone(vendaCompraLojaVirtual.getPessoa().getTelefone());

        CobrancaApiAsaasCartaoDTO cobrancaApiAsaasCartaoDTO = new CobrancaApiAsaasCartaoDTO();
        cobrancaApiAsaasCartaoDTO.setCustomer(serviceJunoBoleto.buscaClientePessoaApiAsaas(objetoPostCarneJuno));
        cobrancaApiAsaasCartaoDTO.setBillingType(AsaasApiPagamentoStatus.CREDIT_CARD);
        cobrancaApiAsaasCartaoDTO.setDescription("Pagamento da venda: " + vendaCompraLojaVirtual.getId() + " para o cliente: " + vendaCompraLojaVirtual.getPessoa().getNome() + "Forma de pagamento" + AsaasApiPagamentoStatus.CREDIT_CARD);

        if (qtdparcela == 1) {
            cobrancaApiAsaasCartaoDTO.setInstallmentValue(vendaCompraLojaVirtual.getValorTotalVendaLoja().floatValue());
        } else {
            BigDecimal valorParcela = vendaCompraLojaVirtual.getValorTotalVendaLoja()
                    .divide(BigDecimal.valueOf(qtdparcela), RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
            cobrancaApiAsaasCartaoDTO.setInstallmentValue(valorParcela.floatValue());
        }

        cobrancaApiAsaasCartaoDTO.setInstallmentCount(qtdparcela);
        cobrancaApiAsaasCartaoDTO.setDueDate(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

        logger.info("--------Iniciando input de dados de cartão de credito-----------");

        CartaoCreditoApiAsaasDTO cartaoCreditoApiAsaasDTO = new CartaoCreditoApiAsaasDTO();
        cartaoCreditoApiAsaasDTO.setCcv(securityCode);
        cartaoCreditoApiAsaasDTO.setExpiryMonth(expirationMonth);
        cartaoCreditoApiAsaasDTO.setExpiryYear(expirationYear);
        cartaoCreditoApiAsaasDTO.setHolderName(holderName);
        cartaoCreditoApiAsaasDTO.setNumber(cardNumber);

        cobrancaApiAsaasCartaoDTO.setCreditCard(cartaoCreditoApiAsaasDTO);

        CartaoCreditoAsaasHolderInfoDTO cartaoCreditoAsaasHolderInfoDTO = new CartaoCreditoAsaasHolderInfoDTO();
        PessoaFisica pessoaFisica = vendaCompraLojaVirtual.getPessoa();
        cartaoCreditoAsaasHolderInfoDTO.setName(pessoaFisica.getNome());
        cartaoCreditoAsaasHolderInfoDTO.setCpfCnpj(pessoaFisica.getCpf());
        cartaoCreditoAsaasHolderInfoDTO.setPostalCode(cep);
        cartaoCreditoAsaasHolderInfoDTO.setAddressNumber(numero);
        cartaoCreditoAsaasHolderInfoDTO.setAddressComplement(rua);
        cartaoCreditoAsaasHolderInfoDTO.setMobilePhone(vendaCompraLojaVirtual.getPessoa().getTelefone());
        cartaoCreditoAsaasHolderInfoDTO.setEmail(vendaCompraLojaVirtual.getPessoa().getEmail());
        cartaoCreditoAsaasHolderInfoDTO.setPhone(vendaCompraLojaVirtual.getPessoa().getTelefone());

        cobrancaApiAsaasCartaoDTO.setCreditCardHolderInfo(cartaoCreditoAsaasHolderInfoDTO);

        String jsonFinalizaCompraCartaoCredito = new ObjectMapper().writeValueAsString(cobrancaApiAsaasCartaoDTO);

        logger.info("--------Iniciando chamada de API para finalizar compra por cartão de credito-----------");

        Client clientFinalizaCompraCartaoCredito = new HostIgnoringClient(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX).hostIgnoringClient();
        WebResource webResourceFinalizaCompraCartaoCredito = clientFinalizaCompraCartaoCredito.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "payments");

        ClientResponse clientResponseFinalizaCompraCartaoCredito = webResourceFinalizaCompraCartaoCredito
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("access_token", AsaasApiPagamentoStatus.API_KEY)
                .post(ClientResponse.class, jsonFinalizaCompraCartaoCredito);

        String stringRetornoFinalizaCompraCartaoCredito = clientResponseFinalizaCompraCartaoCredito.getEntity(String.class);
        int statusFinalizaCompraCartaoCredito = clientResponseFinalizaCompraCartaoCredito.getStatus();
        clientResponseFinalizaCompraCartaoCredito.close();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        if (statusFinalizaCompraCartaoCredito != 200) {

            for (BoletoJuno boletoJuno : cobrancas) {
                boletoJunoRepository.deleteById(boletoJuno.getId());
                boletoJunoRepository.flush();
            }

            ErroResponseApiAsaasDTO erroResponseApiAsaasDTO = objectMapper
                    .readValue(stringRetornoFinalizaCompraCartaoCredito, new TypeReference<ErroResponseApiAsaasDTO>() {
                    });

            logger.error("--------Erro ao finalizar compra por cartão de credito-----------");
            logger.error("--------Erro ao efetuar cobrança-----------");
            logger.error("--------Retorno API-----------: " + stringRetornoFinalizaCompraCartaoCredito);
            return new ResponseEntity<String>("Erro ao efetuar cobrança" + erroResponseApiAsaasDTO.listaErros(), HttpStatus.NOT_ACCEPTABLE);
        }

        CobrancaGeradaCartaoCreditoAsaasDTO cartaoCredito = objectMapper.
                readValue(stringRetornoFinalizaCompraCartaoCredito, new TypeReference<CobrancaGeradaCartaoCreditoAsaasDTO>() {
                });

        int recorrencia = 1;
        List<BoletoJuno> boletoJunos = new ArrayList<BoletoJuno>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dataCobranca = dateFormat.parse(cartaoCredito.getDueDate());
        Calendar calendar = Calendar.getInstance();

        for (int p = 1; p <= qtdparcela; p++) {

            BoletoJuno boletoJuno = new BoletoJuno();

            boletoJuno.setChargeICartao(cartaoCredito.getId());
            boletoJuno.setCheckoutUrl(cartaoCredito.getInvoiceUrl());
            boletoJuno.setCode(cartaoCredito.getId());
            boletoJuno.setDataVencimento(dateFormat.format(dataCobranca));

            calendar.setTime(dataCobranca);
            calendar.add(Calendar.MONTH, 1);
            dataCobranca = calendar.getTime();

            boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
            boletoJuno.setIdChrBoleto(cartaoCredito.getId());
            boletoJuno.setIdPix(cartaoCredito.getId());
            boletoJuno.setInstallmentLink(cartaoCredito.getInvoiceUrl());
            boletoJuno.setQuitado(false);
            boletoJuno.setRecorrencia(recorrencia);
            boletoJuno.setValor(BigDecimal.valueOf(cobrancaApiAsaasCartaoDTO.getInstallmentValue()));
            boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

            recorrencia++;
            boletoJunos.add(boletoJuno);
        }

        boletoJunoRepository.saveAllAndFlush(boletoJunos);

        if (cartaoCredito.getStatus().equalsIgnoreCase("CONFIRMED")) {

            for (BoletoJuno boletoJuno : boletoJunos) {
                boletoJunoRepository.quitarBoletoById(boletoJuno.getId());
            }

            vd_Cp_Loja_virt_repository.updateFinalizaVenda(vendaCompraLojaVirtual.getId());

            logger.info("--------Finalização de compra por cartão de credito realizada com sucesso-----------");
            logger.info("--------Retorno API-----------: " + stringRetornoFinalizaCompraCartaoCredito);

            return new ResponseEntity<String>("sucesso", HttpStatus.OK);
        }else{
            logger.error("--------Erro ao finalizar compra por cartão de credito-----------");
            logger.error("--------Retorno API-----------: " + stringRetornoFinalizaCompraCartaoCredito);
            return new ResponseEntity<String>("Pagamento não pode ser finalizado: Status:" + cartaoCredito.getStatus(), HttpStatus.OK);

        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/finalizarCompraCartao")
    public ResponseEntity<String> finalizarCompraCartaoJuno(
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
            return new ResponseEntity<String>("Pagamento não autorizado pela instituição financeira", HttpStatus.OK);
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

    @RequestMapping(method = RequestMethod.POST, value = "**/finalizarCompraCartaoJuno")
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


        /**------------------------REALIZANDO PAGAMENTO POR CARTÃO-------------------------**/

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