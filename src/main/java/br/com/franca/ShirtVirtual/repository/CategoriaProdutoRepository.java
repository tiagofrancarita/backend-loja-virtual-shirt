package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.CategoriaProduto;
import br.com.franca.ShirtVirtual.model.ContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {

    @Query("select cat from CategoriaProduto cat where upper(trim(cat.nomeDesc)) like %?1%")
    List<CategoriaProduto> buscarCategoriaDescricao(String descricaoCategoria);

    @Query("SELECT categ from CategoriaProduto categ WHERE upper(trim(categ.nomeDesc)) like %?1%")
    public CategoriaProduto descricaoExistente(String descricaoCategoria);

    @Query(nativeQuery = true,
            value="SELECT COUNT(1) > 0 FROM  categoria_produto WHERE upper(descricao_categoria_produto) = ?1 ;")
    public boolean existeCategoria(String descricaoCategoria);

    @Query("select categ from CategoriaProduto categ where categ.id = ?1")
    public CategoriaProduto buscarCategoriaPorId(Long idCategoria);

}