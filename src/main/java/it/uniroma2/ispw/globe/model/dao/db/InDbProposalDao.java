package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;

public class InDbProposalDao extends ProposalDao {

    @Override
    public void addProposal(Proposal proposal, User user) {

    }

    @Override
    public Proposal getProposal(String proposalName) {
        return null;
    }

    @Override
    public void removeProposal(String itineraryID) {

    }
}
