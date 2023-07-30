package br.com.franca.ShirtVirtual.integracoesteste;


import br.com.franca.ShirtVirtual.config.tokens.ApiTokenMelhorEnvio;
import okhttp3.*;

import java.io.IOException;

public class ApiMelhorEnvio {
 /*
    public static void main(String[] args) throws  IOException {

        //Imprime etiqueta
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"99c30e62-3551-4e92-97ca-faa327b07e96\"]}");
        Request request = new Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT  + "/api/v2/me/shipment/print")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", ApiTokenMelhorEnvio.EMAIL)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());

        // Geração de etiquetas
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,  "{\"orders\":[\"99c30e62-3551-4e92-97ca-faa327b07e96\"]}");
        Request request = new Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT + "/api/v2/me/shipment/generate")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", "tiagofranca.rita@gmail.com")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());


        /*

        // Pagar Frete, primeiro precisa pagar o frete para depois gerar etiqueta

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"orders\":[\"99c30e62-3551-4e92-97ca-faa327b07e96\"]}");
        Request request = new Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT + "/api/v2/me/shipment/checkout")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", "tiagofranca.rita@gmail.com")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());

/*
        // Geração de etiquetas
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,  "{\"orders\":[\"99c31bed-44eb-44c9-a83d-697e10c4b189\"]}");
        Request request = new Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT + "/api/v2/me/shipment/generate")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", "tiagofranca.rita@gmail.com")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());

 */


    }
/*
        //  compra frete / etiqueta

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT + "/api/v2/me/cart/99c30e62-3551-4e92-97ca-faa327b07e96")
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", "tiagofranca.rita@gmail.com")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());




        /*  Inserir fretes no carrinho

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"from\":{\"name\":\"loja\",\"phone\":\"2133651237\",\"email\":\"tiago@email.com\",\"document\":\"11807566730\",\"company_document\":\"59623574000169\",\"state_register\":\"526802517793\",\"address\":\"rua do franca\",\"complement\":\"NA\",\"number\":\"7\",\"district\":\"Santa Cruz\",\"city\":\"Rio de Janeiro\",\"country_id\":\"BR\",\"postal_code\":\"23575901\",\"state_abbr\":\"RJ\",\"note\":\"teste\"},\"to\":{\"name\":\"francisco\",\"phone\":\"2133651238\",\"email\":\"fran.cisco@email.com\",\"document\":\"73678593070\",\"company_document\":\"07465342000126\",\"address\":\"rua teste\",\"city\":\"SP\",\"postal_code\":\"32160-050\",\"note\":\"teste\"},\"products\":[{\"name\":\"gelo\",\"quantity\":\"1\",\"unitary_value\":\"1\"}],\"volumes\":{\"height\":1,\"width\":1,\"length\":1,\"weight\":1},\"options\":{\"receipt\":true,\"own_hand\":true,\"reverse\":true,\"non_commercial\":true,\"tags\":{\"tag\":\"55\",\"Url\":\"55\"},\"insurance_value\":55,\"plataform\":\"55\"},\"service\":3,\"agency\":49}");
        Request request = new Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT + "/api/v2/me/cart")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+ ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", "tiagofranca.rita@gmail.com")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());


         */
