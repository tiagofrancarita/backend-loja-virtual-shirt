package br.com.franca.ShirtVirtual;

import br.com.franca.ShirtVirtual.controller.CategoriaProdutoController;
import br.com.franca.ShirtVirtual.model.CategoriaProduto;
import br.com.franca.ShirtVirtual.model.PessoaJuridica;
import br.com.franca.ShirtVirtual.repository.CategoriaProdutoRepository;
import static org.junit.jupiter.api.Assertions.*;
import br.com.franca.ShirtVirtual.repository.PessoaJuridicaRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

public class CategoriaProdutoTest {

    @InjectMocks
    private CategoriaProdutoController categoriaProdutoController;

    @Mock
    private CategoriaProdutoRepository categoriaProdutoRepository;
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    // Método executado antes de cada teste para inicializar os mocks
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastrarCategoria() {
        // Dados do exemplo
        String nomeCategoria = "Eletrônicos";
        CategoriaProduto categoria = new CategoriaProduto();
        categoria.setNomeDesc(nomeCategoria);

        // Quando o método save() do repositório for chamado, retorne a própria categoria
        when(categoriaProdutoRepository.save(any(CategoriaProduto.class))).thenReturn(categoria);

        // Executa o método de cadastro de categoria
        CategoriaProduto categoriaCadastrada = categoriaProdutoRepository.save(categoria);

        // Verifica se o resultado do cadastro é igual à categoria que foi salva no repositório
        assertEquals(categoria, categoriaCadastrada);
    }

    @Test
    public void testBuscarCategoriaPorId() {

        Long idCategoria = 1L;
        String nomeCategoria = "Informatica";

        Long idEmpresa = 1L;
        String nomeEmpresa = "Notebook";

        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setId(idEmpresa);
        pessoaJuridica.setNome(nomeEmpresa);

        CategoriaProduto categoria = new CategoriaProduto();
        categoria.setId(idCategoria);
        categoria.setNomeDesc(nomeCategoria);
        categoria.setEmpresa(pessoaJuridica);

        Mockito.when(categoriaProdutoRepository.buscarCategoriaPorId(idCategoria)).thenReturn(categoria);

        // Quando
        CategoriaProduto resultCat = categoriaProdutoRepository.buscarCategoriaPorId(idCategoria);

        // Então
        Assert.assertNotNull(resultCat);
        Assert.assertEquals(idCategoria, resultCat.getId());
        Assert.assertEquals(nomeCategoria, resultCat.getNomeDesc());
        Assert.assertEquals(idEmpresa, resultCat.getEmpresa().getId());

    }
}
