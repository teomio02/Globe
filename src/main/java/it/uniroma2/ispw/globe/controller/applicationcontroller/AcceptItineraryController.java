package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.bean.PaymentBean;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;
import it.uniroma2.ispw.globe.other.Persistence;


public class AcceptItineraryController {

    public void sendResponse(String proposalId, boolean response) {
        ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
        Proposal proposal = proposalDao.getProposal(proposalId);

        if (response) {
            executePayment();
        }

        proposal.setAccepted(response);

    }

    public void executePayment() {

    }
}
