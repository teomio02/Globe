package it.uniroma2.ispw.globe.model.bean;

public class ProposalBean {
    private String name;
    private Float price;
    private String agency;
    private Boolean accepted;

    public ProposalBean(String name, Float price, String agency, Boolean accepted) {
        this.name = name;
        this.price = price;
        this.agency = agency;
        this.accepted = accepted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
