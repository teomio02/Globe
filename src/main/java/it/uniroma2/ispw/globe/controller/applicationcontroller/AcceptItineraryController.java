package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.dao.AccountDao;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;

import static it.uniroma2.ispw.globe.other.ProposalState.ACCEPTED;


public class AcceptItineraryController {

    public String sendResponse(String proposalId, String response) throws ItemNotFoundException {
        ProposalDao proposalDao = Persistence.getFactory(Persistence.getInstance().getType()).getProposalDao();
        AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
        Agency agency = accountDao.getAgencyByProposal(proposalId);
        User user = accountDao.getUserByProposal(proposalId);

        Proposal proposal = proposalDao.getProposal(proposalId);
        String paymentResult = null;

        if (response.equals(ACCEPTED)) {
            paymentResult = executePayment(user.getUsername(),agency.getUsername(),proposal.getPrice());
        }

        proposal.setAccepted(response);
        proposalDao.updateProposal(proposal);

        return paymentResult;

    }

    public String executePayment(String userUsername, String agencyUsername, double amount) throws ItemNotFoundException {

        Account payer = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao().getAccount(userUsername);
        Account payee = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao().getAccount(agencyUsername);

        PaymentApi api = new PaymentApi();

        return api.processPayment(payer.getPaymentCredential(), payee.getPaymentCredential(),amount);
    }

    public ItineraryBean getProposalItinerary(String proposalId) throws ItemNotFoundException {
        ProposalDao proposalDao = Persistence.getFactory(Persistence.getInstance().getType()).getProposalDao();
        Proposal proposal = proposalDao.getProposal(proposalId);
        Itinerary itinerary = proposal.getItinerary();

        return new ItineraryBean(itinerary.getItineraryID(),itinerary.getName(), itinerary.getDescription(), itinerary.getTypes(), itinerary.getDaysNumber());
    }
}
