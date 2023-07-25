package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.AvaliacaoProduto;
import br.com.franca.ShirtVirtual.repository.AvaliacaoProdutoRepository;
import br.com.franca.ShirtVirtual.utils.dto.AvaliacaoProdutoDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
@Slf4j
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
            log.error("Erro ao buscar produto por nome, o nome informado não existe ou é inválido");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " nome: "  +  desc, HttpStatus.NOT_FOUND);
        }

        return modelMapper.map(avaliacaoProdutoDTOS, AvaliacaoProdutoDTO.class);

    }
}