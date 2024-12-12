package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.dao.AttractionDao;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.util.ModelFactory;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public class ManageItineraryController {

    public void saveItinerary(ItineraryBean itineraryBean, UserBean userBean) {
        Itinerary itinerary = new ModelFactory().createItinerary(itineraryBean);
        calculateItinerary(itinerary);
        ItineraryDao itineraryDao = DaoFactory.getFactory(DaoFactory.IN_MEMORY).getItineraryDao();
        itineraryDao.addItinerary(itinerary);
    }

    public void removeItinerary(ItineraryBean itineraryBean, UserBean userBean) {}

    public void editItinerary(ItineraryBean itineraryBean, UserBean userBean) {}

    public void calculateItinerary(Itinerary itinerary) {
        List<Day> days = itinerary.getDays();
        List<City> cities = days.get(0).getCities();
        List<Attraction> attractions = days.get(0).getAttractions();
        List<Attraction> otherAttractions = new ArrayList<>();

        int curDay=1;

        Map<City,List<Attraction>> attractionsByCity = new HashMap<>();

        for (City city : cities) {
            attractionsByCity.put(city, new ArrayList<>());
        }
        for (Attraction attraction : attractions) {
            for (City city : cities) {
                if (attraction.getCity().equals(city.getName())) {
                    attractionsByCity.get(city).add(attraction);
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

        for ( City city : attractionsByCity.keySet()) {

            List<Attraction> attractionPath = getShortestPath(attractionsByCity.get(city));

            int daysForCity = (int)Math.round(((double)attractionsByCity.get(city).size()/(double)attrNum)*itinerary.getDaysNumber());
            if (daysForCity !=0) {
                int attrDayNum = (int)Math.ceil(attractionPath.size()/(double)daysForCity);;
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
                    day.setDayNum(curDay);
                    day.setAttractions(attractionsForDay);
                    day.setCities(List.of(city)); // da cambaire!!!!!
                    newDays.add(day);
                    curDay++;
                }
            }
        }
        itinerary.setDays(newDays);
    }

    public List<Attraction> getShortestPath(List<Attraction> attractions) {
        System.out.println("Calcolo del percorso più corto per "+attractions.get(0).getCity());
        System.out.printf("path iniziale: ");
        for (Attraction attraction : attractions) {
            System.out.print(attraction.getName()+", ");
        }
        System.out.println();

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

        Attraction start = attractions.get(0);
        Attraction current = start;

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
        System.out.printf("path finale: ");
        for (Attraction attraction : path) {
            System.out.print(attraction.getName()+", ");
        }
        System.out.println();
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
        List<JsonObject> json_attractions = getPlaces(name, "");
        List<Attraction> attractions = new ArrayList<>();
        List<AttractionBean> attractionBeans = new ArrayList<>();

        for (JsonObject json_attraction : json_attractions) {
//            System.out.println("MANAGEITINERARYCONTROLLER -> "+json_attraction.get("name").getAsString());
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
        List<JsonObject> json_cities = getPlaces(name, "administrative");
        List<City> cities = new ArrayList<>();
        List<CityBean> citiesBeans = new ArrayList<>();

        for (JsonObject json_city : json_cities) {
//            System.out.println("MANAGEITINERARYCONTROLLER -> "+json_city.get("name").getAsString());
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

        ItineraryBean itineraryBean = new ItineraryBean(itinerary.getName(), itinerary.getDescription(), "", itinerary.getDaysNumber(), 0,0,0,0, null);
        return itineraryBean;
    }

    public CityBean getCity(String cityID) {
        CityDao cityDao = DaoFactory.getFactory(DaoFactory.IN_MEMORY).getCityDao();
        City city = cityDao.getCity(cityID);
        CityBean cityBean = new CityBean(city.getPlaceID(), city.getName(), city.getCountry());
        return cityBean;
    }

    public AttractionBean getAttraction(String attractionID) {
        AttractionDao attractionDao = DaoFactory.getFactory(DaoFactory.IN_MEMORY).getAttractionDao();
        Attraction attraction = attractionDao.getAttraction(attractionID);
        AttractionBean attractionBean = new AttractionBean(attraction.getPlaceID(), attraction.getName(), attraction.getAddress(), attraction.getCity(),0,0);
        return attractionBean;
    }

    public AgencyBean getAgency(AgencyBean agencyBean) {
        return agencyBean;
    }

    public List<ItineraryBean> getUserItineraries(UserBean userBean) {
        return null;
    }

}
