package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.dao.*;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.session.Session;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.engineering.decorator.AccommodationDecorator;
import it.uniroma2.ispw.globe.engineering.decorator.FlightDecorator;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.engineering.decorator.ItineraryDecorator;
import javafx.util.Pair;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;

public class ManageItineraryController {

    public ProposalBean getProposal(String proposalID, String sessionID) throws FailedOperationException, IncorrectDataException {
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
                ProposalDao proposalDao = Persistence.getInstance().getFactory().getProposalDao();
                AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
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
            throw new FailedOperationException("Get proposal");
        }
    }

    public ItineraryBean getItinerary(String itineraryId, String sessionID) throws FailedOperationException, IncorrectDataException {
        try {
            Itinerary itinerary;

            if (itineraryId == null) {
                itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
                if (itinerary == null) {
                    return null;
                }
            } else {
                ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();
                itinerary = itineraryDao.getItinerary(itineraryId);
            }

            if (itinerary == null) {
                throw new FailedOperationException("Get itinerary - itinerary not found");
            }

            ItineraryBean itineraryBean = createItineraryBean(itinerary);

            itineraryBean.setInboundFlightDepartureTime(-1);
            itineraryBean.setInboundFlightArrivalTime(-1);
            itineraryBean.setOutboundFlightDepartureTime(-1);
            itineraryBean.setOutboundFlightArrivalTime(-1);

            Itinerary current = itinerary;
            while (current instanceof ItineraryDecorator itineraryDecorator) {
                if (current instanceof AccommodationDecorator accommodationDecorator) {
                    List<Pair<String,String>> accommodations = new ArrayList<>();
                    for (Accommodation accommodation : accommodationDecorator.getAccommodations()) {
                        accommodations.add(new Pair<>(accommodation.getName(), accommodation.getAddress()));
                    }
                    itineraryBean.setAccommodations(accommodations);
                }
                if (current instanceof FlightDecorator flightDecorator) {
                    Flight inFlight = flightDecorator.getInFlight();
                    Flight outFlight = flightDecorator.getOutFlight();
                    double inDepartureTime = inFlight.getDepartureTime();
                    double inArrivalTime =inFlight.getArrivalTime();
                    double outDepartureTime = outFlight.getDepartureTime();
                    double outArrivalTime = outFlight.getArrivalTime();

                    itineraryBean.setInboundFlightDepartureTime(inDepartureTime);
                    itineraryBean.setInboundFlightArrivalTime(inArrivalTime);
                    itineraryBean.setOutboundFlightDepartureTime(outDepartureTime);
                    itineraryBean.setOutboundFlightArrivalTime(outArrivalTime);
                }
                current = itineraryDecorator.getItinerary();
            }

            return itineraryBean;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get itinerary");
        }
    }

    public List<StepBean> getSteps(String itineraryId, String sessionID) throws FailedOperationException, IncorrectDataException {
        try {
            List<StepBean> steps = new ArrayList<>();
            Itinerary itinerary;

            if (itineraryId == null) {
                itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
            } else {
                ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();
                itinerary = itineraryDao.getItinerary(itineraryId);
            }

            List<Day> days = itinerary.getDays();
            for (Day day : days) {
                List<String> attractions = new ArrayList<>();
                for (Attraction attraction : day.getAttractions()) {
                    attractions.add(attraction.getPlaceID());
                }
                List<String> cities = new ArrayList<>();
                for (City city : day.getCities()) {
                    cities.add(city.getPlaceID());
                }
                StepBean stepBean = new StepBean();
                stepBean.setNum(day.getDayNum()-1);
                stepBean.setAttractions(attractions);
                stepBean.setCity(cities);
                steps.add(stepBean);
            }
            return steps;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get steps");
        }
    }

    public CityBean getCity(int stepNum, String cityID, String sessionID) throws FailedOperationException {
        try {
            CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
            City city = null;

            if (sessionID != null) {
                Itinerary itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
                for (City savedCity : itinerary.getDays().get(stepNum).getCities()) {
                    if (cityID.equals(savedCity.getPlaceID())) {
                        city = savedCity;
                    }
                }
            } else {
                city = cityDao.getCity(cityID);
                if (city == null) {
                    city = cityDao.createCity(cityID);
                }
            }

            if (city != null) {
                CityBean cityBean = new CityBean();
                cityBean.setId(city.getPlaceID());
                cityBean.setName(city.getName());
                cityBean.setCountry(city.getCountry());
                return cityBean;
            } else {
                return null;
            }
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get city");
        }
    }

    public AttractionBean getAttraction(int stepNum, String attractionID, String sessionID) throws FailedOperationException {
        try {
            AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();
            Attraction attraction = null;

            if (sessionID != null) {
                Itinerary itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
                for (Attraction savedeAttraction : itinerary.getDays().get(stepNum).getAttractions()) {
                    if (attractionID.equals(savedeAttraction.getPlaceID())) {
                        attraction = savedeAttraction;
                    }
                }
            } else {
                attraction = attractionDao.getAttraction(attractionID);
                if (attraction == null) {
                    attraction = attractionDao.createAttraction(attractionID);
                }
            }

            if (attraction != null) {
                AttractionBean attractionBean = new AttractionBean();
                attractionBean.setId(attraction.getPlaceID());
                attractionBean.setName(attraction.getName());
                attractionBean.setAddress(attraction.getAddress());
                attractionBean.setCity(attraction.getCity());

                return attractionBean;
            } else {
                return null;
            }
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Get attraction");
        }
    }

    public List<ItineraryBean> getUserItineraries(String sessionId) throws IncorrectDataException, FailedOperationException {
        Session session = SessionManager.getInstance().getSession(sessionId);
        if (session == null) {
            throw new FailedOperationException("GetUserItineraries - Session not found");
        }
        User user = (User) session.getAccount();
        List<Itinerary> itineraries = user.getItineraries();
        List<ItineraryBean> itineraryBeans = new ArrayList<>();
        for (Itinerary itinerary : itineraries) {
            ItineraryBean itineraryBean = createItineraryBean(itinerary);

            itineraryBeans.add(itineraryBean);
        }
        return itineraryBeans;
    }

    public List<ProposalBean> getUserProposals(String sessionId) throws FailedOperationException, IncorrectDataException {
        try {
            AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
            Session session = SessionManager.getInstance().getSession(sessionId);
            if (session == null) {
                throw new FailedOperationException("GetUserProposals - Session not found");
            }
            User user = (User) session.getAccount();
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
            throw new FailedOperationException("Get user's proposal");
        }
    }

    public String getAccountType(String sessionId) {
        Account account = SessionManager.getInstance().getSession(sessionId).getAccount();
        return account.getType();
    }

    public ItineraryBean createItineraryBean(Itinerary itinerary) throws IncorrectDataException {
        ItineraryBean itineraryBean = new ItineraryBean();
        itineraryBean.setId(itinerary.getItineraryID());
        itineraryBean.setName(itinerary.getName());
        itineraryBean.setDescription(itinerary.getDescription());
        itineraryBean.setTypes(itinerary.getTypes());
        itineraryBean.setDuration(itinerary.getDaysNumber());
        itineraryBean.setPhoto(itinerary.getPhotoFile());

        return itineraryBean;
    }
}
