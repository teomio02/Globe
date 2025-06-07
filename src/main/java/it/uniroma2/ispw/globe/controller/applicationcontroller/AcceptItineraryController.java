package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.bean.PaymentBean;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.dao.AccountDao;
import it.uniroma2.ispw.globe.dao.ProposalDao;
import it.uniroma2.ispw.globe.engineering.Persistence;

import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;
import static it.uniroma2.ispw.globe.constants.ProposalState.ACCEPTED;


public class AcceptItineraryController {

    public PaymentBean sendResponse(String proposalId, String response, String sessionId) throws FailedOperationException, DuplicateItemException, IncorrectDataException {
        ProposalDao proposalDao = Persistence.getInstance().getFactory().getProposalDao();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        try {
            Proposal proposal = proposalDao.getProposal(proposalId);
            PaymentBean paymentResult = null;

            if (response.equals(ACCEPTED)) {
                paymentResult = executePayment(proposal);
            }

            proposal.setAccepted(response);
            proposalDao.updateProposal(proposal);

            return paymentResult;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Send response");
        }
    }

    public PaymentBean executePayment(Proposal proposal) throws FailedOperationException, DuplicateItemException, IncorrectDataException {

        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();

        try {
            Agency payee = accountDao.getAgencyByProposal(proposal.getId());
            User payer = accountDao.getUserByProposal(proposal.getId());

            PaymentBean paymentResult = null;

            if (new PaymentApi().processPayment(proposal.getPrice(),payer,payee)) {
                paymentResult = new PaymentBean();
                paymentResult.setAmount(proposal.getPrice());
                paymentResult.setPayeeUsername(payee.getUsername());
                paymentResult.setPayerUsername(payer.getUsername());
            }

            return paymentResult;

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Execute payment");
        }
    }

    public ItineraryBean getProposalItinerary(String proposalId) throws FailedOperationException, IncorrectDataException {
        try {
            ProposalDao proposalDao = Persistence.getInstance().getFactory().getProposalDao();
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
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        try {

            Agency agency = accountDao.getAgencyByProposal(proposalId);
            double curRating = agency.getRating();
            if (curRating == 0) {
                agency.setRating(rating);
            } else {
                agency.setRating((curRating + rating)/2);
            }

            accountDao.updateAgencyRating(agency);

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Send response");
        }
    }
}