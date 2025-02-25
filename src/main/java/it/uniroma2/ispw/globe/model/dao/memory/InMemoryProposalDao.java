package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;

import java.util.ArrayList;
import java.util.List;

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
    public void addProposal(Proposal proposal) {
        for (Proposal savedProposal : proposals) {
            if (proposal.getId().equals(savedProposal.getId())){
                // proposta già esistente
                return;
            }
        }
        proposals.add(proposal);
        proposal.getUser().getProposals().add(proposal);
        proposal.getAgency().getProposals().add(proposal);
    }

    @Override
    public Proposal getProposal(String proposalID) {
        for (Proposal proposal : proposals) {
            if (proposal.getId().equals(proposalID)){
                return proposal;
            }
        }
        return null;
    }

    @Override
    public void updateProposal(Proposal proposal) {

    }
}
