package br.com.franca.ShirtVirtual.utils.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OptionsEnvioFreteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String insurance_value;

    private String receipt;

    private String own_hand;

    private String reverse;

    private String non_commercial;

    private InvoiceEnvioDTO invoice = new InvoiceEnvioDTO();

    private String platform;

   private List<TagsEnvioFreteDTO> tags = new ArrayList<TagsEnvioFreteDTO>();

    public String getInsurance_value() {
        return insurance_value;
    }

    public void setInsurance_value(String insurance_value) {
        this.insurance_value = insurance_value;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getOwn_hand() {
        return own_hand;
    }

    public void setOwn_hand(String own_hand) {
        this.own_hand = own_hand;
    }

    public String getReverse() {
        return reverse;
    }

    public void setReverse(String reverse) {
        this.reverse = reverse;
    }

    public String getNon_commercial() {
        return non_commercial;
    }

    public void setNon_commercial(String non_commercial) {
        this.non_commercial = non_commercial;
    }

    public InvoiceEnvioDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceEnvioDTO invoice) {
        this.invoice = invoice;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public List<TagsEnvioFreteDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagsEnvioFreteDTO> tags) {
        this.tags = tags;
    }
}