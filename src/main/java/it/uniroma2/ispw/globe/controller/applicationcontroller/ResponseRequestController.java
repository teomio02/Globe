package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.Session;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;
import it.uniroma2.ispw.globe.util.decorator.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static it.uniroma2.ispw.globe.other.ProposalState.ACCEPTED;

public class ResponseRequestController {

    public void createProposal(ProposalBean proposalBean, String userUsername, String requestId, String sessionID) {
        ProposalDao proposalDao = Persistence.getFactory(Persistence.getInstance().getType()).getProposalDao();
        RequestDao requestDao = Persistence.getFactory(Persistence.getInstance().getType()).getRequestDao();
        AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();

        User user = (User) accountDao.getAccount(userUsername);
        String proposalID = UUID.randomUUID().toString();
        proposalBean.setID(proposalID);

        Session session = SessionManager.getInstance().getSession(sessionID);

        Proposal proposal = proposalDao.createProposal(proposalID,proposalBean.getPrice(),proposalBean.getDescription(),session.getPendingItinerary());

        Request request = requestDao.getRequest(requestId);
        request.setAccepted(ACCEPTED);

        session.setPendingProposal(proposal);
        session.setPendingRequest(request);
        session.setPendingAccount(user);

    }

    public void saveProposal(String sessionID) {
        ItineraryDao itineraryDao = Persistence.getFactory(Persistence.getInstance().getType()).getItineraryDao();
        ProposalDao proposalDao = Persistence.getFactory(Persistence.getInstance().getType()).getProposalDao();
        RequestDao requestDao = Persistence.getFactory(Persistence.getInstance().getType()).getRequestDao();

        Session session = SessionManager.getInstance().getSession(sessionID);
        Agency agency = (Agency) session.getAccount();
        User user = (User) session.getPendingAccount();

        Proposal proposal = session.getPendingProposal();
        Itinerary itinerary = session.getPendingItinerary();
        itineraryDao.addItinerary(itinerary, agency);

        proposal.setItinerary(session.getPendingItinerary());
        proposalDao.addProposal(proposal,user,agency);

        Request request = session.getPendingRequest();
        request.setAccepted(ACCEPTED);
        requestDao.updateRequest(request);

        session.setPendingItinerary(null);
        session.setPendingProposal(null);
        session.setPendingRequest(null);
    }

    public ProposalBean getProposal(String proposalID, String sessionID) {
        Proposal proposal;
        User user;
        Agency agency;

        if (proposalID == null) {
            Session session = SessionManager.getInstance().getSession(sessionID);
            proposal = session.getPendingProposal();
            if (session.getAccount() instanceof Agency account) {
                agency = account;
                user = (User) session.getPendingAccount();
            } else {
                user = (User) session.getAccount();
                agency = (Agency) session.getPendingAccount();
            }

            if (proposal == null) {
                return null;
            }
        } else {
            ProposalDao proposalDao = Persistence.getFactory(Persistence.getInstance().getType()).getProposalDao();
            AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
            proposal = proposalDao.getProposal(proposalID);
            agency = accountDao.getAgencyByProposal(proposalID);
            user = accountDao.getUserByProposal(proposalID);
        }

        return new ProposalBean(proposalID,proposal.getPrice(),agency.getUsername(),user.getUsername(),proposal.getDescription(),proposal.getAccepted());
    }

    public AgencyRequestBean getAgencyRequest(String requestID, String sessionID) {
        Request request;
        Agency agency;
        User user;

        if (requestID == null) {
            Session session = SessionManager.getInstance().getSession(sessionID);
            request = session.getPendingRequest();

            if (session.getAccount() instanceof Agency account) {
                agency = account;
                user = (User) session.getPendingAccount();
            } else {
                user = (User) session.getAccount();
                agency = (Agency) session.getPendingAccount();
            }
        } else {
            RequestDao requestDao = Persistence.getFactory(Persistence.getInstance().getType()).getRequestDao();
            AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();

            request = requestDao.getRequest(requestID);
            agency = accountDao.getAgencyByRequest(requestID);
            user = accountDao.getUserByRequest(requestID);
        }

        List<String> cities = new ArrayList<>();
        for (City city : request.getCities()) {
            cities.add(city.getPlaceID());
        }
        List<String> attractions = new ArrayList<>();
        for (Attraction attraction : request.getAttractions()) {
            attractions.add(attraction.getPlaceID());
        }

        AgencyRequestBean requestBean = new AgencyRequestBean(requestID,user.getUsername(),agency.getUsername(),request.getOtherRequest(),request.getDayNum(),request.getItineraryType(),request.getAccepted());
        requestBean.setCities(cities);
        requestBean.setAttractions(attractions);

        return requestBean;
    }

    public List<ProposalBean> getAgencyProposals(String sessionID) {

        AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();

        Agency agency = (Agency) SessionManager.getInstance().getSession(sessionID).getAccount();
        List<Proposal> proposals = agency.getProposals();
        List<ProposalBean> proposalBeans = new ArrayList<>();
        for (Proposal proposal : proposals) {
            User user = accountDao.getUserByProposal(proposal.getId());
            ProposalBean proposalBean = new ProposalBean(proposal.getId(),proposal.getPrice(),agency.getUsername(),user.getUsername(),proposal.getDescription(),proposal.getAccepted());
            proposalBeans.add(proposalBean);
        }

        return proposalBeans;
    }

    public List<AgencyRequestBean> getAgencyRequests(String sessionID) {

        AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();

        Agency agency = (Agency) SessionManager.getInstance().getSession(sessionID).getAccount();
        List<Request> requests = agency.getRequests();
        List<AgencyRequestBean> requestBeans = new ArrayList<>();
        for (Request request : requests) {
            User user = accountDao.getUserByRequest(request.getId());
            AgencyRequestBean requestBean = new AgencyRequestBean(request.getId(),user.getUsername(), agency.getUsername(), request.getOtherRequest(), request.getDayNum(), request.getItineraryType(), request.getAccepted());
            requestBeans.add(requestBean);
        }
        return requestBeans;
    }

    public void setPendingRequest(String sessionID,String requestID) {
        RequestDao requestDao = Persistence.getFactory(Persistence.getInstance().getType()).getRequestDao();
        Request request = requestDao.getRequest(requestID);

        SessionManager.getInstance().getSession(sessionID).setPendingRequest(request);
    }
}
