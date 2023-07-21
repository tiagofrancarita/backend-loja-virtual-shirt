package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.model.ContaPagar;
import br.com.franca.ShirtVirtual.repository.ContaPagarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class ContaPagarService {

    private ContaPagarRepository contaPagarRepository;

    @Autowired
    public ContaPagarService(ContaPagarRepository contaPagarRepository) {
        this.contaPagarRepository = contaPagarRepository;
    }

    public boolean verificarDescricaoNoMesCorrente(LocalDate dataVencimento) {
        LocalDate dataAtual = LocalDate.now();
        YearMonth mesCorrente = YearMonth.from(dataAtual);

        return YearMonth.from(dataVencimento).equals(mesCorrente);

    }

}
