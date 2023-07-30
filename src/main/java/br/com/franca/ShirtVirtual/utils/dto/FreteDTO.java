package br.com.franca.ShirtVirtual.utils.dto;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FreteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String service;

    private String agency;

    private FromEnvioFreteDTO from =  new FromEnvioFreteDTO();

    private ToEnvioFreteDTO to =  new ToEnvioFreteDTO();

    private List<ProductsEnvioFreteDTO> products = new ArrayList<ProductsEnvioFreteDTO>();

    private List<VolumeEnvioFreteDTO> volumes = new ArrayList<VolumeEnvioFreteDTO>();

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

    public List<ProductsEnvioFreteDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsEnvioFreteDTO> products) {
        this.products = products;
    }

    public List<VolumeEnvioFreteDTO> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<VolumeEnvioFreteDTO> volumes) {
        this.volumes = volumes;
    }
}