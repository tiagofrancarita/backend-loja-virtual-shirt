package br.com.franca.ShirtVirtual.repository;



import br.com.franca.ShirtVirtual.model.PessoaFisica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

    @Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
    public PessoaFisica existeCPF(String cpf);

    @Query(value = "select pf from PessoaFisica pf where pf.email = ?1")
    public PessoaFisica existeEmail(String email);

    @Query(value = "select pf from PessoaFisica pf where upper(trim(pf.nome)) like %?1%")
    public List<PessoaFisica> pesquisaPorNomePF(String nome);

    @Query(value = "select pf from PessoaFisica pf where pf.cpf= ?1 ")
    public List<PessoaFisica> pesquisaPorCPFPF(String cpf);

    @Query(value = "select pf from PessoaFisica pf where upper(trim(pf.email)) like %?1%")
    public List<PessoaFisica> pesquisaPorEmailPF(String cpf);


}
