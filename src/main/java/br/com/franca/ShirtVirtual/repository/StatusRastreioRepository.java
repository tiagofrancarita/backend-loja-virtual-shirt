package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.StatusRastreio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {


    @Query("select sr from StatusRastreio sr where sr.id = ?1")
    List<StatusRastreio> buscarStatusRastreioPorId(Long idStatusRastreio);

    @Query("select sr from StatusRastreio sr where sr.vendaCompraLojaVirtual.id = ?1")
    List<StatusRastreio> buscarStatusRastreioPorVenda(Long idVenda);

    @Query(value = "select s from StatusRastreio s where s.vendaCompraLojaVirtual.id = ?1 order by s.id")
    public List<StatusRastreio> listaRastreioVenda(Long idVenda);

    @Modifying(flushAutomatically = true)
    @Query(nativeQuery = true, value = "update vd_cp_loja_virt set url_rastreio = ?1 where id = ?2")
    public void salvaUrlRastreio(String urlRastreio, Long idVenda);
}
