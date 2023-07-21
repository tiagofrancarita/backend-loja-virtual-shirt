package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.ContaPagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ContaPagarRepository extends JpaRepository<ContaPagar,Long> {

    @Query("select cp from ContaPagar cp where upper(trim(cp.descricao)) like %?1%")
    List<ContaPagar> buscarContaPagarDescricao(String descContaPagar);

    @Query("select cp from ContaPagar cp where cp.pessoa.id = ?1")
    List<ContaPagar> buscarContaPagarPorPessoa(Long idPessoa);

    @Query("select cp from ContaPagar cp where cp.pessoaFornecedor.id = ?1")
    List<ContaPagar> buscarContaPagarPorFornecedor(Long idFornecedor);

    @Query("select cp from ContaPagar cp where cp.empresa.id = ?1")
    List<ContaPagar> buscarContaPagarPorEmpresa(Long idEmpresa);
}
