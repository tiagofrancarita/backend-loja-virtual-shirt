package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.repository.ContaReceberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContaReceberService {

    private ContaReceberRepository contaReceberRepository;

    @Autowired
    public ContaReceberService(ContaReceberRepository contaReceberRepository) {
        this.contaReceberRepository = contaReceberRepository;
    }

}
