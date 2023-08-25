package br.com.franca.ShirtVirtual.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;

@Controller
public class PagamentoController implements Serializable {

    private static final long serialVersionUID = 1L;

    @RequestMapping(method = RequestMethod.GET, value = "/pagamento/{idVendaCompra}")
    @ApiOperation("Pagamento")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pagamento criado com sucesso", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    public ModelAndView pagamento(@PathVariable(value = "idVendaCompra", required = false) String idVendaCompra) {

        return new ModelAndView("pagamento");
    }
}