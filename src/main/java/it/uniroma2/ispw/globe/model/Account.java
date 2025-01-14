package it.uniroma2.ispw.globe.model;

public abstract class Account {
    private String username;
    private String password;
    private String type;
    private Itinerary newItinerary;
    private Proposal newProposal;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Itinerary getNewItinerary() {
        return newItinerary;
    }

    public void setNewItinerary(Itinerary newItinerary) {
        this.newItinerary = newItinerary;
    }

    public Proposal getNewProposal() {
        return newProposal;
    }

    public void setNewProposal(Proposal newProposal) {
        this.newProposal = newProposal;
    }
}
