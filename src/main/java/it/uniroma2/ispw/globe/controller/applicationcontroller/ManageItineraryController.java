package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.Session;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;

public class ManageItineraryController {

    public ProposalBean getProposal(String proposalID, String sessionID) throws FailedOperationException, DuplicateItemException, IncorrectDataException {
        try {
            Proposal proposal;
            User user;
            Agency agency;

            if (proposalID == null) {
                Session session = SessionManager.getInstance().getSession(sessionID);
                proposal = session.getPendingProposal();
                if (session.getPendingAccount() instanceof User pendingAccount) {
                    user = pendingAccount;
                    agency = (Agency) session.getAccount();
                } else {
                    user = (User) session.getAccount();
                    agency = (Agency) session.getPendingAccount();
                }

            } else {
                ProposalDao proposalDao = Persistence.getFactory(Persistence.getInstance().getType()).getProposalDao();
                AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
                proposal = proposalDao.getProposal(proposalID);
                user = accountDao.getUserByProposal(proposalID);
                agency = accountDao.getAgencyByProposal(proposalID);
            }

            ProposalBean proposalBean = new ProposalBean();
            proposalBean.setID(proposalID);
            proposalBean.setPrice(proposal.getPrice());
            proposalBean.setAgency(agency.getUsername());
            proposalBean.setUser(user.getUsername());
            proposalBean.setDescription(proposal.getDescription());
            proposalBean.setAccepted(proposal.getAccepted());

            return proposalBean;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Get proposal");
        }
    }

    public List<ItineraryBean> getUserItineraries(String sessionId) throws IncorrectDataException {
        User user = (User) SessionManager.getInstance().getSession(sessionId).getAccount();
        List<Itinerary> itineraries = user.getItineraries();
        List<ItineraryBean> itineraryBeans = new ArrayList<>();
        for (Itinerary itinerary : itineraries) {
            ItineraryBean itineraryBean = new ItineraryBean();
            itineraryBean.setId(itinerary.getItineraryID());
            itineraryBean.setName(itinerary.getName());
            itineraryBean.setDescription(itinerary.getDescription());
            itineraryBean.setTypes(itinerary.getTypes());
            itineraryBean.setDuration(itinerary.getDaysNumber());

            itineraryBeans.add(itineraryBean);
        }
        return itineraryBeans;
    }

    public List<ProposalBean> getUserProposals(String sessionId) throws FailedOperationException, DuplicateItemException, IncorrectDataException {
        try {
            AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
            User user = (User) SessionManager.getInstance().getSession(sessionId).getAccount();
            List<Proposal> proposals = user.getProposals();
            List<ProposalBean> proposalBeans = new ArrayList<>();
            for (Proposal proposal : proposals) {
                Agency agency = accountDao.getAgencyByProposal(proposal.getId());

                ProposalBean proposalBean = new ProposalBean();
                proposalBean.setID(proposal.getId());
                proposalBean.setPrice(proposal.getPrice());
                proposalBean.setAgency(agency.getUsername());
                proposalBean.setUser(user.getUsername());
                proposalBean.setDescription(proposal.getDescription());
                proposalBean.setAccepted(proposal.getAccepted());

                proposalBeans.add(proposalBean);
            }
            return proposalBeans;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Get user's proposal");
        }
    }

    public String getAccountType(String sessionId) {
        Account account = SessionManager.getInstance().getSession(sessionId).getAccount();
        return account.getType();
    }
}
