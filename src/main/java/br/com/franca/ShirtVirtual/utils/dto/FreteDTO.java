package br.com.franca.ShirtVirtual.utils.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FreteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String service;

    private String agency;

    private FromEnvioFreteDTO from =  new FromEnvioFreteDTO();

    private ToEnvioFreteDTO to =  new ToEnvioFreteDTO();

    private List<ProductsEnvioEtiquetaDTO> products = new ArrayList<ProductsEnvioEtiquetaDTO>();

    private List<VolumesEnvioEtiquetaDTO> volumes = new ArrayList<VolumesEnvioEtiquetaDTO>();

    private OptionsEnvioFreteDTO options = new OptionsEnvioFreteDTO();

    public OptionsEnvioFreteDTO getOptions() {
        return options;
    }

    public void setOptions(OptionsEnvioFreteDTO options) {
        this.options = options;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public FromEnvioFreteDTO getFrom() {
        return from;
    }

    public void setFrom(FromEnvioFreteDTO from) {
        this.from = from;
    }

    public ToEnvioFreteDTO getTo() {
        return to;
    }

    public void setTo(ToEnvioFreteDTO to) {
        this.to = to;
    }

    public List<ProductsEnvioEtiquetaDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsEnvioEtiquetaDTO> products) {
        this.products = products;
    }

    public List<VolumesEnvioEtiquetaDTO> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<VolumesEnvioEtiquetaDTO> volumes) {
        this.volumes = volumes;
    }
}