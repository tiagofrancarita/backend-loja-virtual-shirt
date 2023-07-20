package br.com.franca.ShirtVirtual.repository;


import br.com.franca.ShirtVirtual.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query(nativeQuery = true,
            value="SELECT COUNT(1) > 0 FROM  Produto WHERE upper(trim(nome)) = upper(trim(?1)) ;")
    public boolean existeProduto(String descricao);

    @Query(nativeQuery = true,
            value=" SELECT COUNT(1) > 0 FROM  Produto WHERE upper(trim(nome)) = upper(trim(?1)) and empresa_id = ?2 ;")
    public boolean existeProduto(String descricao, Long idEmpresa);

    @Query(nativeQuery = true,
            value=" SELECT * FROM  Produto WHERE upper(trim(descricao_produto)) LIKE %?1% ")
    public List<Produto> buscarPorDesc(String descricao);

    @Query(nativeQuery = true,
            value=" SELECT * FROM  Produto WHERE upper(trim(descricao_produto)) LIKE %?1% and empresa_id = ?2 ; ")
    public List<Produto> buscarProdutoNome(String descricao, Long idEmpresa);

}