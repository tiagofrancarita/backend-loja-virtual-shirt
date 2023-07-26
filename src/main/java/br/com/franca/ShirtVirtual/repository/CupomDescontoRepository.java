package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.CupomDesonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface CupomDescontoRepository extends JpaRepository<CupomDesonto, Long> {



    @Query("select cupom from CupomDesonto cupom where upper(trim(cupom.codDesc)) like %?1%")
    List<CupomDesonto> buscarAcessoDescricao(String codDesc);
}
