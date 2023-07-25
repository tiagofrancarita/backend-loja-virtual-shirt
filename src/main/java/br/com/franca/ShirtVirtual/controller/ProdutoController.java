package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Produto;
import br.com.franca.ShirtVirtual.repository.ProdutoRepository;
import br.com.franca.ShirtVirtual.service.ServiceSendEmail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

@Controller
@RestController
@RequestMapping("/produtos")
@Api(tags = "Produtos")
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    final String INICIO_LISTAR_ACESSO = "Iniciando a listagem de acesso!";
    final String LISTAGEM_COM_SUCESSO = "Iniciando a listagem de acesso!";
    final String ERRO_DESCRICAO_CADASTRADA = "Já existe um produto com essa descrição.!";

    private ProdutoRepository produtoRepository;
    private ServiceSendEmail serviceSendEmail;

    @Autowired
    public ProdutoController(ProdutoRepository produtoRepository, ServiceSendEmail serviceSendEmail) {
        this.produtoRepository = produtoRepository;
        this.serviceSendEmail = serviceSendEmail;
    }

    @ApiOperation("Listagem de produtos cadastrados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarProdutos")
    public List<Produto> listarProdutos(){

        return produtoRepository.findAll();

    }

    @ApiOperation("Cadastro de produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @PostMapping(value = "/salvarProduto")
    public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionShirtVirtual, MessagingException, IOException {

        logger.info("Cadastro de produto iniciado.");

        if (produto.getTipoUnidade() == null || produto.getTipoUnidade().isEmpty()) {
            logger.error("Cadastro de produto encerrado com erro, tipo da unidade deve ser informada.");
            throw new ExceptionShirtVirtual("Tipo da unidade deve ser informada.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (produto.getNome().length() < 10) {
            logger.error("Cadastro de produto encerrado com erro, nome do produto deve ter mais que 10 caracteres.");
            throw new ExceptionShirtVirtual("Nome do produto deve ter mais que 10 caracteres.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
            logger.error("Cadastro de produto encerrado com erro, empresa responsável deve ser informada.");
            throw new ExceptionShirtVirtual("Empresa responsável deve ser informada.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (produto.getId() == null) {
            List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getDescricaoProduto().toUpperCase(),
                    produto.getEmpresa().getId());
            if (!produtos.isEmpty()) {
                logger.error("Cadastro de produto encerrado com erro, Já existe um produto com essa descrição." + " Descrição: " +  produto.getDescricaoProduto());
                throw new ExceptionShirtVirtual("Cadastro de produto encerrado com erro, Já existe um produto com essa descrição." + " Descrição: " +  produto.getDescricaoProduto(), HttpStatus.UNPROCESSABLE_ENTITY);
            }

            if (produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
                logger.error("Cadastro de produto encerrado com erro, categoria do produto deve ser imformada.");
                throw new ExceptionShirtVirtual("Categoria do produto deve ser informada.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
                logger.error("Cadastro de produto encerrado com erro, marca do produto deve ser imformada.");
                throw new ExceptionShirtVirtual("Marca do produto deve ser informada.", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            if (produto.getQtdEstoque() < 1) {
                logger.error("Cadastro de produto encerrado com erro, o produto cadastrado deve ter no minímo 1 no estoque.");
                throw new ExceptionShirtVirtual("O produto cadastrado deve ter no minímo 1 no estoque.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }


        if (produto.getImagens() == null || produto.getImagens().isEmpty() || produto.getImagens().size() == 0){
            logger.error("Cadastro de produto encerrado com erro, deve ser carregado imagem do produto");
            throw new ExceptionShirtVirtual("Cadastro de produto encerrado com erro, deve ser informado imagem do produto", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (produto.getImagens().size() < 3 ){
            logger.error("Cadastro de produto encerrado com erro, deve ser carregado pelo menos 3 imagems para o produto");
            throw new ExceptionShirtVirtual("Cadastro de produto encerrado com erro, deve ser carregado pelo menos 3 imagems para o produto", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (produto.getImagens().size() >= 6 ){
            logger.error("Cadastro de produto encerrado com erro, deve ser carregado no maximo 6 imagens para o produto");
            throw new ExceptionShirtVirtual("Cadastro de produto encerrado com erro, deve ser carregado no maximo 6 imagens para o produto", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (produto.getId() == null){

            for (int x = 0; x < produto.getImagens().size(); x++) {
                produto.getImagens().get(x).setProduto(produto);
                produto.getImagens().get(x).setEmpresa(produto.getEmpresa());

                String base64Image = "";

                if (produto.getImagens().get(x).getImagemOriginal().contains("data:image")) {
                    base64Image = produto.getImagens().get(x).getImagemOriginal().split(",")[1];

                } else {
                    base64Image = produto.getImagens().get(x).getImagemOriginal();
                }

                byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);

                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

                if (bufferedImage != null) {

                    int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
                    int largura = Integer.parseInt("800");
                    int altura = Integer.parseInt("600");

                    BufferedImage resizedImage = new BufferedImage(largura, altura, type);
                    Graphics2D g = resizedImage.createGraphics();
                    g.drawImage(bufferedImage, 0, 0, largura, altura, null);
                    g.dispose();

                    ByteArrayOutputStream saidaImagem = new ByteArrayOutputStream();
                    ImageIO.write(resizedImage, "png", saidaImagem);

                    String miniImgBase64 = "data:image/png;base64," + DatatypeConverter.printBase64Binary(saidaImagem.toByteArray());

                    produto.getImagens().get(x).setImagemMiniatura(miniImgBase64);

                    bufferedImage.flush();
                    resizedImage.flush();
                    saidaImagem.flush();
                    saidaImagem.close();

                }
            }
        }
        Produto produtoSalvo = produtoRepository.save(produto);

        if (produto.getQtdAlertaEstoque()  <= 1 && produto.getQtdEstoque() <= 1){

            StringBuilder html = new StringBuilder();
            html.append("<h2>").append("Produto:" + produto.getNome())
                    .append("com estoque baixo" + produto.getQtdEstoque());
            html.append("<p> Código produto:").append(produto.getId()).append("</p>");

            if (produto.getEmpresa().getEmail() != null){
                serviceSendEmail.enviaEmailHtml("Estoque abaixo",html.toString(),produto.getEmpresa().getEmail());
            }
        }

        logger.info("Cadastro de produto realizado com sucesso.");
        return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.CREATED);

    }

    @ApiOperation("Deletar produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Delete com sucesso", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarProduto")
    public ResponseEntity <String> deletarProduto(@RequestBody Produto produto){

        produtoRepository.deleteById(produto.getId());

        return new ResponseEntity<String>("Produto removido com sucesso",HttpStatus.OK);
    }

    @ApiOperation("Deletar produto por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Delete efetuado", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Não encontrada")
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarProdutoPorId/{id}")
    public ResponseEntity<?> deletarProdutoPorId(@PathVariable("id") Long id) {

        logger.info("Deleção de produto em andamento...");
        produtoRepository.deleteById(id);
        logger.info("Produto deletado");
        return new ResponseEntity("Produto removido com sucesso",HttpStatus.OK);

    }

    @ApiOperation("Buscar produto por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarProdutoPorId/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        logger.info("Inicio de busca de produto por id...");
        Produto produto = produtoRepository.findById(id).orElse(null);

        if (produto == null){
            logger.error("Erro ao buscar produto por id, o código informado não existe ou é inválido");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<Produto>(produto,HttpStatus.OK);
    }

    @ApiOperation("Buscar produto por nome")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = ProdutoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarProdutoPorNome/{nome}")
    public ResponseEntity<List<Produto>> buscarPorDesc(@PathVariable("nome") String nome) throws ExceptionShirtVirtual {

        logger.info("Inicio de busca de produto por nome...");
        List<Produto> produto = produtoRepository.buscarPorDesc(nome.toUpperCase().trim());

        if (produto == null){
            logger.error("Erro ao buscar produto por nome, o nome informado não existe ou é inválido");
            throw new ExceptionShirtVirtual("O nome informado não existe. " + " nome: "  +  nome, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<List<Produto>>(produto,HttpStatus.OK);
    }
}