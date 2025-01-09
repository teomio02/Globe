package it.uniroma2.ispw.globe.model.bean;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.User;

public class RequestBean {
    private String id;
    private User user;
    private Agency agency;
    private Boolean accepted;

    public RequestBean(String id, User user, Agency agency, Boolean accepted) {
        this.id = id;
        this.user = user;
        this.agency = agency;
        this.accepted = accepted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
