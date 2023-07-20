package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.model.Usuario;
import br.com.franca.ShirtVirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    @Autowired
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findUserByLogin(username);

        if (usuario == null){
            throw new UsernameNotFoundException("Usuário(a) não encontrado");
        }

    return new User(usuario.getLogin(),usuario.getPassword(),usuario.getAuthorities());
    }
}