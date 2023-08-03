package br.com.franca.ShirtVirtual;

import br.com.franca.ShirtVirtual.service.ServiceJunoBoleto;
import br.com.franca.ShirtVirtual.utils.dto.CriarWebHook;
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
    public void testeCriarWebHook() throws Exception {

        CriarWebHook criarWebHook = new CriarWebHook();
        criarWebHook.setUrl("https://shirt-prod.ck8zepbwckvp.us-east-1.rds.amazonaws.com/ShirtMultiMarcas/requisicaojunoboleto/notificacaoapiv2");
        criarWebHook.getEventTypes().add("PAYMENT_NOTIFICATION");
        criarWebHook.getEventTypes().add("BILL_PAYMENT_STATUS_CHANGED");

        String retorno = serviceJunoBoleto.criarWebHook(criarWebHook);

        System.out.println(retorno);
    }

    @Test
    public void deleteWebHook() throws Exception {

        serviceJunoBoleto.deleteWebHook("wbh_E71095B5BF65E8D2DB018EE8A89BACB8");

    }



    @Test
    public void listaWebHook() throws Exception {

        String retorno = serviceJunoBoleto.listaWebHook();
        System.out.println(retorno);
    }
}