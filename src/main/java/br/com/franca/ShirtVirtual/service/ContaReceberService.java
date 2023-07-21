package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.repository.ContaReceberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class ContaReceberService {

    private ContaReceberRepository contaReceberRepository;

    @Autowired
    public ContaReceberService(ContaReceberRepository contaReceberRepository) {
        this.contaReceberRepository = contaReceberRepository;
    }

    public boolean verificarDescricaoNoMesCorrente(LocalDate dataVencimento) {
        LocalDate dataAtual = LocalDate.now();
        YearMonth mesCorrente = YearMonth.from(dataAtual);

        return YearMonth.from(dataVencimento).equals(mesCorrente);

    }

}
