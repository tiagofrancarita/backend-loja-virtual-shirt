package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.ContaPagar;
import br.com.franca.ShirtVirtual.model.ContaReceber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ContaReceberRepository extends JpaRepository<ContaReceber,Long> {

    @Query("select cr from ContaReceber cr where upper(trim(cr.descricao)) like %?1%")
    List<ContaReceber> buscarContaReceberDescricao(String descContaReceber);

    @Query("select cr from ContaReceber cr where cr.pessoa.id = ?1")
    List<ContaReceber> buscarContaReceberPorPessoa(Long idPessoa);

    @Query("select cr from ContaReceber cr where cr.empresa.id = ?1")
    List<ContaReceber> buscarContaReceberPorEmpresa(Long idEmpresa);

}
