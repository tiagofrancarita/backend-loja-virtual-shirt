package br.com.franca.ShirtVirtual;

import br.com.franca.ShirtVirtual.controller.PessoaFisicaController;
import br.com.franca.ShirtVirtual.enums.TipoEndereco;
import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Endereco;
import br.com.franca.ShirtVirtual.model.PessoaFisica;
import br.com.franca.ShirtVirtual.model.PessoaJuridica;
import br.com.franca.ShirtVirtual.repository.PessoaFisicaRepository;
import br.com.franca.ShirtVirtual.repository.PessoaJuridicaRepository;
import br.com.franca.ShirtVirtual.service.PessoaFisicaService;
import br.com.franca.ShirtVirtual.utils.GeradorCpfValido;
import io.swagger.models.auth.In;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Profile("test")
@SpringBootTest
public class FisicaTeste extends TestCase {

    String nome = "tiago_teste";
    Integer numero = 0;


    // Método executado antes de cada teste para inicializar os mocks
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private PessoaFisicaService pessoaFisicaService;
    private PessoaFisicaRepository pessoaFisicaRepository;
    private PessoaFisicaController pessoaFisicaController;
    private PessoaJuridicaRepository pessoaJuridicaRepository;
    private GeradorCpfValido geradorCpfValido;

    @Autowired
    public FisicaTeste(PessoaFisicaService pessoaFisicaService, PessoaFisicaRepository pessoaFisicaRepository, PessoaFisicaController pessoaFisicaController, PessoaJuridicaRepository pessoaJuridicaRepository, GeradorCpfValido geradorCpfValido) {
        this.pessoaFisicaService = pessoaFisicaService;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaFisicaController = pessoaFisicaController;
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.geradorCpfValido = geradorCpfValido;
    }

    @Test
    public void testeCadastroPessoaFisica() throws ExceptionShirtVirtual, ParseException {



        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse("07/06/1995");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PessoaJuridica pessoaJuridica = pessoaJuridicaRepository.existeCNPJ("1685820629792");

        String nome = "tiago_teste";
        Random gerador = new Random();
        Integer numero = gerador.nextInt(100) + 1;
        numero = (numero + Calendar.SECOND);
        PessoaFisica pessoaFisica  = new PessoaFisica();

        pessoaFisica.setCpf(geradorCpfValido.cpf(false));
        pessoaFisica.setEmail(nome + numero + "@hotmail.com");
        pessoaFisica.setTelefone("21964867990");
        pessoaFisica.setTipoPessoa("FISICA");
        pessoaFisica.setNome("França");
        pessoaFisica.setDataNascimento(date);
        pessoaFisica.setEmpresa(pessoaJuridica);



        Endereco endereco1 = new Endereco();
        endereco1.setBairro("Jd Dias");
        endereco1.setCep("556556565");
        endereco1.setComplemento("Casa cinza");
        endereco1.setEmpresa(pessoaJuridica);
        endereco1.setNumero("389");
        endereco1.setPessoa(pessoaFisica);
        endereco1.setLogradouro("Av. são joao sexto");
        endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
        endereco1.setUf("PR");
        endereco1.setCidade("Curitiba");
        endereco1.setEstado("Curitiba");


        Endereco endereco2 = new Endereco();
        endereco2.setBairro("Jd Maracana");
        endereco2.setCep("7878778");
        endereco2.setComplemento("Andar 4");
        endereco2.setEmpresa(pessoaJuridica);
        endereco2.setNumero("555");
        endereco2.setPessoa(pessoaFisica);
        endereco2.setLogradouro("Av. maringá");
        endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
        endereco2.setUf("PR");
        endereco2.setCidade("Curitiba");
        endereco2.setEstado("Curitiba");

        pessoaFisica.getEnderecos().add(endereco2);
        pessoaFisica.getEnderecos().add(endereco1);

        pessoaFisica = pessoaFisicaController.salvarPessoaFisica(pessoaFisica).getBody();

        assertEquals(true, pessoaFisica.getId() > 0 );

        for (Endereco endereco : pessoaFisica.getEnderecos()) {
            assertEquals(true, endereco.getId() > 0);
        }

        assertEquals(2, pessoaFisica.getEnderecos().size());

    }
}