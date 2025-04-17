package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.exception.*;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;
import it.uniroma2.ispw.globe.util.decorator.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;
import static it.uniroma2.ispw.globe.other.ProposalState.PENDING;

public class RequestItineraryController {
    private static final String CITY = "administrative";
    private static final String ATTRACTION = "";

    public List<JsonObject> getPlaces(String name, String type) {
        NominatimAPIClient api = new NominatimAPIClient();
        List<JsonObject> apiPlaces;
        try {
            apiPlaces = api.getPlaces(name,type);
        } catch (PlaceApiException e) {
            throw new RuntimeException(e);
        }
        return apiPlaces;
    }

    public List<AttractionBean> getAttractions(String name) {
//        //chiama la DAO/API per ottenere i nomi delle attrazioni
        List<JsonObject> jsonAttractions = getPlaces(name, ATTRACTION);
        List<Attraction> attractions = new ArrayList<>();
        List<AttractionBean> attractionBeans = new ArrayList<>();

        for (JsonObject json_attraction : jsonAttractions) {
            Attraction attraction = new PlaceAdapter(json_attraction);
            attractions.add(attraction);
        }

        for (Attraction attraction : attractions) {
            AttractionBean attractionBean = new AttractionBean();
            attractionBean.setId(attraction.getPlaceID());
            attractionBean.setName(attraction.getName());
            attractionBean.setAddress(attraction.getAddress());
            attractionBean.setCity(attraction.getCity());

            attractionBeans.add(attractionBean);
        }

        return attractionBeans;
    }

    public List<CityBean> getCities(String name) {
        List<JsonObject> jsonCities = getPlaces(name, CITY);
        List<City> cities = new ArrayList<>();
        List<CityBean> citiesBeans = new ArrayList<>();

        for (JsonObject json_city : jsonCities) {
            City city = new PlaceAdapter(json_city);
            cities.add(city);
        }

        for (City city : cities) {
            CityBean cityBean = new CityBean();
            cityBean.setId(city.getPlaceID());
            cityBean.setName(city.getName());
            cityBean.setCountry(city.getCountry());

            citiesBeans.add(cityBean);
        }
        return citiesBeans;
    }

    public List<AgencyBean> getAgenciesByType(List<String> types) throws FailedOperationException, DuplicateItemException, IncorrectDataException {
        try {
            AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
            List<Agency> agencies = accountDao.getAgenciesByType(types);
            List<AgencyBean> agencyBeans = new ArrayList<>();

            for (Agency agency: agencies){
                AgencyBean agencyBean = new AgencyBean();
                agencyBean.setName(agency.getUsername());
                agencyBean.setRating(agency.getRating());
                agencyBean.setItineraryTypes(agency.getPreferences());
                agencyBeans.add(agencyBean);
            }
            return agencyBeans;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Get agency by type");
        }
    }

    public RequestBean getRequest(String requestID, String sessionID) throws IncorrectDataException {
        Request request = SessionManager.getInstance().getSession(sessionID).getPendingRequest();
        if (requestID == null) {
            return null;
        }

        String travelMode = null;
        String drivingHours = null;
        String trekkingDifficulty = null;
        String trekkingDistance = null;

        Request current = request;
        while (current instanceof RequestDecorator) {
            if (current instanceof OnTheRoadRequestDecorator) {
                travelMode = ((OnTheRoadRequestDecorator) current).getTravelMode();
                drivingHours = ((OnTheRoadRequestDecorator) current).getDayDrivingHours();
            }
            if (current instanceof NatureRequestDecorator) {
                trekkingDifficulty = ((NatureRequestDecorator) current).getTrekkingDifficulty();
                trekkingDistance = ((NatureRequestDecorator) current).getTrekkingDistance();
            }
            current = ((RequestDecorator) current).getRequest();
        }

        List<String> citiesID = new ArrayList<>();
        for (City city : request.getCities()) {
            citiesID.add(city.getPlaceID());
        }

        List<String> attractionsID = new ArrayList<>();
        for (Attraction attraction : request.getAttractions()) {
            attractionsID.add(attraction.getPlaceID());
        }

        List<String> agencies = new ArrayList<>();
        for (Agency agency: SessionManager.getInstance().getSession(sessionID).getPendingAgencies()) {
            agencies.add(agency.getUsername());
        }

        RequestBean requestBean = new RequestBean();
        requestBean.setId(request.getId());
        requestBean.setCities(citiesID);
        requestBean.setAttractions(attractionsID);
        requestBean.setOtherRequests(request.getOtherRequest());
        requestBean.setDayNum(request.getDayNum());
        requestBean.setAgencies(agencies);
        requestBean.setFlight(request.getFlightRequest());
        requestBean.setAccommodation(request.getAccommodationRequest());
        requestBean.setItineraryType(request.getItineraryType());
        requestBean.setTrekkingDifficulty(trekkingDifficulty);
        requestBean.setTrekkingDistance(trekkingDistance);
        requestBean.setTravelMode(travelMode);
        requestBean.setDrivingHours(drivingHours);

        return requestBean;
    }

    public AgencyBean getAgency(String username, String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
            Account account = accountDao.getAccount(username);

            return null;
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Get agency");
        }
    }



    public void createRequest(RequestBean requestBean, OnTheRoadBean onTheRoadBean, NatureBean natureBean, String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
            RequestDao requestDao = Persistence.getFactory(Persistence.getInstance().getType()).getRequestDao();
            CityDao cityDao = Persistence.getFactory(Persistence.getInstance().getType()).getCityDao();
            AttractionDao attractionDao = Persistence.getFactory(Persistence.getInstance().getType()).getAttractionDao();


            Request request = requestDao.createRequest(UUID.randomUUID().toString(),PENDING,requestBean.getOtherRequests(),requestBean.getDayNum(),requestBean.isFlight(),requestBean.isAccommodation(),requestBean.getItineraryType());

            List<City> cities = new ArrayList<>();
            List<Attraction> attractions = new ArrayList<>();

            for (String cityId : requestBean.getCities()) {
                City city = cityDao.createCity(cityId);
                cities.add(city);
            }

            for (String attractionId : requestBean.getAttractions()) {
                Attraction attraction = attractionDao.createAttraction(attractionId);
                attractions.add(attraction);
            }

            request.setAttractions(attractions);
            request.setCities(cities);

            SessionManager.getInstance().getSession(sessionID).setPendingRequest(request);

            List<Agency> agencies = new ArrayList<>();
            for (String agencyName : requestBean.getAgencies()){
                Agency agency = (Agency) accountDao.getAccount(agencyName);
                agencies.add(agency);
            }
            SessionManager.getInstance().getSession(sessionID).setPendingAgencies(agencies);

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Create request");
        }
    }
}
