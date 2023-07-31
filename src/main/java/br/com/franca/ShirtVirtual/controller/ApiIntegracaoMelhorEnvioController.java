package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.config.tokens.ApiTokenMelhorEnvio;
import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Endereco;
import br.com.franca.ShirtVirtual.model.ItemVendaLoja;
import br.com.franca.ShirtVirtual.model.StatusRastreio;
import br.com.franca.ShirtVirtual.model.VendaCompraLojaVirtual;
import br.com.franca.ShirtVirtual.repository.EnderecoRepository;
import br.com.franca.ShirtVirtual.repository.StatusRastreioRepository;
import br.com.franca.ShirtVirtual.repository.VendaCompraLojaVirtualRepository;
import br.com.franca.ShirtVirtual.service.StatusRastreioService;
import br.com.franca.ShirtVirtual.utils.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RestController
@RequestMapping("/melhorEnvio")
@Api(tags = "Melhor-Envio-Transportadora")
@Slf4j
public class ApiIntegracaoMelhorEnvioController {

    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
    private EnderecoRepository enderecoRepository;
    private JdbcTemplate jdbcTemplate;
    private StatusRastreioRepository statusRastreioRepository;
    private StatusRastreioService statusRastreioService;

    @Autowired
    public ApiIntegracaoMelhorEnvioController(VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository, EnderecoRepository enderecoRepository, JdbcTemplate jdbcTemplate, StatusRastreioRepository statusRastreioRepository, StatusRastreioService statusRastreioService) {
        this.vendaCompraLojaVirtualRepository = vendaCompraLojaVirtualRepository;
        this.enderecoRepository = enderecoRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.statusRastreioRepository = statusRastreioRepository;
        this.statusRastreioService = statusRastreioService;
    }

    @ApiOperation("Consulta valores do frete")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ApiIntegracaoMelhorEnvioController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @PostMapping("/consultaFreteLojaVirtual")
    public ResponseEntity<List<EmpresaTransporteDTO>> consultaFreteLojaVirtual(@RequestBody @Valid ConsultaFreteDTO consultaFreteDTO) throws Exception {



        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(consultaFreteDTO);

        OkHttpClient client = new OkHttpClient().newBuilder() .build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT +"/api/v2/me/shipment/calculate")
                .method("POST", body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", "tiagofranca.rita@gmail.com")
                .build();

        okhttp3.Response response = client.newCall(request).execute();

        JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());

        Iterator<JsonNode> iterator = jsonNode.iterator();

        List<EmpresaTransporteDTO> empresaTransporteDTOs = new ArrayList<EmpresaTransporteDTO>();

        while(iterator.hasNext()) {
            JsonNode node = iterator.next();

            EmpresaTransporteDTO empresaTransporteDTO = new EmpresaTransporteDTO();

            if (node.get("id") != null) {
                empresaTransporteDTO.setId(node.get("id").asText());
            }

            if (node.get("name") != null) {
                empresaTransporteDTO.setNome(node.get("name").asText());
            }

            if (node.get("price") != null) {
                empresaTransporteDTO.setValor(node.get("price").asText());
            }

            if (node.get("company") != null) {
                empresaTransporteDTO.setEmpresa(node.get("company").get("name").asText());
                empresaTransporteDTO.setPicture(node.get("company").get("picture").asText());
            }

            if (empresaTransporteDTO.dadosOK()) {
                empresaTransporteDTOs.add(empresaTransporteDTO);
            }
        }

        return new ResponseEntity<List<EmpresaTransporteDTO>>(empresaTransporteDTOs, HttpStatus.OK);

    }

    @ApiOperation("Inclui o frete no carrinho, faz a compra e gera etiqueta")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ApiIntegracaoMelhorEnvioController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/imprimeCompraEtiquetaFrete/{idVenda}")
    public ResponseEntity<String> imprimeCompraEtiquetaFrete(@PathVariable Long idVenda) throws ExceptionShirtVirtual, IOException {

        VendaCompraLojaVirtual compraLojaVirtual = vendaCompraLojaVirtualRepository.findById(idVenda).orElseGet(null);

        if (compraLojaVirtual == null){

            return new ResponseEntity<String>("Venda não encontrada na base de dados", HttpStatus.NOT_FOUND);
        }

        List<Endereco> enderecos = enderecoRepository.enderecoPj(compraLojaVirtual.getEmpresa().getId());
        compraLojaVirtual.getEmpresa().setEnderecos(enderecos);

        FreteDTO envioEtiquetaDTO = new FreteDTO();
        envioEtiquetaDTO.setAgency("1");
        envioEtiquetaDTO.setService(compraLojaVirtual.getServicoTransportadora());

        envioEtiquetaDTO.setService(compraLojaVirtual.getServicoTransportadora());
        envioEtiquetaDTO.setAgency("49");
        envioEtiquetaDTO.getFrom().setName(compraLojaVirtual.getEmpresa().getNome());
        envioEtiquetaDTO.getFrom().setPhone(compraLojaVirtual.getEmpresa().getTelefone());
        envioEtiquetaDTO.getFrom().setEmail(compraLojaVirtual.getEmpresa().getEmail());
        envioEtiquetaDTO.getFrom().setCompany_document(compraLojaVirtual.getEmpresa().getCnpj());
        envioEtiquetaDTO.getFrom().setState_register(compraLojaVirtual.getEmpresa().getInscEstadual());
        envioEtiquetaDTO.getFrom().setAddress(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getLogradouro());
        envioEtiquetaDTO.getFrom().setComplement(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getComplemento());
        envioEtiquetaDTO.getFrom().setLocation_number(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getNumero());
        envioEtiquetaDTO.getFrom().setDistrict(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getEstado());
        envioEtiquetaDTO.getFrom().setCity(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCidade());
        envioEtiquetaDTO.getFrom().setCountry_id(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getPais());
        envioEtiquetaDTO.getFrom().setPostal_code(compraLojaVirtual.getEmpresa().getEnderecos().get(0).getCep());
        envioEtiquetaDTO.getFrom().setNote("Não há");


        envioEtiquetaDTO.getTo().setName(compraLojaVirtual.getPessoa().getNome());
        envioEtiquetaDTO.getTo().setPhone(compraLojaVirtual.getPessoa().getTelefone());
        envioEtiquetaDTO.getTo().setEmail(compraLojaVirtual.getPessoa().getEmail());
        envioEtiquetaDTO.getTo().setDocument(compraLojaVirtual.getPessoa().getCpf());
        envioEtiquetaDTO.getTo().setAddress(compraLojaVirtual.getPessoa().enderecoEntrega().getLogradouro());
        envioEtiquetaDTO.getTo().setComplement(compraLojaVirtual.getPessoa().enderecoEntrega().getComplemento());
        envioEtiquetaDTO.getTo().setLocation_number(compraLojaVirtual.getPessoa().enderecoEntrega().getNumero());
        envioEtiquetaDTO.getTo().setDistrict(compraLojaVirtual.getPessoa().enderecoEntrega().getEstado());
        envioEtiquetaDTO.getTo().setCity(compraLojaVirtual.getPessoa().enderecoEntrega().getCidade());
        envioEtiquetaDTO.getTo().setState_abbr(compraLojaVirtual.getPessoa().enderecoEntrega().getEstado());
        envioEtiquetaDTO.getTo().setCountry_id(compraLojaVirtual.getPessoa().enderecoEntrega().getPais());
        envioEtiquetaDTO.getTo().setPostal_code(compraLojaVirtual.getPessoa().enderecoEntrega().getCep());
        envioEtiquetaDTO.getTo().setNote("Não há");


        List<ProductsEnvioEtiquetaDTO> products = new ArrayList<ProductsEnvioEtiquetaDTO>();

        for (ItemVendaLoja itemVendaLoja : compraLojaVirtual.getItemVendaLojas()) {

            ProductsEnvioEtiquetaDTO dto = new ProductsEnvioEtiquetaDTO();

            dto.setName(itemVendaLoja.getProduto().getNome());
            dto.setQuantity(itemVendaLoja.getQuantidade().toString());
            dto.setUnitary_value("" + itemVendaLoja.getProduto().getValorTotalVenda().doubleValue());

            products.add(dto);
        }


        envioEtiquetaDTO.setProducts(products);


        List<VolumesEnvioEtiquetaDTO> volumes = new ArrayList<VolumesEnvioEtiquetaDTO>();

        for (ItemVendaLoja itemVendaLoja : compraLojaVirtual.getItemVendaLojas()) {

            VolumesEnvioEtiquetaDTO dto = new VolumesEnvioEtiquetaDTO();

            dto.setHeight(itemVendaLoja.getProduto().getAltura().toString());
            dto.setLength(itemVendaLoja.getProduto().getProfundidade().toString());
            dto.setWeight(itemVendaLoja.getProduto().getPeso().toString());
            dto.setWidth(itemVendaLoja.getProduto().getLargura().toString());

            volumes.add(dto);
        }


        envioEtiquetaDTO.setVolumes(volumes);

        envioEtiquetaDTO.getOptions().setInsurance_value("" + compraLojaVirtual.getValorTotalVendaLoja().doubleValue());
        envioEtiquetaDTO.getOptions().setReceipt(false);
        envioEtiquetaDTO.getOptions().setOwn_hand(false);
        envioEtiquetaDTO.getOptions().setReverse(false);
        envioEtiquetaDTO.getOptions().setNon_commercial(false);
        envioEtiquetaDTO.getOptions().getInvoice().setKey(compraLojaVirtual.getNotaFiscalVenda().getNumeroNotaFiscalVenda());
        envioEtiquetaDTO.getOptions().setPlatform(compraLojaVirtual.getEmpresa().getNomeFantasia());

        TagsEnvioDto dtoTagEnvio = new TagsEnvioDto();
        dtoTagEnvio.setTag("Identificação do pedido na plataforma, exemplo:" + compraLojaVirtual.getId());
        dtoTagEnvio.setUrl(null);
        envioEtiquetaDTO.getOptions().getTags().add(dtoTagEnvio);


        String jsonEnvio = new ObjectMapper().writeValueAsString(envioEtiquetaDTO);



        OkHttpClient client = new OkHttpClient().newBuilder().build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonEnvio);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT + "/api/v2/me/cart")
                .method("POST", body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", ApiTokenMelhorEnvio.EMAIL)
                .build();

        okhttp3.Response response = client.newCall(request).execute();

        System.out.println(response.message());

        String respostaJson = response.body().string();

        if (respostaJson.contains("error")) {
            throw new ExceptionShirtVirtual(respostaJson);
        }

        JsonNode jsonNode = new ObjectMapper().readTree(respostaJson);

        Iterator<JsonNode> iterator = jsonNode.iterator();

        String idEtiqueta = "";

        while(iterator.hasNext()) {
            JsonNode node = iterator.next();
            if (node.get("id") != null) {
                idEtiqueta = node.get("id").asText();
            }else {
                idEtiqueta= node.asText();
            }
            break;
        }

        jdbcTemplate.execute("begin; update vd_cp_loja_virt set codigo_etiqueta = '"+idEtiqueta+"' where id = "+compraLojaVirtual.getId()+"  ;commit;");

        // Pagar Frete, primeiro precisa pagar o frete para depois gerar etiqueta

        OkHttpClient clientCheckout = new OkHttpClient().newBuilder().build();
        okhttp3.MediaType mediaTypeCheckout = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody bodyCheckout = okhttp3.RequestBody.create(mediaTypeCheckout, "{\n    \"mode\": \"private\",\n    \"orders\": [\n        \""+idEtiqueta+"\"\n    ]\n}");
        okhttp3.Request requestCheckout = new okhttp3.Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT + "/api/v2/me/shipment/checkout")
                .method("POST", bodyCheckout)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", ApiTokenMelhorEnvio.EMAIL)
                .build();

        okhttp3.Response responseCheckout = clientCheckout.newCall(requestCheckout).execute();

        if (!responseCheckout.isSuccessful()){

            return new ResponseEntity<String>("Erro ao processar a compra da etiqueta", HttpStatus.NOT_FOUND);

        }

        OkHttpClient clientGeraEtiqueta = new OkHttpClient().newBuilder().build();
        okhttp3.MediaType mediaTypeGeraEtiqueta =  okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody bodyGeraEtiqueta =  okhttp3.RequestBody.create(mediaTypeGeraEtiqueta, "{\n    \"orders\":[\n        \""+idEtiqueta+"\"\n    ]\n}");
        okhttp3.Request requestGeraEtiqueta = new  okhttp3.Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT  + "/api/v2/me/shipment/generate")
                .method("POST", bodyGeraEtiqueta)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " +  ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", ApiTokenMelhorEnvio.EMAIL)
                .build();

        okhttp3.Response responseGeraEtiqueta = clientGeraEtiqueta.newCall(requestGeraEtiqueta).execute();

        if (!responseGeraEtiqueta.isSuccessful()) {
            return new ResponseEntity<String>("Não foi possível gerar a etiqueta", HttpStatus.OK);
        }

        /*Faz impresão das etiquetas*/

        OkHttpClient clientImpressaoEtiqueta = new OkHttpClient().newBuilder().build();
        okhttp3.MediaType mediaTypeImpressaoEtiqueta = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody bodyImpressaoEtiqueta = okhttp3.RequestBody.create(mediaTypeImpressaoEtiqueta, "{\n    \"mode\": \"private\",\n    \"orders\": [\n        \""+idEtiqueta+"\"\n    ]\n}");
        okhttp3.Request requestImpressaoEtiqueta = new okhttp3.Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT  + "/api/v2/me/shipment/print")
                .method("POST", bodyImpressaoEtiqueta)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", ApiTokenMelhorEnvio.EMAIL)
                .build();

        okhttp3.Response responseImpressaoEtiqueta = clientImpressaoEtiqueta.newCall(requestImpressaoEtiqueta).execute();


        if (!responseImpressaoEtiqueta.isSuccessful()) {
            return new ResponseEntity<String>("Não foi imprimir a etiqueta.", HttpStatus.NO_CONTENT);
        }

        String urlEtiqueta = responseImpressaoEtiqueta.body().string();


        jdbcTemplate.execute("begin; update vd_cp_loja_virt set url_impressao_etiqueta =  '"+urlEtiqueta+"'  where id = " + compraLojaVirtual.getId() + "; commit;");


        OkHttpClient clientTracking = new OkHttpClient();

        okhttp3.MediaType mediaTypeTracking = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody bodyTracking = okhttp3.RequestBody.create(mediaTypeTracking, "{\"orders\":[\""+idEtiqueta+"\"]}");
        okhttp3.Request requestTracking = new okhttp3.Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT  + "/api/v2/me/shipment/tracking")
                .method("POST", bodyTracking)
                .addHeader("Accept", "application/json")
                .addHeader("Content-type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", ApiTokenMelhorEnvio.EMAIL)
                .build();

        okhttp3.Response responseTracking = clientTracking.newCall(requestTracking).execute();

        JsonNode jsonNodeTracking = new ObjectMapper().readTree(responseTracking.body().string());

        Iterator<JsonNode> iteratorTracking = jsonNodeTracking.iterator();

        String idEtiquetaTracking = "";
        String statusTracking = "";

        while(iteratorTracking.hasNext()) {
            JsonNode node = iteratorTracking.next();
            if (node.get("tracking") != null && node.get("status") != null ) {
                idEtiquetaTracking = node.get("tracking").asText();
                statusTracking = node.get("status").asText();
            }else {
                idEtiquetaTracking= node.asText();
                statusTracking= node.asText();
            }
            break;
        }

        System.out.println(idEtiquetaTracking);
        System.out.println(statusTracking);

        List<StatusRastreio> rastreios =	statusRastreioRepository.listaRastreioVenda(idVenda);

        if (rastreios.isEmpty()) {

            StatusRastreio rastreio = new StatusRastreio();
            rastreio.setEmpresa(compraLojaVirtual.getEmpresa());
            rastreio.setVendaCompraLojaVirtual(compraLojaVirtual);
            rastreio.setUrlRastreio("https://www.melhorrastreio.com.br/rastreio/" + idEtiquetaTracking);
            rastreio.setStatusRastreio(statusTracking);

            statusRastreioRepository.saveAndFlush(rastreio);
        }else {
            statusRastreioRepository.salvaUrlRastreio("https://www.melhorrastreio.com.br/rastreio/" + idEtiquetaTracking, idVenda);
        }

        return new ResponseEntity<String>("Sucesso", HttpStatus.OK);

    }

    @ApiOperation("Cancela etiqueta")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ApiIntegracaoMelhorEnvioController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/verificaEtiquetaCancelar/{idEtiqueta}")
    public ResponseEntity<String> verificaEtiquetaCancelar(@PathVariable String idEtiqueta) throws ExceptionShirtVirtual, IOException {

        OkHttpClient clientVerificaEtiqueta = new OkHttpClient().newBuilder().build();

        okhttp3.MediaType mediaTypeVerificaEtiqueta = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody bodyVerificaEtiqueta = okhttp3.RequestBody.create(mediaTypeVerificaEtiqueta, "{\n    \"order\": {\n        \"id\": \""+idEtiqueta+"\"]}");
        okhttp3.Request requestVerificaEtiqueta = new okhttp3.Request.Builder()
                .url("https://sandbox.melhorenvio.com.br/api/v2/me/shipment/cancellable")
                .method("POST", bodyVerificaEtiqueta)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", ApiTokenMelhorEnvio.EMAIL)
                .build();

        okhttp3.Response responseVerificaEtiqueta = clientVerificaEtiqueta.newCall(requestVerificaEtiqueta).execute();

         return new ResponseEntity<String>(responseVerificaEtiqueta.body().string(), HttpStatus.OK);
    }

    @ApiOperation("Cancela etiqueta")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ApiIntegracaoMelhorEnvioController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/cancelaEtiqueta/{idEtiqueta}/{descricao}/{reason_id}")
    public ResponseEntity<String> cancelaEtiqueta(@PathVariable String idEtiqueta, @PathVariable String descricao, @PathVariable String reason_id) throws ExceptionShirtVirtual, IOException {


        //String reason_id = "2";

        OkHttpClient clientCancelaEtiqueta = new OkHttpClient().newBuilder().build();
        okhttp3.MediaType mediaTypeCancelaEtiqueta = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody bodyCancelaEtiqueta = okhttp3.RequestBody.create(mediaTypeCancelaEtiqueta, "{\n    \"order\": {\n        \"id\": \""+idEtiqueta+"\",\n        \"reason_id\": \""+reason_id+"\",\n        \"description\": \""+descricao+"\"\n    }\n}");
        okhttp3.Request requestCancelaEtiqueta = new okhttp3.Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT + "/api/v2/me/shipment/cancel")
                .method("POST", bodyCancelaEtiqueta)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", ApiTokenMelhorEnvio.EMAIL)
                .build();

        okhttp3.Response responseCancelaEtiqueta = clientCancelaEtiqueta.newCall(requestCancelaEtiqueta).execute();

        return new ResponseEntity<String>(responseCancelaEtiqueta.body().string(), HttpStatus.OK);
    }
}