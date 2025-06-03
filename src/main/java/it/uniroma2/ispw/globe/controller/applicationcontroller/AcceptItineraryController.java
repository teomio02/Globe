package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.dao.AccountDao;
import it.uniroma2.ispw.globe.dao.ProposalDao;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.model.Itinerary;

import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;
import static it.uniroma2.ispw.globe.constants.ProposalState.ACCEPTED;


public class AcceptItineraryController {

    public String sendResponse(String proposalId, String response, String sessionId) throws FailedOperationException, DuplicateItemException {
        ProposalDao proposalDao = Persistence.getFactory(Persistence.getInstance().getType()).getProposalDao();
        AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
        try {
            Agency agency = accountDao.getAgencyByProposal(proposalId);
            User user = accountDao.getUserByProposal(proposalId);

            Proposal proposal = proposalDao.getProposal(proposalId);
            String paymentResult = null;

            if (response.equals(ACCEPTED)) {
                paymentResult = executePayment(user.getUsername(), agency.getUsername(), proposal.getPrice());
            }

            proposal.setAccepted(response);
            proposalDao.updateProposal(proposal);

            SessionManager.getInstance().getSession(sessionId).setAccount(accountDao.getAccount(user.getUsername()));

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
            AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
            Account payer = accountDao.getAccount(userUsername);
            Account payee = accountDao.getAccount(agencyUsername);

            PaymentApi api = new PaymentApi();

            return api.processPayment(payer.getPaymentCredential(), payee.getPaymentCredential(), amount);

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Execute payment");
        }
    }

    public ItineraryBean getProposalItinerary(String proposalId) throws FailedOperationException, IncorrectDataException {
        try {
            ProposalDao proposalDao = Persistence.getFactory(Persistence.getInstance().getType()).getProposalDao();
            Proposal proposal = proposalDao.getProposal(proposalId);
            if (proposal == null) {
                throw new FailedOperationException("Get proposal's itinerary - proposal not found");
            }
            Itinerary itinerary = proposal.getItinerary();

            ItineraryBean itineraryBean = new ItineraryBean();
            itineraryBean.setId(itinerary.getItineraryID());
            itineraryBean.setName(itinerary.getName());
            itineraryBean.setDescription(itinerary.getDescription());
            itineraryBean.setTypes(itinerary.getTypes());
            itineraryBean.setDuration(itinerary.getDaysNumber());
            itineraryBean.setPhoto(itinerary.getPhotoFile());

            return itineraryBean;

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get proposal's itinerary");
        }
    }

    public void addRaiting(Double rating, String proposalId) throws FailedOperationException {
        AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
        try {
            Agency agency = accountDao.getAgencyByProposal(proposalId);
            agency.setRating(rating);

            // update agency rating

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Send response");
        }
    }
}