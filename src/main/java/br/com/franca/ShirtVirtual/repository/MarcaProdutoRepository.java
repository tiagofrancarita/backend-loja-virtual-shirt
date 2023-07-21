package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.model.MarcaProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MarcaProdutoRepository extends JpaRepository<MarcaProduto, Long> {


    @Query("select mp from MarcaProduto mp where upper(trim(mp.nomeDesc)) like %?1%")
    List<MarcaProduto> buscarMarcaProdutoDescricao(String descricao);











}