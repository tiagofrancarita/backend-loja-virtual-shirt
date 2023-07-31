package br.com.franca.ShirtVirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatusRastreioService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public StatusRastreioService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void listaRastreioVenda(Long idVenda) {

        String value = "select * from status_rastreio status where status.vd_cp_loja_virt_id = "+idVenda+"; ";
        jdbcTemplate.execute(value);
    }


}
