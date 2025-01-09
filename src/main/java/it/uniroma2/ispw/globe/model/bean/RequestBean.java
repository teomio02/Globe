package it.uniroma2.ispw.globe.model.bean;

import java.util.List;

public class RequestBean {
    private String id;
    private String user;
    private String agency;
    private String description;
    private int days;
    private List<String> cities;
    private List<String> attractions;
    private Boolean accepted;

    public RequestBean(String id, List<String> cities, List<String> attractions, String user, String agency, String description, int days , Boolean accepted) {
        this.id = id;
        this.cities = cities;
        this.attractions = attractions;
        this.user = user;
        this.agency = agency;
        this.description = description;
        this.days = days;
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

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
