package it.uniroma2.ispw.globe.model.bean;

import java.util.List;

public class AgencyRequestBean {
    private String id;
    private String user;
    private String agency;
    private String description;
    private int days;
    private List<String> types;
    private List<String> cities;
    private List<String> attractions;
    private String accepted;

    public AgencyRequestBean(String id, String user, String agency, String description, int days, List<String> types, String accepted) {
        this.id = id;
        this.user = user;
        this.agency = agency;
        this.description = description;
        this.days = days;
        this.types = types;
        this.accepted = accepted;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAgency() { return agency; }

    public void setAgency(String agency) { this.agency = agency; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public List<String> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<String> attractions) {
        this.attractions = attractions;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
