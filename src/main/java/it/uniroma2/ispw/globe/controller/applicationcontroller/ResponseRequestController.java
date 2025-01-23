package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;
import it.uniroma2.ispw.globe.model.dao.RequestDao;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.Session;
import it.uniroma2.ispw.globe.other.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static it.uniroma2.ispw.globe.other.ProposalState.ACCEPTED;

public class ResponseRequestController {

    public void createProposal(ProposalBean proposalBean, String requestId, String sessionID) {
        ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
        RequestDao requestDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getRequestDao();

        String proposalID = UUID.randomUUID().toString();
        proposalBean.setID(proposalID);

        Session session = SessionManager.getInstance().getSession(sessionID);
        Agency agency = (Agency) session.getAccount();

        Proposal proposal = proposalDao.createProposal(proposalID,proposalBean.getPrice(),proposalBean.getDescription(),session.getPendingItinerary(),session.getPendingRequest().getUser(),agency);

        Request request = requestDao.getRequest(requestId);
        request.setAccepted(ACCEPTED);

        session.setPendingProposal(proposal);
        session.setPendingRequest(request);

    }

    public void saveProposal(String sessionID) {
        ItineraryDao itineraryDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getItineraryDao();
        ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
        RequestDao requestDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getRequestDao();

        Session session = SessionManager.getInstance().getSession(sessionID);
        Account agency = session.getAccount();

        Proposal proposal = session.getPendingProposal();
        Itinerary itinerary = session.getPendingItinerary();
        itineraryDao.addItinerary(itinerary,agency);

        proposal.setItinerary(session.getPendingItinerary());
        System.out.println(proposal.getId()+" - "+proposal.getAgency().getUsername()+" - "+proposal.getUser().getUsername());
        proposalDao.addProposal(proposal);

        Request request = session.getPendingRequest();
        request.setAccepted(ACCEPTED);

        session.setPendingItinerary(null);
        session.setPendingProposal(null);
        session.setPendingRequest(null);
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

    public ProposalBean getProposal(String proposalID, String sessionID) {
        Proposal proposal;

        if (proposalID == null) {
            proposal = SessionManager.getInstance().getSession(sessionID).getPendingProposal();
            if (proposal == null) {
                return null;
            }
        } else {
            ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
            proposal = proposalDao.getProposal(proposalID);
        }

        return new ProposalBean(proposalID,proposal.getPrice(),proposal.getAgency().getUsername(),proposal.getUser().getUsername(),proposal.getDescription(),proposal.getAccepted());
    }

    public AgencyRequestBean getAgencyRequest(String requestID, String sessionID) {
        Request request;

        if (requestID == null) {
            request = SessionManager.getInstance().getSession(sessionID).getPendingRequest();
        } else {
            RequestDao requestDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getRequestDao();
            request = requestDao.getRequest(requestID);
        }

        List<String> cities = new ArrayList<>();
        for (City city : request.getCities()) {
            cities.add(city.getPlaceID());
        }
        List<String> attractions = new ArrayList<>();
        for (Attraction attraction : request.getAttractions()) {
            attractions.add(attraction.getPlaceID());
        }

        return new AgencyRequestBean(requestID,cities,attractions,request.getUser().getUsername(),request.getAgency().getUsername(),request.getDescription(),request.getDays(),request.getTypes(),request.getAccepted());

    }

    public List<ProposalBean> getAgencyProposals(String sessionID) {

        Agency agency = (Agency) SessionManager.getInstance().getSession(sessionID).getAccount();
        List<Proposal> proposals = agency.getProposals();
        List<ProposalBean> proposalBeans = new ArrayList<>();
        for (Proposal proposal : proposals) {
            ProposalBean proposalBean = new ProposalBean(proposal.getId(),proposal.getPrice(),proposal.getAgency().getUsername(),proposal.getUser().getUsername(),proposal.getDescription(),proposal.getAccepted());
            proposalBeans.add(proposalBean);
        }

        return proposalBeans;
    }

    public List<AgencyRequestBean> getAgencyRequests(String sessionID) {

        Agency agency = (Agency) SessionManager.getInstance().getSession(sessionID).getAccount();
        List<Request> requests = agency.getRequests();
        List<AgencyRequestBean> requestBeans = new ArrayList<>();
        for (Request request : requests) {
            AgencyRequestBean requestBean = new AgencyRequestBean(request.getId(),request.getUser().getUsername(), request.getAgency().getUsername(), request.getDescription(), request.getDays(), request.getTypes(), request.getAccepted());
            requestBeans.add(requestBean);
        }
        return requestBeans;
    }

    public User getUser(String id) {

        return new User();
    }

    public void setPendingRequest(String sessionID,String requestID) {
        RequestDao requestDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getRequestDao();
        Request request = requestDao.getRequest(requestID);

        SessionManager.getInstance().getSession(sessionID).setPendingRequest(request);
    }
}
