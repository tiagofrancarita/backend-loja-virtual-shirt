package br.com.franca.ShirtVirtual.integracoesteste;


public class ApiMelhorEnvio {
/*
    public static void main(String[] args) throws IOException {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,"{\n" +
                "\"from\":\n" +
                "{\"postal_code\":\"96020360\"},\n" +
                "\"to\":{\"postal_code\":\"01018020\"},\n" +
                "\"products\":[{\n" +
                "\"id\":\"1\",\"width\":16,\"height\":25,\"length\":11,\"weight\":0.3,\n" +
                "\"insurance_value\":55.05,\"quantity\":2,\"heitgh\":11,\"lenght\":11}],\n" +
                "\"options\":{\"receipt\":true,\"own_hand\":true}\n" +
                "}");
        Request request = new Request.Builder()
                .url(ApiTokenMelhorEnvio.URL_MELHOR_ENVIO_ENDPOINT + "/api/v2/me/shipment/calculate")
                .method("POST", body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + ApiTokenMelhorEnvio.TOKEN_MELHOR_ENVIO_AMBIENTE_TESTE)
                .addHeader("User-Agent", "tiagofranca.rita@gmail.com")
                .build();

        Response response = client.newCall(request).execute();
        //System.out.println(response.body().string());

        JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());

        Iterator<JsonNode> iterator = jsonNode.iterator();

        List<EmpresaTransporteDTO> apiMelhorEnvioDtos = new ArrayList<EmpresaTransporteDTO>();

        while (iterator.hasNext()){
            JsonNode node  = iterator.next();
            EmpresaTransporteDTO apiMelhorEnvioDto = new EmpresaTransporteDTO();

            if (node.get("id") != null){
                apiMelhorEnvioDto.setId(node.get("id").asText());
            }
            if (node.get("name") != null){
                apiMelhorEnvioDto.setNome(node.get("name").asText());
            }
            if (node.get("price") != null){
                apiMelhorEnvioDto.setValor(node.get("price").asText());
            }
            if (node.get("custom_price") != null){
                apiMelhorEnvioDto.setCustom_price(node.get("custom_price").asText());
            }
            if (node.get("discount") != null){
                apiMelhorEnvioDto.setDiscount(node.get("discount").asText());
            }
            if (node.get("company") != null){
                apiMelhorEnvioDto.setEmpresa(node.get("company").get("name").asText());
                apiMelhorEnvioDto.setPicture(node.get("company").get("picture").asText());
            }
            if (node.get("additional_services") != null){
                apiMelhorEnvioDto.setReceipt(node.get("additional_services").get("receipt").asText());
                apiMelhorEnvioDto.setOwn_hand(node.get("additional_services").get("own_hand").asText());
                apiMelhorEnvioDto.setCollect(node.get("additional_services").get("collect").asText());
            }

            if (apiMelhorEnvioDto.dadosOK()){
                apiMelhorEnvioDtos.add(apiMelhorEnvioDto);
            }
        }

            //System.out.println(apiMelhorEnvioDtos);

    } */
}

