package br.com.franca.ShirtVirtual.controller;


import br.com.franca.ShirtVirtual.exceptions.ExceptionShirtVirtual;
import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.model.ImagemProduto;
import br.com.franca.ShirtVirtual.repository.ImagemProdutoRepository;
import br.com.franca.ShirtVirtual.utils.dto.ImagemProdutoDTO;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
@RequestMapping("/imagemProduto")
@Api(tags = "Imagem-Produto")
@Slf4j
public class ImagemProdutoController {

    private ImagemProdutoRepository imagemProdutoRepository;

    @Autowired
    public ImagemProdutoController(ImagemProdutoRepository imagemProdutoRepository) {
        this.imagemProdutoRepository = imagemProdutoRepository;
    }


    @ResponseBody
    @PostMapping(value = "/cadastrarImagemProduto")
    @ApiOperation("Cadastro imagem de produto")
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED", response = ImagemProdutoController.class),
            @ApiResponse(code = 403, message = "ACESS DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    public ResponseEntity <ImagemProdutoDTO> cadastrarImagemProduto(@RequestBody @Valid ImagemProduto imagemProduto) throws ExceptionShirtVirtual {

        log.info("Inicio do cadastro de imagem de um produto");

        ImagemProduto imagemProdutoSalva = imagemProdutoRepository.save(imagemProduto);

        ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
        imagemProdutoDTO.setId(imagemProduto.getId());
        imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
        imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
        imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
        imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());

        log.info("Cadastro realizado com sucesso.");
        return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoDTO, HttpStatus.CREATED);

    }

    @ApiOperation("Buscar imagem por idProduto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ImagemProdutoController.class),
            @ApiResponse(code = 403, message = "ACESS DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @GetMapping(value = "/buscarImagemPorProduto/{idProduto}")
    public ResponseEntity<List<ImagemProdutoDTO>> buscarImagemPorProduto(@PathVariable("idProduto") Long idProduto) throws ExceptionShirtVirtual {

        List<ImagemProdutoDTO> imagemProdutoDTOS = new ArrayList<ImagemProdutoDTO>();
        List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscarImagemProduto(idProduto);

        for (ImagemProduto imagemProduto: imagemProdutos){

            ImagemProdutoDTO imagemProdutoDTO = new ImagemProdutoDTO();
            imagemProdutoDTO.setId(imagemProduto.getId());
            imagemProdutoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
            imagemProdutoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
            imagemProdutoDTO.setProduto(imagemProduto.getProduto().getId());
            imagemProdutoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
            imagemProdutoDTOS.add(imagemProdutoDTO);

        }

        return new ResponseEntity<List<ImagemProdutoDTO>>(imagemProdutoDTOS,HttpStatus.OK);
    }

    @ApiOperation("delete Todas imagens produto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ImagemProdutoController.class),
            @ApiResponse(code = 403, message = "ACESS DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @DeleteMapping(value = "/deleteTodasImagensProduto/{idProduto}")
    public ResponseEntity<?> deleteTodasImagensProduto(@PathVariable("idProduto") Long idProduto) throws ExceptionShirtVirtual {

        if (!imagemProdutoRepository.existsById(idProduto)){
            log.error("A Imagem já foi excluida ou não exite"  + "IdProduto: " + idProduto);
            return new ResponseEntity<String>("A Imagem já foi excluida ou não exite"  + "IdProduto: " + idProduto, HttpStatus.OK);
        }

        imagemProdutoRepository.deletarImagensProduto(idProduto);
        return new ResponseEntity("Todas as imagens referente ao produto foram removidas com sucesso",HttpStatus.OK);

    }

    @ApiOperation("Deletar imagem produto por id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = ImagemProdutoController.class),
            @ApiResponse(code = 403, message = "ACESS DENIED"),
            @ApiResponse(code = 404, message = "NOT FOUND")
    })
    @ResponseBody
    @DeleteMapping(value = "/deleteImagemProdutoPorId/{id}")
    public ResponseEntity<?> deleteNotaItemProduto(@PathVariable("id") Long id) {

        imagemProdutoRepository.deleteById(id);
        return new ResponseEntity("imagem removido com sucesso",HttpStatus.OK);

    }
}