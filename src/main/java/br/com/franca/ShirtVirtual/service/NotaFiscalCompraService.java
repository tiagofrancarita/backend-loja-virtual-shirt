package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.model.Acesso;
import br.com.franca.ShirtVirtual.model.NotaFiscalCompra;
import br.com.franca.ShirtVirtual.repository.NotaFiscalCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotaFiscalCompraService {

    private NotaFiscalCompraRepository notaFiscalCompraRepository;

    @Autowired
    public NotaFiscalCompraService(NotaFiscalCompraRepository notaFiscalCompraRepository){

        this.notaFiscalCompraRepository = notaFiscalCompraRepository;
    }
}