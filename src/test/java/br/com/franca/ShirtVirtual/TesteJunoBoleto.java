package br.com.franca.ShirtVirtual;

import br.com.franca.ShirtVirtual.service.ServiceJunoBoleto;
import br.com.franca.ShirtVirtual.utils.dto.CriarWebHook;
import br.com.franca.ShirtVirtual.utils.dto.ObjetoPostCarneJuno;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;


import junit.framework.TestCase;

@Profile("dev")
@SpringBootTest(classes = ShirtVirtualApplication.class)
public class TesteJunoBoleto extends TestCase {

    @Autowired
    private ServiceJunoBoleto serviceJunoBoleto;

    @Test
    public void testgerarCarneApiAsaas() throws Exception {

        ObjetoPostCarneJuno dados = new ObjetoPostCarneJuno();
        dados.setEmail("alex.fernando.egidio@gmail.com");
        dados.setPayerName("alex fernando egidio");
        dados.setPayerCpfCnpj("05916564937");
        dados.setPayerPhone("45999795800");
        dados.setIdVenda(15L);

        String retorno = serviceJunoBoleto.gerarCarneApiAsaas(dados);

        System.out.println(retorno);
    }


    @Test
    public void testcriarChavePixAsaas() throws Exception {

        String chaveAPi = serviceJunoBoleto.criarChavePixAsaas();

        System.out.println("Chave Asass API" + chaveAPi);
    }

    @Test
    public void testeBuscaCliente() throws Exception {

        ObjetoPostCarneJuno dados = new ObjetoPostCarneJuno();
        dados.setEmail("tiagofranca.rita@gmail.com");
        dados.setPayerName("Tiago França");
        dados.setPayerCpfCnpj("05916564937");
        dados.setPayerPhone("45999795800");

        String chaveAPi = serviceJunoBoleto.buscaClientePessoaApiAsaas(dados);


    }
}