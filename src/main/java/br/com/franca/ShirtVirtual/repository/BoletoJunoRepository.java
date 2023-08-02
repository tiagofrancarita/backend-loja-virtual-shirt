package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.BoletoJuno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletoJunoRepository extends JpaRepository<BoletoJuno, Long> {
}
