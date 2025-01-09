package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;

public abstract class ProposalDao {
    public abstract void addProposal(ProposalBean proposalBean, Itinerary itinerary, User user, Agency agency);
    public abstract Proposal getProposal(String proposalName);
    public abstract void removeProposal(String itineraryID);
}
