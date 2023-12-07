package br.com.franca.ShirtVirtual;

import br.com.franca.ShirtVirtual.controller.PagamentoController;
import br.com.franca.ShirtVirtual.service.ServiceJunoBoleto;
import br.com.franca.ShirtVirtual.utils.dto.CriarWebHook;
import br.com.franca.ShirtVirtual.utils.dto.ObjetoPostCarneJuno;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;


import junit.framework.TestCase;
import org.springframework.web.context.WebApplicationContext;

@Profile("dev")
@SpringBootTest(classes = ShirtVirtualApplication.class)
public class TesteJunoBoleto extends TestCase {

    @Autowired
    private ServiceJunoBoleto serviceJunoBoleto;

    @Autowired
    private PagamentoController pagamentoController;

    @Autowired
    private WebApplicationContext wac;


    @org.junit.Test
    public void testfinalizarCompraCartaoAsaas() throws Exception {

        pagamentoController.finalizarCompraCartaoAsaas("5292465982742455", "marcelo h almeida",
                "805", "06", "2024", 34L, "11807566730",
                2, "87025758", "Pioneiro antonio de ganello",
                "365", "PR", "Maringá");
    }

    @Test
    public void testGeraCarneApiAsaas() throws Exception {

        ObjetoPostCarneJuno dados = new ObjetoPostCarneJuno();
        dados.setEmail("joaomendes@outlook.com");
        dados.setPayerName("Joaoa Mendes");
        dados.setPayerCpfCnpj("98819321734");
        dados.setPayerPhone("21964867990");
        dados.setIdVenda(37L);

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