package br.com.franca.ShirtVirtual.repository;


import br.com.franca.ShirtVirtual.model.FormaPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {

    @Query("select fp from FormaPagamento fp where upper(trim(fp.descFormaPagamento)) like %?1%")
    List<FormaPagamento> buscarFormaPagamentoDescricao(String descFormaPagamento);

}
