package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.repository.AcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcessoService {

    private AcessoRepository acessoRepository;

    @Autowired
    public AcessoService(AcessoRepository acessoRepository) {
        this.acessoRepository = acessoRepository;
    }

    public Acesso salvarAcesso(Acesso acesso){

        //Validações

        return acessoRepository.save(acesso);

    }
}
