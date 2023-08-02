package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.AccessTokenJunoAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AccesTokenJunoRepository extends JpaRepository<AccessTokenJunoAPI, Long> {
}
