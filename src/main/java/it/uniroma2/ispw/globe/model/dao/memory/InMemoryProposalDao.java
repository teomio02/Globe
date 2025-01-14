package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryProposalDao extends ProposalDao {
    private static InMemoryProposalDao instance = null;

    List<Proposal> proposals = new ArrayList<>();

    private InMemoryProposalDao() {}

    public static InMemoryProposalDao getInstance() {
        if (instance == null) {
            instance = new InMemoryProposalDao();
        }
        return instance;
    }

    @Override
    public void addProposal(Proposal proposal, User user) {
        for (Proposal savedProposal : proposals) {
            if (proposal.getId().equals(proposal.getId())){
                // proposta già esistente
                return;
            }
        }
        proposals.add(proposal);
        user.getProposals().add(proposal);
    }

    @Override
    public Proposal getProposal(String proposalID) {
        for (Proposal proposal : proposals) {
            
        }
        return null;
    }

    @Override
    public void removeProposal(String proposalID) {

    }
}
