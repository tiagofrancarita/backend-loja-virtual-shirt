package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.config.tokens.ApiTokenMelhorEnvio;
import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Endereco;
import br.com.franca.ShirtVirtual.model.ItemVendaLoja;
import br.com.franca.ShirtVirtual.model.VendaCompraLojaVirtual;
import br.com.franca.ShirtVirtual.repository.EnderecoRepository;
import br.com.franca.ShirtVirtual.repository.VendaCompraLojaVirtualRepository;
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

    @Autowired
    public ApiIntegracaoMelhorEnvioController(VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository, EnderecoRepository enderecoRepository) {
        this.vendaCompraLojaVirtualRepository = vendaCompraLojaVirtualRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @ApiOperation("Listagem de todos os acessos cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = AcessoController.class),
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

    @ResponseBody
    @PostMapping(value = "/imprimeCompraEtiquetaFrete/{idVenda}")
    public ResponseEntity<String> imprimeCompraEtiquetaFrete(@PathVariable Long idVenda) throws ExceptionShirtVirtual, IOException {

        VendaCompraLojaVirtual vendaCompraLojaVirtual = vendaCompraLojaVirtualRepository.findById(idVenda).orElseGet(null);

        if (vendaCompraLojaVirtual == null){

            return new ResponseEntity<String>("Venda não encontrada na base de dados", HttpStatus.NOT_FOUND);
        }

        List<Endereco> enderecos = enderecoRepository.enderecoPj(vendaCompraLojaVirtual.getEmpresa().getId());
        vendaCompraLojaVirtual.getEmpresa().setEnderecos(enderecos);

        FreteDTO freteDTO = new FreteDTO();
        freteDTO.setAgency("1");
        freteDTO.setService(vendaCompraLojaVirtual.getServicoTransportadora());

        freteDTO.getFrom().setName(vendaCompraLojaVirtual.getEmpresa().getNome());
        freteDTO.getFrom().setPhone(vendaCompraLojaVirtual.getEmpresa().getTelefone());
        freteDTO.getFrom().setEmail(vendaCompraLojaVirtual.getEmpresa().getEmail());
        freteDTO.getFrom().setCompany_document(vendaCompraLojaVirtual.getEmpresa().getCnpj());
        freteDTO.getFrom().setDocument(vendaCompraLojaVirtual.getEmpresa().getCnpj());
        freteDTO.getFrom().setState_register(vendaCompraLojaVirtual.getEmpresa().getInscEstadual());

        freteDTO.getFrom().setAddress(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getLogradouro());
        freteDTO.getFrom().setComplement(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getComplemento());
        freteDTO.getFrom().setLocation_number(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getNumero());
        freteDTO.getFrom().setDistrict(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getEstado());
        freteDTO.getFrom().setCity(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getCidade());
        freteDTO.getFrom().setCountry_id(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getUf());
        freteDTO.getFrom().setPostal_code(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getCep());
        freteDTO.getFrom().setState_abbr(vendaCompraLojaVirtual.getEmpresa().getEnderecos().get(0).getUf());
        freteDTO.getFrom().setNote("NOTA");

        freteDTO.getTo().setName(vendaCompraLojaVirtual.getPessoa().getNome());
        freteDTO.getTo().setPhone(vendaCompraLojaVirtual.getPessoa().getTelefone());
        freteDTO.getTo().setEmail(vendaCompraLojaVirtual.getPessoa().getEmail());
        freteDTO.getTo().setCompany_document(vendaCompraLojaVirtual.getPessoa().getCpf());
        freteDTO.getTo().setState_register(vendaCompraLojaVirtual.getPessoa().getCpf());
        freteDTO.getTo().setDocument(vendaCompraLojaVirtual.getPessoa().getCpf());

        freteDTO.getTo().setAddress(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getLogradouro());
        freteDTO.getTo().setComplement(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getComplemento());
        freteDTO.getTo().setLocation_number(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getNumero());
        freteDTO.getTo().setDistrict(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getBairro());
        freteDTO.getTo().setCity(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getCidade());
        freteDTO.getTo().setPostal_code(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getCep());
        freteDTO.getTo().setState_abbr(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getUf());
        freteDTO.getTo().setCountry_id(vendaCompraLojaVirtual.getPessoa().enderecoEntrega().getEstado());
        freteDTO.getFrom().setNote("NOTA");


        List<ProductsEnvioFreteDTO> products = new ArrayList<ProductsEnvioFreteDTO>();

        for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItemVendaLojas()){
            ProductsEnvioFreteDTO productsEnvDTO = new ProductsEnvioFreteDTO();

            productsEnvDTO.setName(itemVendaLoja.getProduto().getNome());
            productsEnvDTO.setQuantity(itemVendaLoja.getQuantidade().toString());
            productsEnvDTO.setUnitary_value("" + itemVendaLoja.getProduto().getValorTotalVenda().doubleValue());
            products.add(productsEnvDTO);
        }

        freteDTO.setProducts(products);

        List<VolumeEnvioFreteDTO> volumes = new ArrayList<VolumeEnvioFreteDTO>();
        for (ItemVendaLoja itemVendaLoja : vendaCompraLojaVirtual.getItemVendaLojas()) {
            VolumeEnvioFreteDTO volumeEnvDTO = new VolumeEnvioFreteDTO();
            volumeEnvDTO.setHeight(itemVendaLoja.getProduto().getAltura().toString());
            volumeEnvDTO.setLength(itemVendaLoja.getProduto().getProfundidade().toString());
            volumeEnvDTO.setWeight(itemVendaLoja.getProduto().getPeso().toString());
            volumeEnvDTO.setWidth(itemVendaLoja.getProduto().getLargura().toString());
            volumes.add(volumeEnvDTO);
        }

        freteDTO.setVolumes(volumes);

        freteDTO.getOptions().setInsurance_value("" + vendaCompraLojaVirtual.getValorTotalVendaLoja().doubleValue());
        freteDTO.getOptions().setReceipt("false");
        freteDTO.getOptions().setOwn_hand("false");
        freteDTO.getOptions().setReverse("false");
        freteDTO.getOptions().setNon_commercial("false");
        freteDTO.getOptions().getInvoice().setKey(vendaCompraLojaVirtual.getNotaFiscalVenda().getNumeroNotaFiscalVenda());
        freteDTO.getOptions().setPlatform(vendaCompraLojaVirtual.getEmpresa().getNomeFantasia());


        TagsEnvioFreteDTO tagsEnvioFreteDTO = new TagsEnvioFreteDTO();
        tagsEnvioFreteDTO.setTag(" Pedido: " + vendaCompraLojaVirtual.getNotaFiscalVenda().getId());
        tagsEnvioFreteDTO.setUrl(null);

        freteDTO.getOptions().getTags().add(tagsEnvioFreteDTO);


        String jsonEnvioFreteDTO = new ObjectMapper().writeValueAsString(freteDTO);


        OkHttpClient client = new OkHttpClient().newBuilder().build();

        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, jsonEnvioFreteDTO);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT + "/api/v2/me/cart")
                .method("POST",body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+ ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", ApiTokenMelhorEnvio.EMAIL)
                .build();

        okhttp3.Response response = client.newCall(request).execute();

        JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());

        Iterator<JsonNode> iterator = jsonNode.iterator();

        String idEtiqueta = "";

        while(iterator.hasNext()) {
            JsonNode node = iterator.next();
            idEtiqueta = node.get("id").asText();
            break;
        }

        //vendaCompraLojaVirtual.setCodigoFrete(idEtiqueta);
        vendaCompraLojaVirtualRepository.updateEtiqueta(idEtiqueta, vendaCompraLojaVirtual.getId());

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

        vendaCompraLojaVirtualRepository.updateURLEtiqueta(urlEtiqueta, vendaCompraLojaVirtual.getId());

        return new ResponseEntity<String>("Sucesso", HttpStatus.OK);

    }

}