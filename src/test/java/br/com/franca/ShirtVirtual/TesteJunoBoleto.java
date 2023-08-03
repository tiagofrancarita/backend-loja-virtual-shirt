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
}