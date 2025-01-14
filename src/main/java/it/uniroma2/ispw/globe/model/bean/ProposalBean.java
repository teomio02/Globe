package it.uniroma2.ispw.globe.model.bean;

import it.uniroma2.ispw.globe.model.Itinerary;

public class ProposalBean {
    private String id;
    private String name;
    private double price;
    private String agency;
    private String user;
    private String description;
    private Boolean accepted;

    public ProposalBean(String id, String name, double price, String agency, String user, String description, Boolean accepted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.agency = agency;
        this.user = user;
        this.description = description;
        this.accepted = accepted;
    }

    public ProposalBean(String name, double price, String user, String description) {
        this.name = name;
        this.price = price;
        this.user = user;
        this.description = description;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
