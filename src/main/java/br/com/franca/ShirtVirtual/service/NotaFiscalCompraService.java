package br.com.franca.ShirtVirtual.service;


import br.com.franca.ShirtVirtual.repository.NotaFiscalCompraRepository;
import br.com.franca.ShirtVirtual.utils.dto.RelatorioProdCompraNotaFiscalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotaFiscalCompraService {

    private NotaFiscalCompraRepository notaFiscalCompraRepository;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public NotaFiscalCompraService(NotaFiscalCompraRepository notaFiscalCompraRepository, JdbcTemplate jdbcTemplate){

        this.notaFiscalCompraRepository = notaFiscalCompraRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RelatorioProdCompraNotaFiscalDTO> gerarRelatorioProdCompraNota(RelatorioProdCompraNotaFiscalDTO relatorioProdCompraNotaFiscalDTO) {



        List<RelatorioProdCompraNotaFiscalDTO> retorno = new ArrayList<RelatorioProdCompraNotaFiscalDTO>();

        String sql = " SELECT p.id as codigoProduto "
                     + " ,p.nome_produto as nomeProduto "
                     + " ,p.valor_total_venda as valorVendaProduto "
                     + " ,ntp.quantidade_produto as quantidadeComprada "
                     + " ,pj.id as codigoFornecedor "
                     + " ,pj.nome_pessoa as nomeFornecedor "
                     + " ,nfc.data_compra as dataCompra "
                     + " FROM nota_fiscal_compra as nfc "
                     + " INNER JOIN nota_item_produto as ntp ON  nfc.id = nota_fiscal_compra_id "
                     + " INNER JOIN produto as p ON p.id = ntp.produto_id "
                     + " INNER JOIN pessoa_juridica as pj ON pj.id = nfc.pessoa_id WHERE ";

        sql += " nfc.data_compra >='" + relatorioProdCompraNotaFiscalDTO.getDataInicial()+"' and ";
        sql += " nfc.data_compra <= '" + relatorioProdCompraNotaFiscalDTO.getDataFinal() +"' ";



        retorno = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RelatorioProdCompraNotaFiscalDTO.class));

        return retorno;
    }

}