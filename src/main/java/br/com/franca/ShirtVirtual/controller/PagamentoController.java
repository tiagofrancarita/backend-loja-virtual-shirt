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
        } else {
            modelAndView.addObject("venda", vendaService.consultaVenda(compraLojaVirtual));
        }

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "**/finalizarCompraCartao")
    public ResponseEntity<String> finalizarCompraCartaoAsaas(@RequestParam("cardNumber") String cardNumber, @RequestParam("holderName") String holderName,
                                                             @RequestParam("securityCode") String securityCode, @RequestParam("expirationMonth") String expirationMonth, @RequestParam("expirationYear") String expirationYear,
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

                if (boletoJunoRepository.existsById(boletoJuno.getId())) {
                    boletoJunoRepository.deleteById(boletoJuno.getId());
                    boletoJunoRepository.flush();
                }
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
        } else {

            logger.error("--------Erro ao finalizar compra por cartão de credito-----------");
            logger.error("--------Retorno API-----------: " + stringRetornoFinalizaCompraCartaoCredito);
            return new ResponseEntity<String>("Pagamento não pode ser finalizado: Status:" + cartaoCredito.getStatus(), HttpStatus.OK);

        }
    }
}