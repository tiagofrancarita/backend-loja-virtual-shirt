package br.com.franca.ShirtVirtual.repository;


import br.com.franca.ShirtVirtual.model.CategoriaProduto;
import br.com.franca.ShirtVirtual.model.PessoaFisica;
import br.com.franca.ShirtVirtual.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Repository
@Transactional
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {

    @Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
    public PessoaJuridica existeCNPJ(String cnpj);

    @Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
    public PessoaJuridica existeInscricaoEstadual(String inscEstadual);

    @Query(value = "select pj from PessoaJuridica pj where pj.inscricaoMunicipal = ?1")
    public PessoaJuridica existeInscricaoMunincipal(String inscricaoMunicipal);


    @Query(value = "select pj from PessoaJuridica pj where (trim(pj.nome)) like %?1%")
    public List<PessoaJuridica> pesquisaPorNomePJ(String nome);

    @Query(value = "select pj from PessoaJuridica pj where pj.cnpj= ?1 ")
    public List<PessoaJuridica> pesquisaPorCnpjPj(String cnpj);

    @Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.email)) like %?1%")
    public List<PessoaJuridica> pesquisaPorEmailPj(String email);

    @Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.categoria)) like %?1%")
    public List<PessoaJuridica> pesquisaPorCategoriaPj(String categoria);


}
