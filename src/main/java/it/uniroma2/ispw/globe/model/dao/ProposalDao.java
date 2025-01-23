package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;

import static it.uniroma2.ispw.globe.other.ProposalState.PENDING;

public abstract class ProposalDao {
    public Proposal createProposal(String id, double price, String description, Itinerary itinerary, User user, Agency agency) {
        Proposal proposal = new Proposal();

        proposal.setId(id);
        proposal.setItinerary(itinerary);
        proposal.setPrice(price);
        proposal.setDescription(description);
        proposal.setUser(user);
        proposal.setAgency(agency);
        proposal.setAccepted(PENDING);

        return proposal;
    }
    public abstract void addProposal(Proposal proposal);
    public abstract Proposal getProposal(String proposalName);
    public abstract void removeProposal(String itineraryID);
}
