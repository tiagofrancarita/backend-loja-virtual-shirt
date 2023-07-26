package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.ItemVendaLoja;
import br.com.franca.ShirtVirtual.model.NotaFiscalCompra;
import br.com.franca.ShirtVirtual.model.VendaCompraLojaVirtual;
import br.com.franca.ShirtVirtual.repository.NotaFiscalCompraRepository;
import br.com.franca.ShirtVirtual.service.NotaFiscalCompraService;
import br.com.franca.ShirtVirtual.utils.dto.ItemVendaDTO;
import br.com.franca.ShirtVirtual.utils.dto.VendaCompraLojaVirtualDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RestController
@RequestMapping("/NotaFiscaCompra")
@Api(tags = "Nota-Fiscal-Compra")
@Slf4j
public class NotaFiscalCompraController {


    final String INICIO_LISTAR_ACESSO = "Iniciando a listagem de acesso!";
    final String LISTAGEM_COM_SUCESSO = "Iniciando a listagem de acesso!";
    final String ERRO_DESCRICAO_CADASTRADA = "Já existe um acesso com essa descrição.!";

    private NotaFiscalCompraService notaFiscalCompraService;
    private NotaFiscalCompraRepository notaFiscalCompraRepository;

    @Autowired
    public NotaFiscalCompraController(NotaFiscalCompraService notaFiscalCompraService, NotaFiscalCompraRepository notaFiscalCompraRepository){
        this.notaFiscalCompraService = notaFiscalCompraService;
        this.notaFiscalCompraRepository = notaFiscalCompraRepository;
    }

    @ApiOperation("Listagem de todas notas fiscais cadastradas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @GetMapping("/listarNotaFiscalCompra")
    public List<NotaFiscalCompra> listarNotaFiscalCompra(){
        log.debug("Iniciando a listagem de nota fiscal");
        log.info("Lista extraida com sucesso");
        return notaFiscalCompraRepository.findAll();

    }

    @ResponseBody
    @PostMapping(value = "/cadastroNotaFiscalCompra")
    @ApiOperation("Cadastro de uma nota fiscal de compra")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cadastro criado com sucesso", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    public ResponseEntity <NotaFiscalCompra> cadastroNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExceptionShirtVirtual {

        log.info("Inicio do cadastro de nota fiscal");

        if (notaFiscalCompra.getId() == null){

            if (notaFiscalCompra.getDescricaoObs() != null){
                List<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.buscarNotaFiscalCompraDescricao(notaFiscalCompra.getDescricaoObs().toUpperCase().trim());
                if (!notaFiscalCompras.isEmpty()){
                    log.error("Cadastro de nota fiscal de compra encerrado com erro, descrição já cadastrada, favor informar outra");
                    throw new ExceptionShirtVirtual("Cadastro de nota fiscal de compra encerrado com erro, descrição já cadastrada, favor informar outra" + " Descrição: " + notaFiscalCompra.getDescricaoObs().toUpperCase().trim(), HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }

        }
        if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <=0 ){
            log.error("Cadastro de nota fiscal de compra encerrado com erro, é necessário associar uma pessoa responsável a nota de compra");
            throw new ExceptionShirtVirtual("Cadastro de nota fiscal de compra encerrado com erro, é necessário associar uma pessoa responsável a nota de compra", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <=0 ){
            log.error("Cadastro de nota fiscal de compra encerrado com erro, é necessário associar uma empresa responsável a nota de compra");
            throw new ExceptionShirtVirtual("Cadastro de nota fiscal de compra encerrado com erro, é necessário associar uma empresa responsável a nota de compra", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <=0 ){
            log.error("Cadastro de nota fiscal de compra encerrado com erro, é necessário associar uma conta a pagar a nota de compra");
            throw new ExceptionShirtVirtual("Cadastro de nota fiscal de compra encerrado com erro, é necessário associar uma conta a pagar a nota de compra", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        NotaFiscalCompra notaFiscalCompraCadastrada = notaFiscalCompraRepository.save(notaFiscalCompra);
        log.info("Cadastro realizado com sucesso.");
        return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraCadastrada, HttpStatus.CREATED);

    }

    @ApiOperation("Deletar nota fiscal de compra")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Nota deletado com sucesso", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Não autorizado"),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    @ResponseBody
    @DeleteMapping(value = "/deletarNotaFiscalCompra")
    public ResponseEntity <String> deletarNotaFiscalCompra(@RequestBody NotaFiscalCompra notaFiscalCompra){

        notaFiscalCompraRepository.deleteById(notaFiscalCompra.getId());
        return new ResponseEntity<String>("Nota Fiscal de compra removido com sucesso",HttpStatus.OK);
    }

    @ApiOperation("Deletar nota fiscal de compra por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "ACESS DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @DeleteMapping(value = "/deleteNotaFiscalCompraPorId/{id}")
    public ResponseEntity<?> deleteNotaFiscalCompraPorId(@PathVariable("id") Long id) {

        notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id);
        notaFiscalCompraRepository.deleteById(id);
        return new ResponseEntity("Nota fiscal de compra removido com sucesso",HttpStatus.OK);

    }

    @ApiOperation("Buscar nota fiscal por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso encontrada", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorId/{id}")
    public ResponseEntity<NotaFiscalCompra> buscarNotaFiscalCompraPorId(@PathVariable("id") Long id) throws ExceptionShirtVirtual {

        NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

        if (notaFiscalCompra == null){
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  id, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra,HttpStatus.OK);
    }

    @ApiOperation("Buscar nota fiscal de compra pela descrição")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Acesso encontrada", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Acesso não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorDesc/{desc}")
    public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalCompraPorDesc(@PathVariable("desc") String desc) {

        List<NotaFiscalCompra> notaFiscalCompra = notaFiscalCompraRepository.buscarNotaFiscalCompraDescricao(desc.toUpperCase().trim());

        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompra,HttpStatus.OK);
    }

    @ApiOperation("Buscar nota fiscal de compra pela numero nota")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Nota encontrada", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Nota não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorNumeroNota/{numeroNota}")
    public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalCompraPorNumeroNota(@PathVariable("numeroNota") String numeroNota) {

        List<NotaFiscalCompra> notaFiscalCompra = notaFiscalCompraRepository.buscarNotaFiscalCompraNumeroNota(numeroNota.toUpperCase().trim());

        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompra,HttpStatus.OK);
    }

    @ApiOperation("Buscar nota fiscal de compra pela serie nota")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Nota encontrada", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Nota não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorSerieNota/{SerieNota}")
    public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalCompraPorSerieNota(@PathVariable("SerieNota") String SerieNota) {

        List<NotaFiscalCompra> notaFiscalCompra = notaFiscalCompraRepository.buscarNotaFiscalCompraSerieNota(SerieNota.toUpperCase().trim());

        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompra,HttpStatus.OK);
    }

    @ApiOperation("Buscar nota fiscal de compra por id pessoa")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorPessoa/{idPessoa}")
    public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalCompraPorPessoa(@PathVariable("idPessoa") Long idPessoa) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de nota fiscal de compra por id pessoa");
        List<NotaFiscalCompra> notaFiscalCompra = notaFiscalCompraRepository.buscarNotaFiscalCompraPorPessoa(idPessoa);

        if (notaFiscalCompra == null){
            log.error("Erro ao buscar nota por pessoa, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  idPessoa, HttpStatus.NOT_FOUND);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompra,HttpStatus.OK);
    }


    @ApiOperation("Buscar nota fiscal de compra por id empresa")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorEmpresa/{idEmpresa}")
    public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalCompraPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de nota fiscal de compra por id empresa");
        List<NotaFiscalCompra> notaFiscalCompra = notaFiscalCompraRepository.buscarNotaFiscalCompraPorEmpresa(idEmpresa);

        if (notaFiscalCompra == null){
            log.error("Erro ao buscar nota fiscal de compra por empresa, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  idEmpresa, HttpStatus.NOT_FOUND);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompra,HttpStatus.OK);
    }

    @ApiOperation("Buscar nota fiscal de compra por id conta a pagar")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorContaPagar/{idContaPagar}")
    public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalCompraPorContaPagar(@PathVariable("idContaPagar") Long idContaPagar) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de nota fiscal de compra por id conta pagar");
        List<NotaFiscalCompra> notaFiscalCompra = notaFiscalCompraRepository.buscarNotaFiscalCompraPorContaPagar(idContaPagar);

        if (notaFiscalCompra == null){
            log.error("Erro ao buscar nota fiscal de compra por empresa, id inválido ou inexistente");
            throw new ExceptionShirtVirtual("O código informado não existe. " + " id: "  +  idContaPagar, HttpStatus.NOT_FOUND);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompra,HttpStatus.OK);
    }

    @ApiOperation("Buscar nota fiscal de compra por cnpj pessoa juridica ")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Consulta encontrada", response = NotaFiscalCompraController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
            @ApiResponse(code = 404, message = "Consulta não encontrada")
    })
    @ResponseBody
    @GetMapping(value = "/buscaNotaFiscalCompraPessoaCnpj/{cnpj}")
    public ResponseEntity<List<NotaFiscalCompra>> buscaNotaFiscalCompraPessoaCnpj(@PathVariable("cnpj") String cnpj) throws ExceptionShirtVirtual {

        log.info("Inicio da busca de nota fiscal de compra por id conta pagar");
        List<NotaFiscalCompra> notaFiscalCompra = notaFiscalCompraRepository.buscarNotaFiscalCompraCnpjPessoa(cnpj);

        if (notaFiscalCompra == null){
            log.error("Erro ao buscar nota fiscal de compra por pessoa, cnpj inválido ou inexistente");
            throw new ExceptionShirtVirtual("Erro ao buscar nota fiscal de compra por pessoa, cnpj inválido ou inexistente" + " CNPJ: "  +  cnpj, HttpStatus.NOT_FOUND);

        }
        log.info("Busca realizada com sucesso");
        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompra,HttpStatus.OK);
    }

    @ApiOperation("Buscar venda por data ")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = VendaCompraLojaVirtualController.class),
            @ApiResponse(code = 403, message = "Acess DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarNotaFiscalCompraPorData/{dataInicio}/{dataFim}")
    public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalCompraPorData(@PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim") String dataFim) throws ParseException, ParseException {


        log.info("Inicio da busca de nota fiscal de compra data inicio e fim");

        SimpleDateFormat dataFormatada = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = dataFormatada.parse(dataInicio);
        Date d2 = dataFormatada.parse(dataFim);

        List<NotaFiscalCompra> notaFiscalCompra = notaFiscalCompraRepository.buscarNotaFiscalCompraPorData(d1,d2);

        log.info("Busca concluida com sucesso");

        return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompra, HttpStatus.OK);
    }
}