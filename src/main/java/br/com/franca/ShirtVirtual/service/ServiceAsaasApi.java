package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.config.tokens.ApiTokenAsass;
import br.com.franca.ShirtVirtual.config.tokens.AsaasApiPagamentoStatus;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class ServiceAsaasApi implements Serializable {

    private static final long serialVersionUID = 1L;

    private ApiTokenAsass apiTokenAsass;
    private AsaasApiPagamentoStatus asaasApiPagamentoStatus;

    @Autowired
    public ServiceAsaasApi(ApiTokenAsass apiTokenAsass, AsaasApiPagamentoStatus asaasApiPagamentoStatus) {
        this.apiTokenAsass = apiTokenAsass;
        this.asaasApiPagamentoStatus = asaasApiPagamentoStatus;
    }

    /**
     * Cria a chave da API Asass para o PIX;
     * @return Chave
     */
    /**
     * Cria a chave da API Asass para o PIX;
     * @return Chave
     */
    public String criarChavePixAsaas() throws Exception {

        Client client = new HostIgnoringClient(AsaasApiPagamentoStatus.URL_API_ASAAS).hostIgnoringClient();
        WebResource webResource = client.resource(AsaasApiPagamentoStatus.URL_API_ASAAS + "pix/addressKeys");

        ClientResponse clientResponse = webResource.accept("application/json;charset=UTF-8")
                .header("Content-Type", "application/json")
                .header("access_token", AsaasApiPagamentoStatus.API_KEY)
                .post(ClientResponse.class, "{\"type\":\"EVP\"}");

        String strinRetorno = clientResponse.getEntity(String.class);
        clientResponse.close();
        return strinRetorno;

    }

}
