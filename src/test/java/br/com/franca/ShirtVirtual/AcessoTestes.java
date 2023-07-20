package br.com.franca.ShirtVirtual;

import br.com.franca.ShirtVirtual.controller.AcessoController;
import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.repository.AcessoRepository;
import br.com.franca.ShirtVirtual.service.AcessoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Calendar;
import java.util.List;

@SpringBootTest
class AcessoTestes extends TestCase {


    private AcessoRepository acessoRepository;
    private AcessoService acessoService;
    private AcessoController acessoController;
    private WebApplicationContext wac;

    @Autowired
    public AcessoTestes(AcessoController acessoController,AcessoRepository acessoRepository,AcessoService acessoService,WebApplicationContext wac) {

        this.acessoController = acessoController;
        this.acessoRepository = acessoRepository;
        this.acessoService = acessoService;
        this.wac = wac;
    }

    @Test
    public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {

        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();

        Acesso acesso = new Acesso();
        acesso.setDescAcesso("ROLE_COMPRADOR" + Calendar.getInstance().getTimeInMillis());
        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions retornoApi = mockMvc
                .perform(MockMvcRequestBuilders.post("/acessos/cadastrarAcesso")
                        .content(objectMapper.writeValueAsString(acesso))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());

        Acesso objetoRetorno = objectMapper.
                readValue(retornoApi.andReturn().getResponse().getContentAsString(),
                        Acesso.class);

        assertEquals(acesso.getDescAcesso(), objetoRetorno.getDescAcesso());
    }
    @Test
    public void testRestApiDeleteAcesso() throws JsonProcessingException, Exception {

        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();

        Acesso acesso = new Acesso();
        acesso.setDescAcesso("ROLE_TESTE_DELETE" + Calendar.getInstance().getTimeInMillis());
        acesso = acessoRepository.save(acesso);

        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions retornoApi = mockMvc
                .perform(MockMvcRequestBuilders.delete("/acessos/deletarAcesso")
                        .content(objectMapper.writeValueAsString(acesso))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
        System.out.println("Status de retorno: " + retornoApi.andReturn().getResponse().getStatus());

        assertEquals("Acesso removido com sucesso", retornoApi.andReturn().getResponse().getContentAsString());
        assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
    }
    @Test
    public void testRestApiDeletePorIDAcesso() throws JsonProcessingException, Exception {

        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();

        Acesso acesso = new Acesso();
        acesso.setDescAcesso("ROLE_TESTE_DELETE_ID" + Calendar.getInstance().getTimeInMillis());
        acesso = acessoRepository.save(acesso);

        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions retornoApi = mockMvc
                .perform(MockMvcRequestBuilders.delete("/acessos/deleteAcessoPorId/" + acesso.getId())
                        .content(objectMapper.writeValueAsString(acesso))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        System.out.println("Retorno da API: " + retornoApi.andReturn().getResponse().getContentAsString());
        System.out.println("Status de retorno: " + retornoApi.andReturn().getResponse().getStatus());

        assertEquals("Acesso removido com sucesso", retornoApi.andReturn().getResponse().getContentAsString());
        assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
    }
    @Test
    public void testRestApiObterAcessoID() throws JsonProcessingException, Exception {

        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();

        Acesso acesso = new Acesso();
        acesso.setDescAcesso("ROLE_OBTER_ID" + Calendar.getInstance().getTimeInMillis());
        acesso = acessoRepository.save(acesso);

        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions retornoApi = mockMvc
                .perform(MockMvcRequestBuilders.get("/acessos/buscarAcessoPorId/" + acesso.getId())
                        .content(objectMapper.writeValueAsString(acesso))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        assertEquals(200, retornoApi.andReturn().getResponse().getStatus());

        Acesso acessoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);

        assertEquals(acesso.getDescAcesso(), acessoRetorno.getDescAcesso());
        assertEquals(acesso.getId(), acessoRetorno.getId());
    }
    @Test
    public void testRestApiObterAcessoDesc() throws JsonProcessingException, Exception {

        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        MockMvc mockMvc = builder.build();

        Acesso acesso = new Acesso();
        acesso.setDescAcesso("ROLE_TESTE_OBTER_LIST" + Calendar.getInstance().getTimeInMillis());
        acesso = acessoRepository.save(acesso);

        ObjectMapper objectMapper = new ObjectMapper();
        ResultActions retornoApi = mockMvc
                .perform(MockMvcRequestBuilders.get("/acessos/buscarPorDesc/OBTER_LIST")
                        .content(objectMapper.writeValueAsString(acesso))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));

        assertEquals(200, retornoApi.andReturn().getResponse().getStatus());

        List<Acesso> retornoApiList = objectMapper.
                readValue(retornoApi.andReturn()
                                .getResponse().getContentAsString(), new TypeReference<List<Acesso>>() {});

        assertEquals(1, retornoApiList.size());
        assertEquals(acesso.getDescAcesso(), retornoApiList.get(0).getDescAcesso());
        acessoRepository.deleteById(acesso.getId());
    }

    @Test
    public void testeCadastrarAcesso() throws JsonProcessingException, Exception {

        Acesso acesso = new Acesso();
        acesso.setDescAcesso("ROLE_ADMIN_TESTE_controller" + Calendar.getInstance().getTimeInMillis());
        acesso = acessoController.salvarAcesso(acesso).getBody();
        assertEquals(true,acesso.getId() > 0);

    }

    @Test
    public void salvarFormaCorreta()throws JsonProcessingException, Exception{

        String descricaoAcesso = "ROLE_ADMIN" + Calendar.getInstance().getTimeInMillis();

        Acesso acesso = new Acesso();
        acesso.setDescAcesso(descricaoAcesso);
        acesso = acessoController.salvarAcesso(acesso).getBody();

       assertEquals(descricaoAcesso,acesso.getDescAcesso());
    }

    @Test
    public void testeCarregamento()throws JsonProcessingException, Exception{

        Acesso acesso = new Acesso();
        acesso.setDescAcesso("ROLE_ADMIN2" + Calendar.getInstance().getTimeInMillis());
        acesso = acessoController.salvarAcesso(acesso).getBody();
        Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();

        assertEquals(acesso.getId(), acesso2.getId());
    }

    @Test
    public void testeDeletarAcesso()throws JsonProcessingException, Exception{

        Acesso acesso = new Acesso();
        acesso.setDescAcesso("ROLE_ADMIN4" + Calendar.getInstance().getTimeInMillis());
        acesso = acessoController.salvarAcesso(acesso).getBody();
        acessoRepository.deleteById(acesso.getId());
        acessoRepository.flush();
        Acesso acessoDelete = acessoRepository.findById(acesso.getId()).orElse(null);

        assertEquals(true, acessoDelete == null);

    }

    @Test
    public void testeQuery()throws JsonProcessingException, Exception{

        Acesso acesso = new Acesso();
        acesso.setDescAcesso("ROLE_ALUNO" + Calendar.getInstance().getTimeInMillis());
        acesso = acessoController.salvarAcesso(acesso).getBody();
        List<Acesso> acessos = acessoRepository.buscarAcessoDescricao("ALUNO".trim().toUpperCase());

        assertEquals(1, acessos.size());
        acessoRepository.deleteById(acesso.getId());

    }
}