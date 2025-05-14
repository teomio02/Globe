package it.uniroma2.ispw.globe.model.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

import java.util.List;

import static it.uniroma2.ispw.globe.other.ProposalState.*;

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

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) throws IncorrectDataException {
        if (user == null || user.isEmpty()) {
            throw new IncorrectDataException("Request user not valid");
        }
        this.user = user;
    }

    public String getAgency() { return agency; }

    public void setAgency(String agency) throws IncorrectDataException{
        if (agency == null || agency.isEmpty()) {
            throw new IncorrectDataException("Request agency not valid");
        }
        this.agency = agency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws IncorrectDataException {
        if (description == null || description.isEmpty()) {
            throw new IncorrectDataException("Request description not valid");
        }
        this.description = description;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) throws IncorrectDataException {
        if (days < 1 || days > 99) {
            throw new IncorrectDataException("Request days not valid");
        }
        this.days = days;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) throws IncorrectDataException {
        if (cities == null || cities.isEmpty()) {
            throw new IncorrectDataException("Request cities not valid");
        }
        this.cities = cities;
    }

    public List<String> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<String> attractions) throws IncorrectDataException {
        if (attractions == null || attractions.isEmpty()) {
            throw new IncorrectDataException("Request attractions not valid");
        }
        this.attractions = attractions;
    }

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) throws IncorrectDataException {
        if (accepted.equals(ACCEPTED) || accepted.equals(REJECTED) || accepted.equals(PENDING)) {
            this.accepted = accepted;
            return;
        }
        throw new IncorrectDataException("Request accepted not valid");
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) throws IncorrectDataException {
        if (types == null || types.isEmpty()) {
            throw new IncorrectDataException("Request types not valid");
        }
        this.types = types;
    }
}
