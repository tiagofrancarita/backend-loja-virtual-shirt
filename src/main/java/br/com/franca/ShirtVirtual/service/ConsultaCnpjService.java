package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.utils.dto.CepDTO;
import br.com.franca.ShirtVirtual.utils.dto.ConsultaCnpjDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsultaCnpjService {

    public ConsultaCnpjDTO consultaCnpjReceitaWS(String cnpj){

        return new RestTemplate().getForEntity("https://receitaws.com.br/v1/cnpj/" + cnpj, ConsultaCnpjDTO.class).getBody();


    }
}
