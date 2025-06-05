package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.bean.CredentialsBean;
import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.dao.*;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.uniroma2.ispw.globe.constants.ItineraryType.CULTURE;
import static it.uniroma2.ispw.globe.constants.ProposalState.PENDING;
import static it.uniroma2.ispw.globe.constants.UserType.AGENCY;
import static it.uniroma2.ispw.globe.constants.UserType.USER;
import static org.junit.jupiter.api.Assertions.*;

class ManageItineraryControllerTest {

    // Teo Miozzi

    @Test
    void testGetItineraryCorrectID() throws FailedOperationException, IncorrectDataException, DaoException {
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        String sessionID = SessionManager.getInstance().addSession(accountDao.getAccount("userTest"));

        ManageItineraryController controller = new ManageItineraryController();
        String itineraryID = "id1234";

        ItineraryBean itinerary = controller.getItinerary(itineraryID,sessionID);
        assertNotNull(itinerary);
    }

    @Test
    void testGetProposalItineraryIncorrectID() throws IncorrectDataException, DaoException {
        ManageItineraryController controller = new ManageItineraryController();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        String sessionID = SessionManager.getInstance().addSession(accountDao.getAccount("userTest"));
        String itineraryID = "id0000";
        String errorMess = "";

        try {
            controller.getItinerary(itineraryID,sessionID);
        } catch (FailedOperationException e) {
            errorMess = e.getMessage();
        }

        Assertions.assertEquals("Get itinerary - itinerary not found operation failed", errorMess);
    }

    @Test
    void testGetUserItinerariesCorrect() throws DaoException, IncorrectDataException, FailedOperationException {
        ManageItineraryController controller = new ManageItineraryController();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        String sessionID = SessionManager.getInstance().addSession(accountDao.getAccount("userTest"));
        List<ItineraryBean> itineraries = controller.getUserItineraries(sessionID);
        assertNotNull(itineraries);
        assertFalse(itineraries.isEmpty());
    }

    @Test
    void testGetUserItinerariesIncorrect() throws IncorrectDataException {
        ManageItineraryController controller = new ManageItineraryController();
        String sessionID = "00000";
        String errorMess = "";

        try {
            controller.getUserItineraries(sessionID);
        } catch (FailedOperationException e) {
            errorMess = "GetUserItineraries - Session not found operation failed";
        }
        Assertions.assertEquals("GetUserItineraries - Session not found operation failed", errorMess);
    }

    @Test
    void testGetAccountType() throws DaoException {
        ManageItineraryController controller = new ManageItineraryController();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        String sessionID = SessionManager.getInstance().addSession(accountDao.getAccount("userTest"));
        String type = controller.getAccountType(sessionID);
        Assertions.assertEquals(USER, type);
    }

    @BeforeAll
    static void provideData() throws IncorrectDataException, DaoException {
        Persistence.getInstance().setType(Persistence.IN_MEMORY);
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();
        CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
        AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();
        ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();
        ProposalDao proposalDao = Persistence.getInstance().getFactory().getProposalDao();

        CredentialsBean agencyCredentialsBean = new CredentialsBean();
        agencyCredentialsBean.setUsername("agencyTest");
        agencyCredentialsBean.setPassword("passwordA");
        agencyCredentialsBean.setType(AGENCY);
        agencyCredentialsBean.setDescription("agency description");
        agencyCredentialsBean.setPaymentCredentials("1234567890123456");
        agencyCredentialsBean.setPreferences(List.of(CULTURE));
        if (accountDao.getAccount("agencyTest") == null) {
            accountDao.addAccount(agencyCredentialsBean);
        }

        CredentialsBean userCredentialsBean = new CredentialsBean();
        userCredentialsBean.setUsername("userTest");
        userCredentialsBean.setPassword("passwordU");
        userCredentialsBean.setType(USER);
        userCredentialsBean.setPaymentCredentials("1234567890123456");
        if (accountDao.getAccount("userTest") == null) {
            accountDao.addAccount(userCredentialsBean);
        }

        City city = cityDao.createCity("r41485");
        if (cityDao.getCity("r41485") == null) {
            cityDao.addCity(city);
        }

        Attraction attraction = attractionDao.createAttraction("r1849830");
        if (attractionDao.getAttraction("r1849830") == null) {
            attractionDao.addAttraction(attraction);
        }


        Day day = new Day();
        day.setDayNum(1);
        day.setId("id1234");
        day.setCities(List.of(city));
        day.setAttractions(List.of(attraction));

        Itinerary itinerary = new BaseItinerary();
        itinerary.setItineraryID("id1234");
        itinerary.setName("itinerary name");
        itinerary.setDescription("itinerary description");
        itinerary.setDaysNumber(1);
        itinerary.setDays(List.of(day));
        itinerary.setTypes(List.of(CULTURE));
        itinerary.setPhotoFile(null);
        if (itineraryDao.getItinerary("id1234") == null) {
            itineraryDao.addItinerary(itinerary,accountDao.getAccount("userTest"));
        }

        Proposal proposal = new Proposal();
        proposal.setId("id5678");
        proposal.setItinerary(itinerary);
        proposal.setDescription("proposal description");
        proposal.setPrice(113.5);
        proposal.setAccepted(PENDING);
        if (proposalDao.getProposal("id5678") == null) {
            proposalDao.addProposal(proposal, (User) accountDao.getAccount("userTest"), (Agency) accountDao.getAccount("agencyTest"));
        }
    }
}