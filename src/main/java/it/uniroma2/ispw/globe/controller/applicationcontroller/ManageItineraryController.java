package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.dao.AttractionDao;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import it.uniroma2.ispw.globe.util.ModelFactory;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public class ManageItineraryController {

    private static final String CITY = "administrative";
    private static final String ATTRACTION = "";

    public void saveItinerary(ItineraryBean itineraryBean, String sessionID) {
        Itinerary itinerary = new ModelFactory().createItinerary(itineraryBean);
        calculateItinerary(itinerary);
        User user = SessionManager.getInstance().getSession(sessionID).getUser();
        ItineraryDao itineraryDao = DaoFactory.getFactory(DaoFactory.IN_MEMORY).getItineraryDao();
        itineraryDao.addItinerary(itinerary, user);
    }

    public void removeItinerary(ItineraryBean itineraryBean, UserBean userBean) {}

    public void editItinerary(ItineraryBean itineraryBean, UserBean userBean) {}

    public void calculateItinerary(Itinerary itinerary) {
        List<Day> days = itinerary.getDays();
        List<City> cities = days.get(0).getCities();
        List<Attraction> attractions = days.get(0).getAttractions();
        List<Attraction> otherAttractions = new ArrayList<>();

        int curDay=1;

        Map<String,List<Attraction>> attractionsByCity = new HashMap<>();

        for (City city : cities) {
            attractionsByCity.put(city.getName(), new ArrayList<>());
        }
        for (Attraction attraction : attractions) {
            for (City city : cities) {
                if (attraction.getCity().equals(city.getName())) {
                    attractionsByCity.get(city.getName()).add(attraction);
                }
            }
            if (!attractionsByCity.containsKey(attraction.getCity())){
                otherAttractions.add(attraction);
            }
        }

        int attrNum = 0;

        for (List<Attraction> attractionList : attractionsByCity.values()) {
            attrNum += attractionList.size();
        }

        List<Day> newDays = new ArrayList<>();

        for ( Map.Entry<String, List<Attraction>> entry : attractionsByCity.entrySet()) {

            List<Attraction> attractionPath = getShortestPath(entry.getValue());

            int daysForCity = (int)Math.round(((double)entry.getValue().size()/(double)attrNum)*itinerary.getDaysNumber());
            if (daysForCity !=0) {
                int attrDayNum = (int)Math.ceil(attractionPath.size()/(double)daysForCity);
                int curAttr = 0;
                for (int i = 0; i<daysForCity ; i++) {
                    List<Attraction> attractionsForDay = new ArrayList<>();
                    int curAttrOnDay = 0;
                    while (curAttrOnDay < attrDayNum && curAttr<attractionPath.size()) {
                        attractionsForDay.add(attractionPath.get(curAttr));
                        curAttr++;
                        curAttrOnDay++;
                    }
                    Day day = itinerary.getDays().get(curDay);
                    for (City city : cities) {
                        if (city.getName().equals(entry.getKey())) {
                            day.getCities().add(city);
                        }
                    }
                    day.setDayNum(curDay);
                    day.setAttractions(attractionsForDay);
                    newDays.add(day);
                    curDay++;
                }
            }
        }
        itinerary.setDays(newDays);
    }

    public List<Attraction> getShortestPath(List<Attraction> attractions) {

        Map<Attraction,List<Pair<Attraction,Double>>> distances = new HashMap<>();

        for (Attraction attraction : attractions) {
            distances.put(attraction,new ArrayList<>());
            for (Attraction otherAttraction : attractions) {
                if (!attraction.equals(otherAttraction)) {
                    double latitudeDistance = attraction.getLatitude() - otherAttraction.getLatitude();
                    double longitudeDistance = attraction.getLongitude() - otherAttraction.getLongitude();
                    double distance = Math.sqrt(Math.pow(latitudeDistance, 2) + Math.pow(longitudeDistance, 2));
                    Pair<Attraction, Double> pair = new Pair<>(otherAttraction,distance);
                    List<Pair<Attraction,Double>> pairs = distances.get(attraction);
                    pairs.add(pair);
                    distances.put(attraction,pairs);
                }
            }
        }

        List<Attraction> path = new ArrayList<>();
        List<Attraction> visited = new ArrayList<>();

        Attraction current = attractions.get(0);

        while (visited.size()<attractions.size()) {
            path.add(current);
            visited.add(current);

            Attraction next = null;
            double minDistance = Double.MAX_VALUE;

            for (Pair<Attraction,Double> attrDistance : distances.get(current)) {
                if (!visited.contains(attrDistance.getKey()) && attrDistance.getValue()<minDistance) {
                    next = attrDistance.getKey();
                    minDistance = attrDistance.getValue();
                }
            }

            if (next == null) {
                break;
            }
            current = next;
        }
        return path;
    }

    public List<StepBean> getSteps(String itineraryName) {
        List<StepBean> steps = new ArrayList<>();

        ItineraryDao itineraryDao = DaoFactory.getFactory(DaoFactory.IN_MEMORY).getItineraryDao();
        Itinerary itinerary = itineraryDao.getItinerary(itineraryName);

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
            StepBean stepBean = new StepBean(cities,attractions);
            steps.add(stepBean);
        }
        return steps;
    }

    public List<JsonObject> getPlaces(String name, String type) {
        APIClient api = new APIClient();
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

    public ItineraryBean getItinerary(String itineraryName) {
        ItineraryDao itineraryDao = DaoFactory.getFactory(DaoFactory.IN_MEMORY).getItineraryDao();
        Itinerary itinerary = itineraryDao.getItinerary(itineraryName);

        return new ItineraryBean(itinerary.getName(), itinerary.getDescription(), "", itinerary.getDaysNumber(), 0,0,0,0, null);
    }

    public CityBean getCity(String cityID) {
        CityDao cityDao = DaoFactory.getFactory(DaoFactory.IN_MEMORY).getCityDao();
        City city = cityDao.getCity(cityID);

        return new CityBean(city.getPlaceID(), city.getName(), city.getCountry());
    }

    public AttractionBean getAttraction(String attractionID) {
        AttractionDao attractionDao = DaoFactory.getFactory(DaoFactory.IN_MEMORY).getAttractionDao();
        Attraction attraction = attractionDao.getAttraction(attractionID);

        return new AttractionBean(attraction.getPlaceID(), attraction.getName(), attraction.getAddress(), attraction.getCity(),0,0);
    }

    public AgencyBean getAgency(AgencyBean agencyBean) {
        return agencyBean;
    }

    public List<ItineraryBean> getUserItineraries(String sessionId) {

        User user = SessionManager.getInstance().getSession(sessionId).getUser();
        List<Itinerary> itineraries = user.getItineraries();
        List<ItineraryBean> itineraryBeans = new ArrayList<>();
        for (Itinerary itinerary : itineraries) {
            ItineraryBean itineraryBean = new ItineraryBean(itinerary.getName(),itinerary.getDescription(),"",itinerary.getDaysNumber());
            itineraryBeans.add(itineraryBean);
        }
        return itineraryBeans;
    }

}
