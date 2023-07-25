package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.StatusRastreio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long> {


    @Query("select sr from StatusRastreio sr where sr.id = ?1")
    List<StatusRastreio> buscarStatusRastreioPorId(Long idStatusRastreio);
}
