package it.uniroma2.ispw.globe.model.bean;


import it.uniroma2.ispw.globe.exception.IncorrectDataException;

public class ProposalBean {
    private String id;
    private double price;
    private String agency;
    private String user;
    private String description;
    private String accepted;

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws IncorrectDataException {
        if (price < 0) {
            throw new IncorrectDataException("Price not valid");
        }
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

    public void setDescription(String description) throws IncorrectDataException {
        if (description == null || description.isEmpty()) {
            throw new IncorrectDataException("Description not valid");
        }
        this.description = description;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }
}
