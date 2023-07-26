package br.com.franca.ShirtVirtual;

import br.com.franca.ShirtVirtual.controller.PessoaJuridicaController;
import br.com.franca.ShirtVirtual.enums.TipoEndereco;
import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Endereco;
import br.com.franca.ShirtVirtual.model.PessoaJuridica;
import br.com.franca.ShirtVirtual.repository.PessoaFisicaRepository;
import br.com.franca.ShirtVirtual.repository.PessoaJuridicaRepository;
import br.com.franca.ShirtVirtual.service.PessoaFisicaService;
import br.com.franca.ShirtVirtual.service.PessoaJuridicaService;
import br.com.franca.ShirtVirtual.utils.GeradorCnpjValido;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Calendar;

@Profile("test")
@SpringBootTest
public class JuridicaTest extends TestCase {

    private PessoaFisicaService pessoaFisicaService;
    private PessoaFisicaRepository pessoaFisicaRepository;
    private PessoaJuridicaRepository pessoaJuridicaRepository;
    private PessoaJuridicaService pessoaJuridicaService;
    private PessoaJuridicaController pessoaJuridicaController;
    private GeradorCnpjValido geradorCnpjValido;


    @Autowired
    public JuridicaTest(PessoaFisicaService pessoaFisicaService, PessoaFisicaRepository pessoaFisicaRepository, PessoaJuridicaRepository pessoaJuridicaRepository, PessoaJuridicaService pessoaJuridicaService, PessoaJuridicaController pessoaJuridicaController, GeradorCnpjValido geradorCnpjValido) {
        this.pessoaFisicaService = pessoaFisicaService;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.pessoaJuridicaService = pessoaJuridicaService;
        this.pessoaJuridicaController = pessoaJuridicaController;

        this.geradorCnpjValido = geradorCnpjValido;
    }

    @Test
    public void testeCadastroPessoaJuridica() throws ExceptionShirtVirtual {

        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setCnpj(geradorCnpjValido.cnpj(false));
        pessoaJuridica.setNome("Tiago");
        pessoaJuridica.setEmail("tiago.franca.dev1@outlook.com");
        pessoaJuridica.setTelefone("459997958001");
        pessoaJuridica.setInscEstadual("" + Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setInscricaoMunicipal("" + Calendar.getInstance().getTimeInMillis());
        pessoaJuridica.setNomeFantasia("Teste nome fantaisa");
        pessoaJuridica.setRazaoSocial("121212");
        pessoaJuridica.setCategoria("Informaticax");
        pessoaJuridica.setTipoPessoa("Juridico");

        Endereco endereco1 = new Endereco();
        endereco1.setBairro("Jd Dias");
        endereco1.setCep("23575901");
        endereco1.setComplemento("Casa cinza");
        endereco1.setEmpresa(pessoaJuridica);
        endereco1.setNumero("389");
        endereco1.setPessoa(pessoaJuridica);
        endereco1.setLogradouro("Av. são joao sexto");
        endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
        endereco1.setUf("PR");
        endereco1.setCidade("Curitiba");
        endereco1.setEstado("Curitiba");


        Endereco endereco2 = new Endereco();
        endereco2.setBairro("Jd Maracana");
        endereco2.setCep("23575901");
        endereco2.setComplemento("Andar 4");
        endereco2.setEmpresa(pessoaJuridica);
        endereco2.setNumero("555");
        endereco2.setPessoa(pessoaJuridica);
        endereco2.setLogradouro("Av. maringá");
        endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
        endereco2.setUf("PR");
        endereco2.setCidade("Curitiba");
        endereco2.setEstado("Curitiba");

        pessoaJuridica.getEnderecos().add(endereco2);
        pessoaJuridica.getEnderecos().add(endereco1);

        pessoaJuridica = pessoaJuridicaController.salvarPessoaJuridica(pessoaJuridica).getBody();

        assertEquals(true, pessoaJuridica.getId() > 0 );

        for (Endereco endereco : pessoaJuridica.getEnderecos()) {
            assertEquals(true, endereco.getId() > 0);
        }

        assertEquals(2, pessoaJuridica.getEnderecos().size());

    }
}