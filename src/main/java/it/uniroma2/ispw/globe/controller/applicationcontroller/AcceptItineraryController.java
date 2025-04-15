package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.dao.AccountDao;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;

import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;
import static it.uniroma2.ispw.globe.other.ProposalState.ACCEPTED;


public class AcceptItineraryController {

    public String sendResponse(String proposalId, String response) throws FailedOperationException, DuplicateItemException {
        ProposalDao proposalDao = Persistence.getFactory(Persistence.getInstance().getType()).getProposalDao();
        AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
        try {
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
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Send response");
        }
    }

    public String executePayment(String userUsername, String agencyUsername, double amount) throws FailedOperationException, DuplicateItemException {

        try {
            Account payer = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao().getAccount(userUsername);
            Account payee = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao().getAccount(agencyUsername);

            PaymentApi api = new PaymentApi();

            return api.processPayment(payer.getPaymentCredential(), payee.getPaymentCredential(),amount);

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Execute payment");
        }
    }

    public ItineraryBean getProposalItinerary(String proposalId) throws FailedOperationException, DuplicateItemException {
        try {
            ProposalDao proposalDao = Persistence.getFactory(Persistence.getInstance().getType()).getProposalDao();
            Proposal proposal = proposalDao.getProposal(proposalId);
            Itinerary itinerary = proposal.getItinerary();

            return new ItineraryBean(itinerary.getItineraryID(),itinerary.getName(), itinerary.getDescription(), itinerary.getTypes(), itinerary.getDaysNumber());

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Get proposal's itinerary");
        }
    }
}
