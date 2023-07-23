package br.com.franca.ShirtVirtual.repository;

import br.com.franca.ShirtVirtual.model.VendaCompraLojaVirtual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface VendaCompraLojaVirtualRepository extends JpaRepository<VendaCompraLojaVirtual, Long> {

    @Query("select vd from VendaCompraLojaVirtual vd where vd.id = ?1")
    List<VendaCompraLojaVirtual> buscaVendaPorId(Long idVenda);

    @Query("select vd from VendaCompraLojaVirtual vd where vd.pessoa.id = ?1")
    List<VendaCompraLojaVirtual> buscarVendaPorCliente(Long idCliente);

    @Query("select vd from VendaCompraLojaVirtual vd where vd.formaPagamento.id = ?1")
    List<VendaCompraLojaVirtual> buscarVendaPorFormaPagamento(Long idFormaPagamento);

    @Query("select vd from VendaCompraLojaVirtual vd where vd.notaFiscalVenda.id = ?1")
    List<VendaCompraLojaVirtual> buscarVendaPorNotaFiscalVenda(Long idNotaFiscalVenda);

    @Query("select vd from VendaCompraLojaVirtual vd where vd.empresa.id = ?1")
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


}