package it.uniroma2.ispw.globe.model;

import java.util.List;

public class User extends Account{
    private String type;
    private List<Itinerary> itineraries;
    private List<Proposal> proposals;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

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
