package br.com.franca.ShirtVirtual.controller;

import java.io.Serializable;

import br.com.franca.ShirtVirtual.model.BoletoJuno;
import br.com.franca.ShirtVirtual.repository.BoletoJunoRepository;
import br.com.franca.ShirtVirtual.utils.dto.AttibutesNotificaoPagaApiJuno;
import br.com.franca.ShirtVirtual.utils.dto.DataNotificacaoApiJunotPagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/requisicaojunoboleto")
public class RecebePagamentoWebHookApiJuno implements Serializable {

    private static final long serialVersionUID = 1L;

    private BoletoJunoRepository boletoJunoRepository;

    @Autowired
    public RecebePagamentoWebHookApiJuno(BoletoJunoRepository boletoJunoRepository) {
        this.boletoJunoRepository = boletoJunoRepository;
    }

    @ResponseBody
    @RequestMapping(value = "/notificacaoapiv2", consumes = {"application/json;charset=UTF-8"},
            headers = "Content-Type=application/json;charset=UTF-8", method = RequestMethod.POST)
    private HttpStatus recebeNotificaopagamentojunoapiv2(@RequestBody DataNotificacaoApiJunotPagamento dataNotificacaoApiJunotPagamento) {


        for (AttibutesNotificaoPagaApiJuno data : dataNotificacaoApiJunotPagamento.getData()) {

            String codigoBoletoPix = data.getAttributes().getCharge().getCode();

            String status = data.getAttributes().getStatus();

            boolean boletoPago = status.equalsIgnoreCase("CONFIRMED") ? true : false;

            BoletoJuno boletoJuno = boletoJunoRepository.findByCode(codigoBoletoPix);

            if (boletoJuno != null && !boletoJuno.isQuitado() && boletoPago) {

                boletoJunoRepository.quitarBoletoById(boletoJuno.getId());
                System.out.println("Boleto: " + boletoJuno.getCode() + " foi quitado ");

            }
        }

        return HttpStatus.OK;
    }



}
