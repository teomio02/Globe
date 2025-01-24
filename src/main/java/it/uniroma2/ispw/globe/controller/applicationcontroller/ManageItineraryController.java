package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.ItineraryType;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.Session;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

import static it.uniroma2.ispw.globe.other.UserType.AGENCY;

public class ManageItineraryController {

    private static final String CITY = "administrative";
    private static final String ATTRACTION = "";

    public void removeItinerary(ItineraryBean itineraryBean, UserBean userBean) {/*-*/}

    public void editItinerary(ItineraryBean itineraryBean, UserBean userBean) {/*-*/}

    public ProposalBean getProposal(String proposalID, String sessionID) {
        Proposal proposal;

        if (proposalID == null) {
            proposal = SessionManager.getInstance().getSession(sessionID).getPendingProposal();
        } else {
            ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
            proposal = proposalDao.getProposal(proposalID);
        }

        return new ProposalBean(proposalID,proposal.getPrice(),proposal.getAgency().getUsername(),proposal.getUser().getUsername(),proposal.getDescription(),proposal.getAccepted());
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
        User user = (User) SessionManager.getInstance().getSession(sessionId).getAccount();
        List<Proposal> proposals = user.getProposals();
        List<ProposalBean> proposalBeans = new ArrayList<>();
        for (Proposal proposal : proposals) {
            ProposalBean proposalBean = new ProposalBean(proposal.getId(),proposal.getPrice(),proposal.getAgency().getUsername(),proposal.getUser().getUsername(),proposal.getDescription(),proposal.getAccepted());
            proposalBeans.add(proposalBean);
        }
        return proposalBeans;
    }

    public String getAccountType(String sessionId) {
        Account account = SessionManager.getInstance().getSession(sessionId).getAccount();
        return account.getType();
    }

}
