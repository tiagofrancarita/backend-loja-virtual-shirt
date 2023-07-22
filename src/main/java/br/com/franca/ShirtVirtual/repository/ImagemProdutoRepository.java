package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.ImagemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ImagemProdutoRepository extends JpaRepository<ImagemProduto, Long> {

    @Query("SELECT ip FROM ImagemProduto ip WHERE ip.produto.id = ?1")
    List<ImagemProduto> buscarImagemProduto(Long idProduto);

    @Query(nativeQuery = true, value = "DELETE  FROM imagem_produto WHERE produto_id = ?1")
    void deletarImagensProduto(Long idProduto);









}