package br.com.franca.ShirtVirtual.service;

import br.com.franca.ShirtVirtual.repository.VendaCompraLojaVirtualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class VendaCompraLojaVirtualService {


    @PersistenceContext
    private EntityManager entityManager;


    private VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public VendaCompraLojaVirtualService(EntityManager entityManager, VendaCompraLojaVirtualRepository vendaCompraLojaVirtualRepository, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.vendaCompraLojaVirtualRepository = vendaCompraLojaVirtualRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deletarVendaTotal(Long idVenda) {

        String value =
                " BEGIN;"
                        + " UPDATE      nota_fiscal_venda SET   vd_cp_loja_virt_id = null WHERE vd_cp_loja_virt_id = "+idVenda+"; "
                        + " DELETE FROM nota_fiscal_venda WHERE vd_cp_loja_virt_id  = "+idVenda+"; "
                        + " DELETE FROM item_venda_loja   WHERE vd_cp_loja_virt_id  = "+idVenda+"; "
                        + " DELETE FROM status_rastreio   WHERE vd_cp_loja_virt_id  = "+idVenda+"; "
                        + " DELETE FROM vd_cp_loja_virt   WHERE id                  = "+idVenda+"; "
                        + " commit; ";

        jdbcTemplate.execute(value);
    }

    public void cancelarNotaFiscal(Long idVenda) {

        String value =
                " BEGIN;"
                + " UPDATE vd_cp_loja_virt SET ativo = false   WHERE id = "+idVenda+"; "
                + " commit; ";

        jdbcTemplate.execute(value);
    }
}
