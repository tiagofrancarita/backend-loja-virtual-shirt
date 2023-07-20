package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.model.PessoaJuridica;
import br.com.franca.ShirtVirtual.model.Usuario;
import br.com.franca.ShirtVirtual.repository.PessoaJuridicaRepository;
import br.com.franca.ShirtVirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Calendar;

@Service
public class PessoaJuridicaService {

    private PessoaJuridicaRepository pessoaJuridicaRepository;
    private UsuarioRepository usuarioRepository;
    private JdbcTemplate jdbcTemplate;
    private ServiceSendEmail serviceSendEmail;

    @Autowired
    public PessoaJuridicaService(PessoaJuridicaRepository pessoaJuridicaRepository, UsuarioRepository usuarioRepository, JdbcTemplate jdbcTemplate, ServiceSendEmail serviceSendEmail) {
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.usuarioRepository = usuarioRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.serviceSendEmail = serviceSendEmail;
    }

    public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica){

        for (int i = 0; i< pessoaJuridica.getEnderecos().size(); i++) {
            pessoaJuridica.getEnderecos().get(i).setPessoa(pessoaJuridica);
            pessoaJuridica.getEnderecos().get(i).setEmpresa(pessoaJuridica);
        }

        pessoaJuridica = pessoaJuridicaRepository.save(pessoaJuridica);

        Usuario usuarioPj = usuarioRepository.findByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail());

        if (usuarioPj == null) {

            String constraint = usuarioRepository.consultaConstraintAcesso();
            if (constraint != null) {
                jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint +"; commit;");
            }

            usuarioPj = new Usuario();
            usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
            usuarioPj.setEmpresa(pessoaJuridica);
            usuarioPj.setPessoa(pessoaJuridica);
            usuarioPj.setLogin(pessoaJuridica.getEmail());

            String senha = "" + Calendar.getInstance().getTimeInMillis();
            String senhaCript = new BCryptPasswordEncoder().encode(senha);

            usuarioPj.setSenha(senhaCript);

            usuarioPj = usuarioRepository.save(usuarioPj);

            //usuarioRepository.cadastroAcessoUserPJ(usuarioPj.getId());
            usuarioRepository.cadastroAcessoUserPJ(usuarioPj.getId(), "ROLE_ADMIN");

            StringBuilder menssagemHtml = new StringBuilder();

            menssagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>");
            menssagemHtml.append("<b>Login: </b>"+pessoaJuridica.getEmail()+"</b><br/>");
            menssagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
            menssagemHtml.append("Obrigado!");

            try {
                serviceSendEmail.enviaEmailHtml("Acesso Gerado para Loja Virtual", menssagemHtml.toString() , pessoaJuridica.getEmail());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pessoaJuridica;
    }
}