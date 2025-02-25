package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;

import static it.uniroma2.ispw.globe.other.ProposalState.ACCEPTED;


public class AcceptItineraryController {

    public String sendResponse(String proposalId, String response) {
        ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
        Proposal proposal = proposalDao.getProposal(proposalId);
        String paymentResult = null;

        if (response.equals(ACCEPTED)) {
            paymentResult = executePayment(proposal.getUser().getUsername(),proposal.getAgency().getUsername(),proposal.getPrice());
        }

        proposal.setAccepted(response);
        proposalDao.updateProposal(proposal);

        return paymentResult;

    }

    public String executePayment(String userUsername, String agencyUsername, double amount) {
        Account payer = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao().getAccount(userUsername);
        Account payee = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao().getAccount(agencyUsername);

        PaymentApi api = new PaymentApi();

        return api.processPayment(payer.getPaymentCredential(), payee.getPaymentCredential(),amount);
    }

    public ItineraryBean getProposalItinerary(String proposalId) {
        ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
        Proposal proposal = proposalDao.getProposal(proposalId);
        Itinerary itinerary = proposal.getItinerary();

        return new ItineraryBean(itinerary.getItineraryID(),itinerary.getName(), itinerary.getDescription(), itinerary.getTypes(), itinerary.getDaysNumber());
    }
}
