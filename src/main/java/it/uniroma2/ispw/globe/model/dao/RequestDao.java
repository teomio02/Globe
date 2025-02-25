package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import it.uniroma2.ispw.globe.other.Persistence;
<<<<<<< HEAD
=======
import it.uniroma2.ispw.globe.util.decorator.Request;

import java.util.ArrayList;
import java.util.List;
import it.uniroma2.ispw.globe.other.Persistence;
import org.w3c.dom.Attr;
>>>>>>> refs/remotes/origin/main

import java.util.ArrayList;
import java.util.List;

public abstract class RequestDao {
        public Request createAgencyRequest(String requestID,String userUsername,String agencyUsername,String isAccepted,String description,int days,List<String> citiesID,List<String> attractionsID,List<String> types) {
        //da cambiare
        Request request = new Request();

        AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();
        CityDao cityDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getCityDao();
        AttractionDao attractionDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAttractionDao();

        User user = (User) accountDao.getAccount(userUsername);
        Agency agency = (Agency) accountDao.getAccount(agencyUsername);


        BaseRequest request = new BaseRequest();
        request.setId(requestID);
        request.setUser(user);
        request.setAgency(agency);
        request.setAccepted(isAccepted);
        request.setDescription(description);
        request.setDays(days);
        request.setAttractions(new ArrayList<>());
        request.setCities(new ArrayList<>());
        request.setTypes(types);

        for (String cityID : citiesID) {
            City city = cityDao.getCity(cityID);
            System.out.println(city.getPlaceID()+" - "+city.getName());
            request.getCities().add(city);
        }
        for (String attractionID : attractionsID) {
            Attraction attraction = attractionDao.getAttraction(attractionID);
            System.out.println(attraction.getPlaceID()+" - "+attraction.getName());
            request.getAttractions().add(attraction);
        }

        return request;
    }

    public Request createUserRequest(String id, User user, Agency agency, boolean accepted, String otherRequest, int dayNum, List<String> citiesID, List<String> attractionsID, boolean flight, boolean accommodation, List<String> itineraryType) {

        BaseRequest request = new BaseRequest();

        List<City> cities = new ArrayList<>();
        List<Attraction> attractions = new ArrayList<>();

        CityDao cityDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getCityDao();
        AttractionDao attractionDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAttractionDao();

        for (String cityId : citiesID) {
            City city = cityDao.createCity(cityId);
            cities.add(city);
        }

        for (String attractionId : attractionsID) {
            Attraction attraction = attractionDao.createAttraction(attractionId);
            attractions.add(attraction);
        }

        request.setId(id);
        request.setUser(user);
        request.setAgency(agency);
        request.setAccepted(accepted);
        request.setOtherRequest(otherRequest);
        request.setDayNum(dayNum);
        request.setFlightRequest(flight);
        request.setAccommodationRequest(accommodation);
        request.setItineraryType(itineraryType);
        request.setCities(cities);
        request.setAttractions(attractions);

        return request;
    }

    public abstract void addAgencyRequest(AgencyRequestBean requestBean, User user, Agency agency);
    public abstract void addUserRequest(RequestBean requestBean, User user, Agency agency);
    public abstract Request getRequest(String requestId);
    public abstract void updateRequest(Request request);
}
