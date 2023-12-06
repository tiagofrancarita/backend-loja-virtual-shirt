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
    public void testGeraCarneApiAsaas() throws Exception {

        ObjetoPostCarneJuno dados = new ObjetoPostCarneJuno();
        dados.setEmail("tiagofranca.ritaa@outlook.com");
        dados.setPayerName("Tiago França");
        dados.setPayerCpfCnpj("11807566730");
        dados.setPayerPhone("21964867990");
        dados.setIdVenda(35L);

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
        dados.setEmail("sandrastarita@gmail.com");
        dados.setPayerName("Sandra");
        dados.setPayerCpfCnpj("71115412787");
        dados.setPayerPhone("45999795800");

        String chaveAPi = serviceJunoBoleto.buscaClientePessoaApiAsaas(dados);


    }
}