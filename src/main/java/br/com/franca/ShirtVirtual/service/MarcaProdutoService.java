package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.model.MarcaProduto;
import br.com.franca.ShirtVirtual.repository.MarcaProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarcaProdutoService {

    private MarcaProdutoRepository marcaProdutoRepository;

    @Autowired
    public MarcaProdutoService(MarcaProdutoRepository marcaProdutoRepository) {
        this.marcaProdutoRepository = marcaProdutoRepository;
    }

    public MarcaProduto salvarMarca(MarcaProduto marcaProduto){
        return marcaProdutoRepository.save(marcaProduto);

    }
}