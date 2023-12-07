package br.com.franca.ShirtVirtual.utils.dto;

public class CobrancaApiAsaasCartaoDTO {

    private String customer;
    private String billingType;
    private float value;
    private String dueDate;
    private String description;
    private String externalReference;
    private Integer installmentCount;
    private float installmentValue;

    private DiscontCobrancaAsaasDTO discount = new DiscontCobrancaAsaasDTO();
    private InterestCobrancaAsassDTO interest = new InterestCobrancaAsassDTO();
    private FineConbrancaAsaasDTO fine = new FineConbrancaAsaasDTO();

    private CartaoCreditoApiAsaasDTO creditCard = new CartaoCreditoApiAsaasDTO();
    private CartaoCreditoAsaasHolderInfoDTO creditCardHolderInfo = new CartaoCreditoAsaasHolderInfoDTO();

    private boolean postalService = false;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public Integer getInstallmentCount() {
        return installmentCount;
    }

    public void setInstallmentCount(Integer installmentCount) {
        this.installmentCount = installmentCount;
    }

    public float getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValue(float installmentValue) {
        this.installmentValue = installmentValue;
    }

    public DiscontCobrancaAsaasDTO getDiscount() {
        return discount;
    }

    public void setDiscount(DiscontCobrancaAsaasDTO discount) {
        this.discount = discount;
    }

    public InterestCobrancaAsassDTO getInterest() {
        return interest;
    }

    public void setInterest(InterestCobrancaAsassDTO interest) {
        this.interest = interest;
    }

    public FineConbrancaAsaasDTO getFine() {
        return fine;
    }

    public void setFine(FineConbrancaAsaasDTO fine) {
        this.fine = fine;
    }

    public CartaoCreditoApiAsaasDTO getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CartaoCreditoApiAsaasDTO creditCard) {
        this.creditCard = creditCard;
    }

    public CartaoCreditoAsaasHolderInfoDTO getCreditCardHolderInfo() {
        return creditCardHolderInfo;
    }

    public void setCreditCardHolderInfo(CartaoCreditoAsaasHolderInfoDTO creditCardHolderInfo) {
        this.creditCardHolderInfo = creditCardHolderInfo;
    }

    public boolean isPostalService() {
        return postalService;
    }

    public void setPostalService(boolean postalService) {
        this.postalService = postalService;
    }
}
