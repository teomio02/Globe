package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;

public abstract class ProposalDao {
    public Proposal createProposal(ProposalBean proposalBean, Itinerary itinerary, User user, Agency agency) {
        Proposal proposal = new Proposal();

        proposal.setId(proposalBean.getID());
        proposal.setName(proposalBean.getName());
        proposal.setItinerary(itinerary);
        proposal.setPrice(proposalBean.getPrice());
        proposal.setDescription(proposalBean.getDescription());
        proposal.setUser(user);
        proposal.setAgency(agency);
        proposal.setAccepted(false);

        return proposal;
    }
    public abstract void addProposal(Proposal proposal, User user);
    public abstract Proposal getProposal(String proposalName);
    public abstract void removeProposal(String itineraryID);
}
