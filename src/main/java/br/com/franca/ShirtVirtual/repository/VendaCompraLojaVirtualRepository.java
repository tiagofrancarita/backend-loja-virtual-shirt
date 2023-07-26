package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.VendaCompraLojaVirtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {


    @Query(value = "SELECT vd FROM VendaCompraLojaVirtual vd WHERE vd.id = ?1 and  vd.ativo = TRUE ")
    VendaCompraLojaVirtual BuscaVendaAtiva(Long id);

    @Query("SELECT vd from VendaCompraLojaVirtual vd where vd.pessoa.id = ?1 AND  vd.ativo = TRUE ")
    List<VendaCompraLojaVirtual> buscarVendaPorCliente(Long idCliente);

    @Query(value="SELECT i.vendaCompraLojaVirtual FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.ativo = TRUE AND i.produto.id = ?1")
    List<VendaCompraLojaVirtual> buscarVendaPorProduto(Long idProduto);

    @Query("SELECT vd FROM VendaCompraLojaVirtual vd WHERE vd.formaPagamento.id = ?1")
    List<VendaCompraLojaVirtual> buscarVendaPorFormaPagamento(Long idFormaPagamento);

    @Query("SELECT vd FROM VendaCompraLojaVirtual vd WHERE vd.notaFiscalVenda.id = ?1")
    List<VendaCompraLojaVirtual> buscarVendaPorNotaFiscalVenda(Long idNotaFiscalVenda);

    @Query("SELECT vd FROM VendaCompraLojaVirtual vd WHERE vd.empresa.id = ?1")
    List<VendaCompraLojaVirtual> buscarVendaPorEmpresa(Long idEmpresa);

    @Query("SELECT vd FROM VendaCompraLojaVirtual vd WHERE upper(trim(vd.pessoa.cpf)) LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscarVendaPorCpfCliente(String cpfCliente);

    @Query("SELECT vd FROM VendaCompraLojaVirtual vd WHERE upper(trim(vd.pessoa.email)) LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscarVendaPorEmailCliente(String emailCliente);

    @Query("SELECT vd FROM VendaCompraLojaVirtual vd WHERE upper(trim(vd.notaFiscalVenda.numeroNotaFiscalVenda)) LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscarVendaPorNumeroNota(String numeroNotaFiscal);

    @Query("SELECT vd FROM VendaCompraLojaVirtual vd WHERE upper(trim(vd.empresa.cnpj)) LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscarVendaPorCnpjEmpresa(String cnpjEmpresa);

    @Query("SELECT vd FROM VendaCompraLojaVirtual vd WHERE upper(trim(vd.enderecoEntrega.bairro)) LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscarVendaPorBairroEntrega(String nomeBairro);

    @Query(value="SELECT DISTINCT(i.vendaCompraLojaVirtual) FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.ativo = TRUE AND UPPER(TRIM(i.vendaCompraLojaVirtual.pessoa.nome))  LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscaVendaPorNomeCliente(String nomeCliente);

    @Query(value="SELECT DISTINCT(i.vendaCompraLojaVirtual) FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.ativo = TRUE AND UPPER(TRIM(i.produto.nome))  LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscarVendaPorNomeProduto(String valor);

    @Query(value="SELECT DISTINCT(i.vendaCompraLojaVirtual) FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.ativo = TRUE AND UPPER(TRIM(i.vendaCompraLojaVirtual.enderecoCobranca.logradouro))  LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscaVendaPorEnderecoCobranca(String valor);

    @Query(value="SELECT DISTINCT(i.vendaCompraLojaVirtual) FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.ativo = TRUE AND UPPER(TRIM(i.vendaCompraLojaVirtual.enderecoCobranca.logradouro))  LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscaVendaPorEnderecoEntrega(String valor);

    @Query(value="SELECT DISTINCT(i.vendaCompraLojaVirtual) FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.ativo = TRUE AND UPPER(TRIM(i.vendaCompraLojaVirtual.enderecoCobranca.estado))  LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscaVendaPorEstadoEntrega(String trim);

    @Query(value="SELECT DISTINCT(i.vendaCompraLojaVirtual) FROM ItemVendaLoja i WHERE i.vendaCompraLojaVirtual.ativo = TRUE AND UPPER(TRIM(i.vendaCompraLojaVirtual.enderecoCobranca.estado))  LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscaVendaPorEstadoCobranca(String trim);

    @Query(value = "SELECT vd FROM VendaCompraLojaVirtual vd WHERE vd.dtVenda BETWEEN  ?1 AND ?2 AND vd.ativo = true ")
    List<VendaCompraLojaVirtual> buscarVendaPorData(Date dataInicio, Date dataFim);

    //WHERE payment_date BETWEEN '2007-02-07' AND '2007-02-15';
}