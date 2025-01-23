package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.AgencyRequestBean;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import it.uniroma2.ispw.globe.other.Persistence;
import org.w3c.dom.Attr;

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
    public abstract void addAgencyRequest(Request request, User user, Agency agency);
    public abstract void addUserRequest(RequestBean requestBean, User user, Agency agency);
    public abstract Request getRequest(String requestId);
    public abstract void removeRequest(String requestId);
}
