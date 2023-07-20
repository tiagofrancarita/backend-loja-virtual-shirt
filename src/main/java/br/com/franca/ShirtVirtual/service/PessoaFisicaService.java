package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.model.PessoaFisica;
import br.com.franca.ShirtVirtual.model.Usuario;
import br.com.franca.ShirtVirtual.repository.PessoaFisicaRepository;
import br.com.franca.ShirtVirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Calendar;

@Service
public class PessoaFisicaService {

    private PessoaFisicaRepository pessoaFisicaRepository;
    private UsuarioRepository usuarioRepository;
    private JdbcTemplate jdbcTemplate;
    private ServiceSendEmail serviceSendEmail;

    @Autowired
    public PessoaFisicaService(PessoaFisicaRepository pessoaFisicaRepository, UsuarioRepository usuarioRepository, JdbcTemplate jdbcTemplate, ServiceSendEmail serviceSendEmail) {

        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.usuarioRepository = usuarioRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.serviceSendEmail = serviceSendEmail;
    }

    public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica){

        for (int i = 0; i< pessoaFisica.getEnderecos().size(); i++) {
            pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
           // pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
        }

        pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);

        Usuario usuarioPf = usuarioRepository.findByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());

        if (usuarioPf == null) {

            String constraint = usuarioRepository.consultaConstraintAcesso();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint +"; commit;");
            }

            usuarioPf = new Usuario();
            usuarioPf.setDataAtualSenha(Calendar.getInstance().getTime());
            usuarioPf.setEmpresa(pessoaFisica.getEmpresa());
            usuarioPf.setPessoa(pessoaFisica);
            usuarioPf.setLogin(pessoaFisica.getEmail());

            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCript = new BCryptPasswordEncoder().encode(senha);

            usuarioPf.setSenha(senhaCript);

            usuarioPf = usuarioRepository.save(usuarioPf);

            usuarioRepository.cadastroAcessoUserPF(usuarioPf.getId());

            StringBuilder menssagemHtml = new StringBuilder();

            menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>");
            menssagemHtml.append("<b>Login: </b>"+ pessoaFisica.getEmail()+"</b><br/>");
            menssagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
            menssagemHtml.append("Obrigado!");

            try {
                serviceSendEmail.enviaEmailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString() , pessoaFisica.getEmail());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pessoaFisica;
    }
}