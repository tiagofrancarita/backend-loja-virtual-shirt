package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.config.tokens.ApiTokenIntegracaoJuno;
import br.com.franca.ShirtVirtual.config.tokens.AsaasApiPagamentoStatus;
import br.com.franca.ShirtVirtual.model.AccessTokenJunoAPI;
import br.com.franca.ShirtVirtual.model.BoletoJuno;
import br.com.franca.ShirtVirtual.model.VendaCompraLojaVirtual;
import br.com.franca.ShirtVirtual.repository.AccesTokenJunoRepository;
import br.com.franca.ShirtVirtual.repository.BoletoJunoRepository;
import br.com.franca.ShirtVirtual.repository.VendaCompraLojaVirtualRepository;
import br.com.franca.ShirtVirtual.utils.GeradorCpfValido;
import br.com.franca.ShirtVirtual.utils.ValidaCpf;
import br.com.franca.ShirtVirtual.utils.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;


@Service
public class ServiceJunoBoleto implements Serializable {

    private static final long serialVersionUID = 1L;

    private AccessTokenJunoService accessTokenJunoService;
    private AccesTokenJunoRepository accesTokenJunoRepository;
    private VendaCompraLojaVirtualRepository vd_Cp_Loja_virt_repository;
    private BoletoJunoRepository boletoJunoRepository;
    private GeradorCpfValido geradorCpfValido;

    @Autowired
    public ServiceJunoBoleto(AccessTokenJunoService accessTokenJunoService, AccesTokenJunoRepository accesTokenJunoRepository, VendaCompraLojaVirtualRepository vd_Cp_Loja_virt_repository, BoletoJunoRepository boletoJunoRepository, GeradorCpfValido geradorCpfValido) {
        this.accessTokenJunoService = accessTokenJunoService;
        this.accesTokenJunoRepository = accesTokenJunoRepository;
        this.vd_Cp_Loja_virt_repository = vd_Cp_Loja_virt_repository;
        this.boletoJunoRepository = boletoJunoRepository;
        this.geradorCpfValido = geradorCpfValido;
    }

    /**
     * Retorna id do customer da API Asass;
     * @return id
     */
    public String  buscaClientePessoaApiAsaas(ObjetoPostCarneJuno objetoPostCarneJuno) throws Exception {

        String customer_id ="";

        // ------------------------------ INICIO - Cria o cliente na API Asass ------------------------------

        Client client = new HostIgnoringClient(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX).hostIgnoringClient();
        WebResource webResource = client.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "customers?email=" + objetoPostCarneJuno.getEmail());

        ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("access_token", AsaasApiPagamentoStatus.API_KEY)
                .get(ClientResponse.class);

        LinkedHashMap<String, Object>  parser = new JSONParser(clientResponse.getEntity(String.class)).parseObject();
        clientResponse.close();
        Integer totalClientes = Integer.parseInt(parser.get("totalCount").toString());

        if (totalClientes <= 0) { /*Não existe cliente na API Asass*/

            ClienteAsaasApiPagamento clienteAsaasApiPagamento = new ClienteAsaasApiPagamento();

            if (!ValidaCpf.isCPF(objetoPostCarneJuno.getPayerCpfCnpj())){
                clienteAsaasApiPagamento.setCpfCnpj(geradorCpfValido.cpf(false));
            }else {
                clienteAsaasApiPagamento.setCpfCnpj(objetoPostCarneJuno.getPayerCpfCnpj());
            }
            clienteAsaasApiPagamento.setName(objetoPostCarneJuno.getPayerName());
            clienteAsaasApiPagamento.setEmail(objetoPostCarneJuno.getEmail());
            clienteAsaasApiPagamento.setPhone(objetoPostCarneJuno.getPayerPhone());

            Client clientNovoCadastroAsas = new HostIgnoringClient(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX).hostIgnoringClient();
            WebResource webResourceNovoCadastroAsas = clientNovoCadastroAsas.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "customers");

            ClientResponse clientResponseNovoCadastroAsas = webResourceNovoCadastroAsas.accept("application/json;charset=UTF-8")
                    .header("Content-Type", "application/json")
                    .header("access_token", AsaasApiPagamentoStatus.API_KEY)
                    .post(ClientResponse.class, new ObjectMapper().writeValueAsString(clienteAsaasApiPagamento));

                LinkedHashMap<String, Object>  parserNovoCadastroAsas = new JSONParser(clientResponseNovoCadastroAsas.getEntity(String.class)).parseObject();
                clientResponseNovoCadastroAsas.close();
                customer_id = parserNovoCadastroAsas.get("id").toString();

        } else { /*Existe cliente na API Asass*/

           List<Object> data = (List<Object>) parser.get("data");
           customer_id =  new Gson().toJsonTree(data.get(0)).getAsJsonObject().get("id").toString().replaceAll("\"", "");  /*Pega o id do cliente*/


        }

        System.out.println("ID do cliente na API Asass: " + customer_id);
        return customer_id;

        // ------------------------------ FIM - Cria o cliente na API Asass ------------------------------

    }

    /**
     * Cria a chave da API Asass para o PIX;
     * @return Chave
     */
    public String criarChavePixAsaas() throws Exception {

        Client client = new HostIgnoringClient(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX).hostIgnoringClient();
        WebResource webResource = client.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "pix/addressKeys");

        ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("access_token", AsaasApiPagamentoStatus.API_KEY)
                .post(ClientResponse.class, "{\"type\":\"EVP\"}");

        String strinRetornoo = clientResponse.getEntity(String.class);
        clientResponse.close();
        return strinRetornoo;

    }


    public String cancelarBoleto(String code) throws Exception {

        AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();

        Client client = new HostIgnoringClient("https://api.juno.com.br/").hostIgnoringClient();
        WebResource webResource = client.resource("https://api.juno.com.br/charges/"+code+"/cancelation");

        ClientResponse clientResponse = webResource.accept(MediaType.APPLICATION_JSON)
                .header("X-Api-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracaoJuno.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .put(ClientResponse.class);

        if (clientResponse.getStatus() == 204) {

            boletoJunoRepository.deleteByCode(code);

            return "Cancelado com sucesso";
        }

        return clientResponse.getEntity(String.class);

    }



    /*
     * {"id":"wbh_AE815607C1F5A94934934A2EA3CA0180","url":"https://lojavirtualmentoria-env.eba-bijtuvkg.sa-east-1.elasticbeanstalk.com/loja_virtual_mentoria/requisicaojunoboleto/notificacaoapiv2","secret":"23b85f4998289533ed3ee310ae9d5bd3f803fadac7fb1ecff0296fbf1bb060f8","status":"ACTIVE","eventTypes":[{"id":"evt_DC2E7E8848B08C62","name":"DOCUMENT_STATUS_CHANGED","label":"O status de um documento foi alterado","status":"ENABLED"}],"_links":{"self":{"href":"https://api.juno.com.br/api-integration/notifications/webhooks/wbh_AE815607C1F5A94934934A2EA3CA0180"}}}
     *
     * */
    public String criarWebHook(CriarWebHook criarWebHook) throws Exception {

        AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();

        Client client = new HostIgnoringClient("https://api.juno.com.br/").hostIgnoringClient();
        WebResource webResource = client.resource("https://api.juno.com.br/notifications/webhooks");

        String json = new ObjectMapper().writeValueAsString(criarWebHook);

        ClientResponse clientResponse = webResource
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracaoJuno.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .post(ClientResponse.class, json);

        String resposta = clientResponse.getEntity(String.class);
        clientResponse.close();

        return resposta;

    }

    public String listaWebHook() throws Exception {

        AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();

        Client client = new HostIgnoringClient("https://api.juno.com.br/").hostIgnoringClient();
        WebResource webResource = client.resource("https://api.juno.com.br/notifications/webhooks");

        ClientResponse clientResponse = webResource
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracaoJuno.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .get(ClientResponse.class);

        String resposta = clientResponse.getEntity(String.class);

        return resposta;

    }



    public void deleteWebHook(String idWebHook) throws Exception {

        AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();

        Client client = new HostIgnoringClient("https://api.juno.com.br/").hostIgnoringClient();
        WebResource webResource = client.resource("https://api.juno.com.br/notifications/webhooks/" + idWebHook);

        webResource
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("X-API-Version", 2)
                .header("X-Resource-Token", ApiTokenIntegracaoJuno.TOKEN_PRIVATE_JUNO)
                .header("Authorization", "Bearer " + accessTokenJunoAPI.getAccess_token())
                .delete();


    }

    public AccessTokenJunoAPI obterTokenApiJuno() throws Exception {

        AccessTokenJunoAPI accessTokenJunoAPI = accessTokenJunoService.buscaTokenAtivoJuno();

        if (accessTokenJunoAPI == null || (accessTokenJunoAPI != null && accessTokenJunoAPI.expirado()) ) {

            String clienteID = "vi7QZerW09C8JG1o";
            String secretID = "$A_+&ksH}&+2<3VM]1MZqc,F_xif_-Dc";

            Client client = new HostIgnoringClient("https://api.juno.com.br/").hostIgnoringClient();

            WebResource webResource = client.resource("https://api.juno.com.br/authorization-server/oauth/token?grant_type=client_credentials");

            String basicChave = clienteID + ":" + secretID;
            String token_autenticao = DatatypeConverter.printBase64Binary(basicChave.getBytes());

            ClientResponse clientResponse = webResource.
                    accept(MediaType.APPLICATION_FORM_URLENCODED)
                    .type(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + token_autenticao)
                    .post(ClientResponse.class);

            if (clientResponse.getStatus() == 200) { /*Sucesso*/
                accesTokenJunoRepository.deleteAll();
                accesTokenJunoRepository.flush();

                AccessTokenJunoAPI accessTokenJunoAPI2 = clientResponse.getEntity(AccessTokenJunoAPI.class);
                accessTokenJunoAPI2.setToken_acesso(token_autenticao);

                accessTokenJunoAPI2 = accesTokenJunoRepository.saveAndFlush(accessTokenJunoAPI2);

                return accessTokenJunoAPI2;
            }else {
                return null;
            }


        }else {
            return accessTokenJunoAPI;
        }
    }

    public String gerarCarneApiAsaas(ObjetoPostCarneJuno objetoPostCarneJuno) throws Exception {

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository.findById(objetoPostCarneJuno.getIdVenda()).get();

        CobrancaApiAsaasDTO cobrancaApiAsaasDTO = new CobrancaApiAsaasDTO();
        cobrancaApiAsaasDTO.setCustomer(this.buscaClientePessoaApiAsaas(objetoPostCarneJuno));
        cobrancaApiAsaasDTO.setDescription("Pix ou boleto gerado para a cobrança da venda " + vendaCompraLojaVirtual.getId());
        cobrancaApiAsaasDTO.setInstallmentValue(vendaCompraLojaVirtual.getValorTotalVendaLoja().floatValue());
        cobrancaApiAsaasDTO.setInstallmentCount(1);

        Calendar dataVencimento = Calendar.getInstance();
        dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
        cobrancaApiAsaasDTO.setDueDate(new SimpleDateFormat("yyyy-MM-dd").format(dataVencimento.getTime()));
        cobrancaApiAsaasDTO.getInterest().setValue(1F);
        cobrancaApiAsaasDTO.getFine().setValue(1F);

        // PIX BOLETO ou UNDEFINED
        cobrancaApiAsaasDTO.setBillingType("UNDEFINED");

        String jsonGerarCarneApiAsaas = new ObjectMapper().writeValueAsString(cobrancaApiAsaasDTO);

        Client clientGerarCarneApiAsaas = new HostIgnoringClient(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX).hostIgnoringClient();
        WebResource webResourceGerarCarneApiAsaas = clientGerarCarneApiAsaas.resource(AsaasApiPagamentoStatus.URL_API_ASAAS_SANDBOX + "payments");

        ClientResponse clientResponseGerarCarneApiAsaas = webResourceGerarCarneApiAsaas
                .accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("access_token", AsaasApiPagamentoStatus.API_KEY)
                .post(ClientResponse.class, jsonGerarCarneApiAsaas);

        String stringRetornoGerarCarneApiAsaas = clientResponseGerarCarneApiAsaas.getEntity(String.class);
        clientResponseGerarCarneApiAsaas.close();

        return stringRetornoGerarCarneApiAsaas;
    }


    /*
     * Método que gera o PIX  e Boleto com a API da Juno/Ebanx
     * */
    public String gerarCarneApiJuno(ObjetoPostCarneJuno objetoPostCarneJuno) throws Exception {

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vd_Cp_Loja_virt_repository.findById(objetoPostCarneJuno.getIdVenda()).get();

        CobrancaJunoAPI cobrancaJunoAPI = new CobrancaJunoAPI();

        cobrancaJunoAPI.getCharge().setPixKey(ApiTokenIntegracaoJuno.CHAVE_BOLETO_PIX_JUNO);
        cobrancaJunoAPI.getCharge().setDescription(objetoPostCarneJuno.getDescription());
        cobrancaJunoAPI.getCharge().setAmount(Float.valueOf(objetoPostCarneJuno.getTotalAmount()));
        cobrancaJunoAPI.getCharge().setInstallments(Integer.parseInt(objetoPostCarneJuno.getInstallments()));

        Calendar dataVencimento = Calendar.getInstance();
        dataVencimento.add(Calendar.DAY_OF_MONTH, 7);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        cobrancaJunoAPI.getCharge().setDueDate(dateFormat.format(dataVencimento.getTime()));

        cobrancaJunoAPI.getCharge().setFine(BigDecimal.valueOf(1.00));
        cobrancaJunoAPI.getCharge().setInterest(BigDecimal.valueOf(1.00));
        cobrancaJunoAPI.getCharge().setMaxOverdueDays(10);
        cobrancaJunoAPI.getCharge().getPaymentTypes().add("BOLETO_PIX");

        cobrancaJunoAPI.getBilling().setName(objetoPostCarneJuno.getPayerName());
        cobrancaJunoAPI.getBilling().setDocument(objetoPostCarneJuno.getPayerCpfCnpj());
        cobrancaJunoAPI.getBilling().setEmail(objetoPostCarneJuno.getEmail());
        cobrancaJunoAPI.getBilling().setPhone(objetoPostCarneJuno.getPayerPhone());

        AccessTokenJunoAPI accessTokenJunoAPI = this.obterTokenApiJuno();
        if (accessTokenJunoAPI != null) {

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

            if (clientResponse.getStatus() == 200) { /*Retornou com sucesso*/

                clientResponse.close();
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY); /*Converte relacionamento um para muitos dentro de json*/

                BoletoGeradoApiJuno jsonRetornoObj = objectMapper.readValue(stringRetorno,
                        new TypeReference<BoletoGeradoApiJuno>() {});

                int recorrencia = 1;

                List<BoletoJuno> boletoJunos = new ArrayList<BoletoJuno>();

                for (ConteudoBoletoJuno c : jsonRetornoObj.get_embedded().getCharges()) {

                    BoletoJuno boletoJuno = new BoletoJuno();

                    boletoJuno.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
                    boletoJuno.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
                    boletoJuno.setCode(c.getCode());
                    boletoJuno.setLink(c.getLink());
                    boletoJuno.setDataVencimento(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(c.getDueDate())));
                    boletoJuno.setCheckoutUrl(c.getCheckoutUrl());
                    boletoJuno.setValor(new BigDecimal(c.getAmount()));
                    boletoJuno.setIdChrBoleto(c.getId());
                    boletoJuno.setInstallmentLink(c.getInstallmentLink());
                    boletoJuno.setIdPix(c.getPix().getId());
                    boletoJuno.setPayloadInBase64(c.getPix().getPayloadInBase64());
                    boletoJuno.setImageInBase64(c.getPix().getImageInBase64());
                    boletoJuno.setRecorrencia(recorrencia);

                    boletoJunos.add(boletoJuno);
                    recorrencia ++;

                }

                boletoJunoRepository.saveAllAndFlush(boletoJunos);

                return boletoJunos.get(0).getLink();

            }else {
                return stringRetorno;
            }

        }else {
            return "Não exite chave de acesso para a API";
        }

    }
}