package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;
import it.uniroma2.ispw.globe.model.dao.RequestDao;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResponseRequestController {

    public void createProposal(ProposalBean proposalBean, String requestId, String sessionID) {
        ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
        RequestDao requestDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getRequestDao();

        String proposalID = UUID.randomUUID().toString();
        proposalBean.setID(proposalID);

        Agency agency = (Agency) SessionManager.getInstance().getSession(sessionID).getAccount();
        Proposal proposal = proposalDao.createProposal(proposalBean,null,getUser(proposalBean.getUser()),agency);

        agency.setNewProposal(proposal);
    }

    public String saveProposal(ProposalBean proposalBean, String requestId, String sessionID) {
        ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
        RequestDao requestDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getRequestDao();

        String proposalID = UUID.randomUUID().toString();
        proposalBean.setID(proposalID);

        Agency agency = (Agency) SessionManager.getInstance().getSession(sessionID).getAccount();
        Proposal proposal = proposalDao.createProposal(proposalBean,null,getUser(proposalBean.getUser()),agency);

        // da aggiungere in altra funzione
        proposalDao.addProposal(proposal,getUser(proposalBean.getUser()));

        requestDao.removeRequest(requestId);

        return proposalID;
    }

    public void addItineraryToProposal(String sessionID) {
//        ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
//        ItineraryDao itineraryDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getItineraryDao();
//
//
//        Proposal proposal = proposalDao.getProposal(proposalId);
//        Itinerary itinerary = itineraryDao.getItinerary(itineraryId);
//
//        proposal.setItinerary(itinerary);


    }

    public List<ProposalBean> getAgencyProposals(String sessionId) {

        List<ProposalBean> proposalBeans = new ArrayList<>();

        return proposalBeans;
    }

    public List<RequestBean> getAgencyRequests(String sessionId) {

        List<RequestBean> requestBeans = new ArrayList<>();
        
        return requestBeans;
    }

    public User getUser(String id) {

        return new User();
    }
}
