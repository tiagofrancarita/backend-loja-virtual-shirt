package br.com.franca.ShirtVirtual.utils.dto;

import java.io.Serializable;

public class ToEnvioFreteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
    private String email;
    private String document;
    private String company_document;
    private String state_register;
    private String postal_code;
    private String address;
    private String location_number;
    private String complement;
    private String district;
    private String city;
    private String state_abbr;
    private String country_id;
    private String latitude;
    private String longitude;
    private String note;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getCompany_document() {
        return company_document;
    }

    public void setCompany_document(String company_document) {
        this.company_document = company_document;
    }

    public String getState_register() {
        return state_register;
    }

    public void setState_register(String state_register) {
        this.state_register = state_register;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation_number() {
        return location_number;
    }

    public void setLocation_number(String location_number) {
        this.location_number = location_number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState_abbr() {
        return state_abbr;
    }

    public void setState_abbr(String state_abbr) {
        this.state_abbr = state_abbr;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
