package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.VendaCompraLojaVirtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {


    @Query(value = "SELECT vd FROM VendaCompraLojaVirtual vd WHERE vd.id = ?1 and  vd.ativo = TRUE ")
    VendaCompraLojaVirtual BuscaVendaAtiva(Long id);

    @Query("SELECT vd from VendaCompraLojaVirtual vd where vd.pessoa.id = ?1 AND  vd.ativo = TRUE ")
    List<VendaCompraLojaVirtual> buscarVendaPorCliente(Long idCliente);

    @Query(value="SELECT i.vendaCompraLojaVirtual FROM ItemVendaLoja i WHERE "
            + " i.vendaCompraLojaVirtual.ativo = TRUE AND i.produto.id = ?1")
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

    @Query("SELECT vd FROM VendaCompraLojaVirtual vd WHERE upper(trim(vd.pessoa.nome)) LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscaVendaPorNomeCliente(String nomeCliente);

    @Query(value="SELECT DISTINCT(i.vendaCompraLojaVirtual) FROM ItemVendaLoja i WHERE "
            + " i.vendaCompraLojaVirtual.ativo = TRUE AND UPPER(TRIM(i.produto.nome))  LIKE %?1% ")
    List<VendaCompraLojaVirtual> buscarVendaPorNomeProduto(String valor);

}