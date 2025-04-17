package it.uniroma2.ispw.globe.model.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

public class AttractionBean {
    private String id;
    private String name;
    private String address;
    private String city;
    private double openingHours;
    private double closingHours;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(double openingHours) {
        this.openingHours = openingHours;
    }

    public double getClosingHours() {
        return closingHours;
    }

    public void setClosingHours(double closingHours) {
        this.closingHours = closingHours;
    }
}
