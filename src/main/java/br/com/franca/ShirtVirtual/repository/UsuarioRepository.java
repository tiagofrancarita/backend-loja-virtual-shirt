package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT u FROM Usuario u WHERE u.login = ?1")
    Usuario findUserByLogin(String login);


    @Query(value = "SELECT u FROM Usuario u WHERE u.pessoa.id = ?1 or u.login = ?2")
    Usuario findByPessoa(Long id, String email);


    @Query(nativeQuery = true,value = "SELECT constraint_name FROM information_schema.constraint_column_usage WHERE table_name = 'usuarios_acesso' AND column_name = 'acesso_id' AND constraint_name <> 'unique_acesso_user' ")
    String consultaConstraintAcesso();


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO usuarios_acesso (usuario_id, acesso_id) VALUES (?1,(SELECT id FROM acesso WHERE descricao_acesso='ROLE_USER'))")
    void cadastroAcessoUserPF(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO usuarios_acesso (usuario_id, acesso_id) VALUES (?1,(SELECT id FROM acesso WHERE descricao_acesso = ?2 limit 1))")
    void cadastroAcessoUserPJ(Long id, String acesso);

    @Query(value = "SELECT u FROM Usuario u WHERE u.dataAtualSenha <= current_date  - 90")
    List<Usuario> listaUsuarioSenhaVencida();

}