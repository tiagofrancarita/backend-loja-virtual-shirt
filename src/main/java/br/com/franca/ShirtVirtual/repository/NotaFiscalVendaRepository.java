package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.NotaFiscalVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface NotaFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {

    @Query(value = "SELECT notaFicalVenda FROM NotaFiscalVenda notaFicalVenda WHERE notaFicalVenda.vendaCompraLojaVirtual.id = ?1 AND notaFicalVenda.vendaCompraLojaVirtual.ativo=TRUE ")
    List<NotaFiscalVenda> buscarNotaFiscalPorIdVenda(Long idVenda);





}