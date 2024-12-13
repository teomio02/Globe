package it.uniroma2.ispw.globe.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Itinerary> itineraries;
    private List<Proposal> proposals;

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

    public List<Itinerary> getItineraries() {
        return itineraries;
    }
    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    public List<Proposal> getProposals() {
        return proposals;
    }
    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
    }
}
