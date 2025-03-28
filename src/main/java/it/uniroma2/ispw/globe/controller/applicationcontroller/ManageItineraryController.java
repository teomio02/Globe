package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.session.Session;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;
import java.util.*;

public class ManageItineraryController {

    public ProposalBean getProposal(String proposalID, String sessionID) {
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
            ProposalDao proposalDao = DaoFactory.getFactory().getProposalDao();
            AccountDao accountDao = DaoFactory.getFactory().getAccountDao();
            proposal = proposalDao.getProposal(proposalID);
            user = accountDao.getUserByProposal(proposalID);
            agency = accountDao.getAgencyByProposal(proposalID);
        }

        return new ProposalBean(proposalID,proposal.getPrice(),agency.getUsername(),user.getUsername(),proposal.getDescription(),proposal.getAccepted());
    }

    public List<ItineraryBean> getUserItineraries(String sessionId) {
        User user = (User) SessionManager.getInstance().getSession(sessionId).getAccount();
        List<Itinerary> itineraries = user.getItineraries();
        List<ItineraryBean> itineraryBeans = new ArrayList<>();
        for (Itinerary itinerary : itineraries) {
            ItineraryBean itineraryBean = new ItineraryBean(itinerary.getItineraryID(),itinerary.getName(),itinerary.getDescription(),itinerary.getTypes(),itinerary.getDaysNumber());
            itineraryBeans.add(itineraryBean);
        }
        return itineraryBeans;
    }

    public List<ProposalBean> getUserProposals(String sessionId) {
        AccountDao accountDao = DaoFactory.getFactory().getAccountDao();
        User user = (User) SessionManager.getInstance().getSession(sessionId).getAccount();
        List<Proposal> proposals = user.getProposals();
        List<ProposalBean> proposalBeans = new ArrayList<>();
        for (Proposal proposal : proposals) {
            Agency agency = accountDao.getAgencyByProposal(proposal.getId());
            ProposalBean proposalBean = new ProposalBean(proposal.getId(),proposal.getPrice(),agency.getUsername(),user.getUsername(),proposal.getDescription(),proposal.getAccepted());
            proposalBeans.add(proposalBean);
        }
        return proposalBeans;
    }

    public String getAccountType(String sessionId) {
        Account account = SessionManager.getInstance().getSession(sessionId).getAccount();
        return account.getType();
    }

}
