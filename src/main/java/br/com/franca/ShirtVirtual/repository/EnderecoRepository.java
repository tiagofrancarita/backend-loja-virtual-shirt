package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {


}