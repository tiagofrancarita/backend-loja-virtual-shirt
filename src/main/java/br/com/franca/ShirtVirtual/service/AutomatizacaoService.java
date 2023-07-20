package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.model.Usuario;
import br.com.franca.ShirtVirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class AutomatizacaoService {

    private UsuarioRepository  usuarioRepository;
    private ServiceSendEmail serviceSendEmail;

    @Autowired
    public AutomatizacaoService(UsuarioRepository usuarioRepository, ServiceSendEmail serviceSendEmail) {
        this.usuarioRepository = usuarioRepository;
        this.serviceSendEmail = serviceSendEmail;
    }

    /*
        Automatização para envio de e-mail para o usuário que a data atual de
        senha esteja cadastrada a mais de 90 dias.
        */
    @Scheduled(initialDelay = 2000, fixedDelay = 86400000)
    //@Scheduled(cron = "* 15 16 * * *",zone = "America/Sao_Paulo")
    public void notificaUsuarioMaior90Dias() throws MessagingException, UnsupportedEncodingException, InterruptedException {

        List<Usuario> usuarios = usuarioRepository.listaUsuarioSenhaVencida();

        for (Usuario usuario : usuarios){

            StringBuilder msg = new StringBuilder();
            msg.append("Olá ").append(usuario.getPessoa().getNome()).append("<br/>");
            msg.append("Necessário realizar a troca da sua senha pois a mesma expirou.");
            msg.append("Loja Virtual Xpto");

            serviceSendEmail.enviaEmailHtml("Senha Expirada",msg.toString(),usuario.getLogin());

            Thread.sleep(3000);

        }
    }
}