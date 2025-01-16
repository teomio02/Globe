package it.uniroma2.ispw.globe.model;

import java.util.List;

public class User extends Account{;
    private List<Proposal> proposals;

    public List<Proposal> getProposals() {
        return proposals;
    }
    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
    }
}
