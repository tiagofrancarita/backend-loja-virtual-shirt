package br.com.franca.ShirtVirtual.controller;

import br.com.franca.ShirtVirtual.model.VendaCompraLojaVirtual;
import br.com.franca.ShirtVirtual.repository.VendaCompraLojaVirtualRepository;
import br.com.franca.ShirtVirtual.service.VendaService;
import br.com.franca.ShirtVirtual.utils.dto.VendaCompraLojaVirtualDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.io.Serializable;

@Controller
public class PagamentoController implements Serializable {

    private static final long serialVersionUID = 1L;


    private VendaCompraLojaVirtualRepository vd_Cp_Loja_virt_repository;

    private VendaService vendaService;

    @Autowired
    public PagamentoController(VendaCompraLojaVirtualRepository vd_Cp_Loja_virt_repository, VendaService vendaService) {
        this.vd_Cp_Loja_virt_repository = vd_Cp_Loja_virt_repository;
        this.vendaService = vendaService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pagamento/{idVendaCompra}")
    @ApiOperation("Pagamento")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pagamento criado com sucesso", response = AcessoController.class),
            @ApiResponse(code = 403, message = "Requisição não autoziada"),
    })
    public ModelAndView pagamento(@PathVariable(value = "idVendaCompra", required = false) String idVendaCompra) {


        ModelAndView modelAndView = new ModelAndView("pagamento");
        VendaCompraLojaVirtual compraLojaVirtual = vd_Cp_Loja_virt_repository.findByIdExclusao(Long.parseLong(idVendaCompra));

        if (compraLojaVirtual == null) {
            modelAndView.addObject("venda", new VendaCompraLojaVirtualDTO());
        }else {
            modelAndView.addObject("venda", vendaService.consultaVenda(compraLojaVirtual));
        }

        return modelAndView;
    }

}



