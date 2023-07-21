package br.com.franca.ShirtVirtual.repository;


import br.com.franca.ShirtVirtual.model.NotaItemProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto, Long> {

    @Query("SELECT notaItemProduto FROM NotaItemProduto notaItemProduto WHERE notaItemProduto.produto.id = ?1")
    List<NotaItemProduto> buscaNotaItemPorProduto(Long idProduto);

    @Query("SELECT notaItemProduto from NotaItemProduto notaItemProduto WHERE notaItemProduto.produto.id = ?1 AND notaItemProduto.notaFiscalCompra.id = ?1")
    List<NotaItemProduto> buscarNotaItemPorProdutoNota(Long idProduto, Long idNotaFiscal);

    @Query("SELECT notaItemProduto from NotaItemProduto notaItemProduto WHERE notaItemProduto.notaFiscalCompra.id = ?1")
    List<NotaItemProduto> buscarNotaItemPorNotaFiscal(Long idNotaFiscal);

    @Query("SELECT notaItemProduto from NotaItemProduto notaItemProduto WHERE notaItemProduto.empresa.id = ?1")
    List<NotaItemProduto> buscarNotaItemPorEmpresa(Long idEmpresa);
}
