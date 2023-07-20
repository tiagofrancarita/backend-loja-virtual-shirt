package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.utils.dto.CepDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConsultaCepService {

    public CepDTO consultaCEP(String cep){

        return new RestTemplate().getForEntity("https://viacep.com.br/ws/"+cep+"/json/",CepDTO.class).getBody();

    }
}
