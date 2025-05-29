package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.dao.AccountDao;
import it.uniroma2.ispw.globe.dao.AttractionDao;
import it.uniroma2.ispw.globe.dao.CityDao;
import it.uniroma2.ispw.globe.dao.RequestDao;
import it.uniroma2.ispw.globe.exception.*;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.session.Session;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.engineering.adapter.PlaceAdapter;
import it.uniroma2.ispw.globe.engineering.decorator.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;
import static it.uniroma2.ispw.globe.constants.ProposalState.PENDING;

public class RequestItineraryController {
    private static final String CITY = "administrative";
    private static final String ATTRACTION = "";


    // da risistemare in teoria dovrebbe fare dao
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

    public RequestBean getRequest(String requestID, String sessionID) throws IncorrectDataException, FailedOperationException, DuplicateItemException {
        Request request;
        Agency agency = null;
        User user;

        if (requestID != null) {
            RequestDao requestDao = Persistence.getFactory(Persistence.getInstance().getType()).getRequestDao();
            AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();

            try {
                request = requestDao.getRequest(requestID);
                agency = accountDao.getAgencyByRequest(requestID);
                user = accountDao.getUserByRequest(requestID);
            } catch (DaoException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
                if (e.getType() == DUPLICATE) {
                    throw new DuplicateItemException();
                }
                throw new FailedOperationException("Get proposal");
            }
        } else {
            Session session = SessionManager.getInstance().getSession(sessionID);
            request = session.getPendingRequest();
            if (session.getAccount() instanceof Agency account) {
                agency = account;
                user = (User) session.getPendingAccount();
            } else {
                user = (User) session.getAccount();
            }
        }

        if (request == null) {
            throw new FailedOperationException("Get Request");
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
        for (Agency a: SessionManager.getInstance().getSession(sessionID).getPendingAgencies()) {
            agencies.add(a.getUsername());
        }

        RequestBean requestBean = new RequestBean();
        requestBean.setID(request.getId());
        requestBean.setUser(user.getUsername());
        if (requestID != null) {
            requestBean.setAgency(agency.getUsername());
        } else {
            requestBean.setAgencies(agencies);
        }
        requestBean.setOtherRequests(request.getOtherRequest());
        requestBean.setDayNum(request.getDayNum());
        requestBean.setTypes(request.getItineraryType());
        requestBean.setCities(citiesID);
        requestBean.setAttractions(attractionsID);
        requestBean.setAccepted(request.getAccepted());
        requestBean.setFlight(request.getFlightRequest());
        requestBean.setAccommodation(request.getAccommodationRequest());

        return requestBean;
    }

    public List<Object> getRequestOptional(String requestID, String sessionID) throws FailedOperationException, IncorrectDataException, DuplicateItemException {
        Request request;
        List<Object> optionals = new ArrayList<>();
        if (requestID != null) {
            RequestDao requestDao = Persistence.getFactory(Persistence.getInstance().getType()).getRequestDao();
            try {
                request = requestDao.getRequest(requestID);
            } catch (DaoException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
                if (e.getType() == DUPLICATE) {
                    throw new DuplicateItemException();
                }
                throw new FailedOperationException("Get proposal");
            }
        } else {
            request = SessionManager.getInstance().getSession(sessionID).getPendingRequest();
        }

        Request current = request;
        while (current instanceof RequestDecorator) {
            if (current instanceof OnTheRoadRequestDecorator) {
                OnTheRoadBean onTheRoadBean = new OnTheRoadBean();
                onTheRoadBean.setMode(((OnTheRoadRequestDecorator) current).getTravelMode());
                onTheRoadBean.setDayDrivingHours(((OnTheRoadRequestDecorator) current).getDayDrivingHours());
                optionals.add(onTheRoadBean);
            }
            if (current instanceof NatureRequestDecorator) {
                NatureBean natureBean = new NatureBean();
                natureBean.setDifficulty(((NatureRequestDecorator) current).getTrekkingDifficulty());
                natureBean.setTrekkingDistance(((NatureRequestDecorator) current).getTrekkingDistance());
                optionals.add(natureBean);
            }
            current = ((RequestDecorator) current).getRequest();
        }

        return optionals;
    }

    public List<AgencyBean> getAgencies(String sessionID) throws FailedOperationException, DuplicateItemException, IncorrectDataException {
        List<Agency> agencies = SessionManager.getInstance().getSession(sessionID).getPendingAgencies();
        List<AgencyBean> agencyBeans = new ArrayList<>();

        for (Agency agency: agencies) {
            AgencyBean agencyBean = new AgencyBean();
            agencyBean.setName(agency.getUsername());
            agencyBean.setRating(agency.getRating());
            agencyBean.setItineraryTypes(agency.getPreferences());
            agencyBeans.add(agencyBean);
        }

        return agencyBeans;
    }

    public void createRequest(RequestBean requestBean, OnTheRoadBean onTheRoadBean, NatureBean natureBean, String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
            RequestDao requestDao = Persistence.getFactory(Persistence.getInstance().getType()).getRequestDao();
            CityDao cityDao = Persistence.getFactory(Persistence.getInstance().getType()).getCityDao();
            AttractionDao attractionDao = Persistence.getFactory(Persistence.getInstance().getType()).getAttractionDao();


            Request request = requestDao.createRequest(UUID.randomUUID().toString(),PENDING,requestBean.getOtherRequests(),requestBean.getDayNum(),requestBean.isFlight(),requestBean.isAccommodation(),requestBean.getTypes());

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

            if (onTheRoadBean != null) {
                OnTheRoadRequestDecorator onTheRoadRequest = new OnTheRoadRequestDecorator(request);
                onTheRoadRequest.setDayDrivingHours(onTheRoadBean.getDayDrivingHours());
                onTheRoadRequest.setTravelMode(onTheRoadBean.getMode());
                request = onTheRoadRequest;
            }
            if (natureBean != null) {
                NatureRequestDecorator natureRequest = new NatureRequestDecorator(request);
                natureRequest.setTrekkingDistance(natureBean.getTrekkingDistance());
                natureRequest.setTrekkingDifficulty(natureBean.getDifficulty());
                request = natureRequest;
            }

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

    public void saveRequest(String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            RequestDao requestDao = Persistence.getFactory(Persistence.getInstance().getType()).getRequestDao();

            Session session = SessionManager.getInstance().getSession(sessionID);
            Account account = session.getAccount();

            Request request = session.getPendingRequest();
            List<Agency> agencies = session.getPendingAgencies();
            requestDao.addAgencyRequest(request, (User) account, agencies);

            session.setPendingRequest(null);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Save request");
        }
    }

    public CityBean getCity(String cityId) throws FailedOperationException {
        try {
            CityDao cityDao = Persistence.getFactory(Persistence.getInstance().getType()).getCityDao();
            City city = cityDao.getCity(cityId);

            CityBean cityBean = new CityBean();
            cityBean.setName(city.getName());
            cityBean.setId(city.getPlaceID());
            cityBean.setCountry(city.getCountry());


            return cityBean;

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Save request");
        }
    }

    public AttractionBean getAttraction(String attractionId) throws FailedOperationException {
        try {
            AttractionDao attractionDao = Persistence.getFactory(Persistence.getInstance().getType()).getAttractionDao();
            Attraction attraction = attractionDao.getAttraction(attractionId);

            AttractionBean attractionBean = new AttractionBean();
            attractionBean.setName(attraction.getName());
            attractionBean.setId(attraction.getPlaceID());
            attractionBean.setCity(attraction.getCity());
            attractionBean.setAddress(attraction.getAddress());

            return attractionBean;

        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            throw new FailedOperationException("Save request");
        }
    }
}
