package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.bean.AgencyBean;
import it.uniroma2.ispw.globe.model.bean.AttractionBean;
import it.uniroma2.ispw.globe.model.bean.CityBean;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import it.uniroma2.ispw.globe.model.dao.AccountDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestItineraryController {
    private static final String CITY = "administrative";
    private static final String ATTRACTION = "";

    public List<JsonObject> getPlaces(String name, String type) {
        NominatimAPIClient api = new NominatimAPIClient();
        List<JsonObject> apiPlaces = null;
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
            agencies.add(agency);
        }
        return new ArrayList<>();
    }

    public void sendRequest(RequestBean requestBean) {
        System.out.println(requestBean.getAgencies()+" - "+requestBean.getItineraryType()+" - "+requestBean.getDayNum());
        for (String city: requestBean.getCities()){
            System.out.print(city);
        }
        System.out.println();
        for (String attraction: requestBean.getAttractions()){
            System.out.print(attraction);
        }


    }
}
