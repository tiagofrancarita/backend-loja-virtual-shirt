package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.AvaliacaoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {

    @Query("select ap from AvaliacaoProduto ap where upper(trim(ap.descricaoAvaliacaoProduto)) like %?1%")
    List<AvaliacaoProduto> buscarAvaliacaoProdutoPorDescricao(String descricaoAvaliacaoProduto);

    @Query("select ap from AvaliacaoProduto ap where ap.produto.id = ?1")
    List<AvaliacaoProduto> buscarAvaliacaoPorProduto(Long idProduto);

    @Query("select ap from AvaliacaoProduto ap where ap.empresa.id = ?1")
    List<AvaliacaoProduto> buscarAvaliacaoProdutoEmpresa(Long idEmpresa);

    @Query("select ap from AvaliacaoProduto ap where ap.pessoa.id = ?1")
    List<AvaliacaoProduto> buscarAvsaliacaoProdutoPorCliente(Long idCliente);

    @Query("select ap from AvaliacaoProduto ap where ap.produto.id = ?1 and ap.pessoa.id = ?2")
    List<AvaliacaoProduto> buscarAvaliacaoPorProdutoPessoa(Long idProduto, Long idCliente);

}