package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.AvaliacaoProduto;
import br.com.franca.ShirtVirtual.repository.AvaliacaoProdutoRepository;
import br.com.franca.ShirtVirtual.utils.dto.AvaliacaoProdutoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoProdutoService {

    private final AvaliacaoProdutoRepository avaliacaoProdutoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AvaliacaoProdutoService(AvaliacaoProdutoRepository avaliacaoProdutoRepository, ModelMapper modelMapper) {
        this.avaliacaoProdutoRepository = avaliacaoProdutoRepository;
        this.modelMapper = modelMapper;
    }

    public AvaliacaoProdutoDTO buscarAvaliacaoPorId(Long id) {
        AvaliacaoProduto avaliacaoProduto = avaliacaoProdutoRepository.findById(id).orElse(null);

        if (avaliacaoProduto != null) {
            return modelMapper.map(avaliacaoProduto, AvaliacaoProdutoDTO.class);
        }

        return null;
    }

    public AvaliacaoProdutoDTO buscarAvaliacaoProdutoPorDescricao(String desc) throws ExceptionShirtVirtual {

        List<AvaliacaoProduto> avaliacaoProdutoDTOS = avaliacaoProdutoRepository.buscarAvaliacaoProdutoPorDescricao(desc.toUpperCase().trim());

        if (avaliacaoProdutoDTOS == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " nome: "  +  desc);
        }

        return modelMapper.map(avaliacaoProdutoDTOS, AvaliacaoProdutoDTO.class);

    }
}