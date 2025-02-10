package it.uniroma2.ispw.globe.model.bean;


public class ProposalBean {
    private String id;
    private double price;
    private String agency;
    private String user;
    private String description;
    private String accepted;

    public ProposalBean(String id, double price, String agency, String user, String description, String accepted) {
        this.id = id;
        this.price = price;
        this.agency = agency;
        this.user = user;
        this.description = description;
        this.accepted = accepted;
    }

    public ProposalBean(double price, String description) {
        this.price = price;
        this.description = description;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
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

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }
}
