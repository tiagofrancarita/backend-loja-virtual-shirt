package br.com.franca.ShirtVirtual;

import br.com.franca.ShirtVirtual.controller.PagamentoController;
import junit.framework.TestCase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import org.springframework.web.context.WebApplicationContext;


@Profile("test")
@SpringBootTest(classes = ShirtVirtualApplication.class)
public class TestePagamentoController extends TestCase {

    @Autowired
    private PagamentoController pagamentoController;

    @Autowired
    private WebApplicationContext wac;

    @Test
    public void testfinalizarCompraCartaoAsaas() throws Exception {

        pagamentoController.finalizarCompraCartaoAsaas("5292465982742455", "marcelo h almeida",
                "805", "06", "2024", 42L, "11807566730",
                3, "87025758", "Pioneiro antonio de ganello",
                "365", "PR", "Maringá");
    }













}
