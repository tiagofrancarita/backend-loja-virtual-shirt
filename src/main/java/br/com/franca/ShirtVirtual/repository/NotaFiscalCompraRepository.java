package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.NotaFiscalCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotaFiscalCompraRepository extends JpaRepository<NotaFiscalCompra, Long> {


    @Query("SELECT notaFiscalCompra FROM NotaFiscalCompra notaFiscalCompra WHERE UPPER(TRIM(notaFiscalCompra.descricaoObs)) LIKE %?1%")
    List<NotaFiscalCompra> buscarNotaFiscalCompraDescricao(String descricaoObs);

    @Query("SELECT notaFiscalCompra FROM NotaFiscalCompra notaFiscalCompra WHERE notaFiscalCompra.id = ?1")
    List<NotaFiscalCompra> buscarNotaFiscalCompraPorId(Long idNotaFiscalCompra);

    @Query("SELECT notaFiscalCompra FROM NotaFiscalCompra notaFiscalCompra WHERE notaFiscalCompra.pessoa.id = ?1")
    List<NotaFiscalCompra> buscarNotaFiscalCompraPorPessoa(Long idPessoa);

    @Query("SELECT notaFiscalCompra FROM NotaFiscalCompra notaFiscalCompra WHERE notaFiscalCompra.empresa.id = ?1")
    List<NotaFiscalCompra> buscarNotaFiscalCompraPorEmpresa(Long idEmpresa);

    @Query("SELECT notaFiscalCompra FROM NotaFiscalCompra notaFiscalCompra WHERE notaFiscalCompra.contaPagar.id = ?1")
    List<NotaFiscalCompra> buscarNotaFiscalCompraPorContaPagar(Long idContaPagar);

    @Query("SELECT notaFiscalCompra FROM NotaFiscalCompra notaFiscalCompra WHERE UPPER(TRIM(notaFiscalCompra.numeroNota)) LIKE %?1%")
    List<NotaFiscalCompra> buscarNotaFiscalCompraNumeroNota(String numeroNota);

    @Query("SELECT notaFiscalCompra FROM NotaFiscalCompra notaFiscalCompra WHERE UPPER(TRIM(notaFiscalCompra.serieNota)) LIKE %?1%")
    List<NotaFiscalCompra> buscarNotaFiscalCompraSerieNota(String SerieNota);

    @Query("SELECT notaFiscalCompra FROM NotaFiscalCompra notaFiscalCompra WHERE UPPER(TRIM(notaFiscalCompra.pessoa.cnpj)) LIKE %?1%")
    List<NotaFiscalCompra> buscarNotaFiscalCompraCnpjPessoa(String cnpjPessoa);

    @Transactional
    @Modifying()
    @Query(nativeQuery = true, value = "SELECT COUNT(1) > 0  FROM nota_fiscal_compra WHERE descricaoObs LIKE %?1% ")
    boolean existeNotaFiscalCompraDescricao(String descricaoObs);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true, value = "DELETE FROM nota_item_produto WHERE nota_fiscal_compra_id = ?1")
    void deleteItemNotaFiscalCompra(Long idNotaFiscalCompra);

    //Data
   // @Query("select notaFiscalCompra from NotaFiscalCompra notaFiscalCompra where notaFiscalCompra. = ?1")
    //List<NotaFiscalCompra> buscarNotaFiscalCompraPorEmpresa(Long idEmpresa);


}