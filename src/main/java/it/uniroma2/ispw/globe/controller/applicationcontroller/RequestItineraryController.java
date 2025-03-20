package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.dao.AccountDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.RequestDao;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;
import it.uniroma2.ispw.globe.util.decorator.*;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static it.uniroma2.ispw.globe.other.ProposalState.PENDING;

public class RequestItineraryController {
    private static final String CITY = "administrative";
    private static final String ATTRACTION = "";

    public List<JsonObject> getPlaces(String name, String type) {
        NominatimAPIClient api = new NominatimAPIClient();
        List<JsonObject> apiPlaces;
        try {
            apiPlaces = api.getPlaces(name,type);
        } catch (IOException e) {
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
            AttractionBean attractionBean = new AttractionBean(attraction.getPlaceID(), attraction.getName(), attraction.getAddress(), attraction.getCity(),0,0);
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
            CityBean cityBean = new CityBean(city.getPlaceID(), city.getName(), city.getCountry());
            citiesBeans.add(cityBean);
        }
        return citiesBeans;
    }

    public List<AgencyBean> getAgenciesByType(List<String> types) {
        AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();
        List<Agency> agencies = accountDao.getAgenciesByType(types);
        List<AgencyBean> agencyBeans = new ArrayList<>();

        for (Agency agency: agencies){
            AgencyBean agencyBean = new AgencyBean(agency.getUsername(), agency.getRating(), agency.getPreferences());
            agencyBeans.add(agencyBean);
        }
        return agencyBeans;
    }

    public RequestBean getRequest(String requestID, String sessionID) {
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


        return new RequestBean(request.getId(),citiesID, attractionsID, request.getOtherRequest(), request.getDayNum(), agencies, request.getFlightRequest(), request.getAccommodationRequest(), request.getItineraryType(), trekkingDifficulty, trekkingDistance, travelMode, drivingHours);
    }

    public AgencyBean getAgency(String username, String sessionID) {
        AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();
        Account account = accountDao.getAccount(username);


        return null;
    }



    public void createRequest(RequestBean requestBean, OnTheRoadBean onTheRoadBean, NatureBean natureBean, String sessionID) {
        System.out.println(requestBean.getAgencies()+" - "+requestBean.getItineraryType()+" - "+requestBean.getDayNum()+" - "+requestBean.getOtherRequests()+" - "+requestBean.isAccommodation()+" - "+requestBean.isFlight());
        if (onTheRoadBean != null) {
            System.out.println(onTheRoadBean.getDayDrivingHours()+" - "+onTheRoadBean.getMode());
        }
        if (natureBean != null) {
            System.out.println(natureBean.getDifficulty()+" - "+natureBean.getTrekkingDistance());
        }
        for (String city: requestBean.getCities()){
            System.out.print(city);
        }
        System.out.println();
        for (String attraction: requestBean.getAttractions()){
            System.out.print(attraction);
        }

        AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();
        RequestDao requestDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getRequestDao();


        Request request = requestDao.createUserRequest(UUID.randomUUID().toString(),null,null,PENDING,requestBean.getOtherRequests(),requestBean.getDayNum(),requestBean.getCities(),requestBean.getAttractions(),requestBean.isFlight(),requestBean.isAccommodation(),requestBean.getItineraryType());
        SessionManager.getInstance().getSession(sessionID).setPendingRequest(request);

        List<Agency> agencies = new ArrayList<>();
        for (String agencyName : requestBean.getAgencies()){
            Agency agency = (Agency) accountDao.getAccount(agencyName);
            agencies.add(agency);
        }
        SessionManager.getInstance().getSession(sessionID).setPendingAgencies(agencies);



    }
}
