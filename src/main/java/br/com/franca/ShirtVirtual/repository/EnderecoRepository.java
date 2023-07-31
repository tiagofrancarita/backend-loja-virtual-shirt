package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    @Query(value = "select e from Endereco e where e.empresa.id = ?1")
    public List<Endereco> enderecoPj(Long idEmpresa);


}