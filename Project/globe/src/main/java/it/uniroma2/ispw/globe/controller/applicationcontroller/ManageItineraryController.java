package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.Bean.*;
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
        List<City> cities = days.getFirst().getCities();
        List<Attraction> attractions = days.getFirst().getAttractions();
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
                // cosa fare con le attrazioni di nessuna città
            }
        }

        int attrNum = 0;

        for (List<Attraction> attractionList : attractionsByCity.values()) {
            attrNum += attractionList.size();
        }

        List<Day> newDays = new ArrayList<>();

        for ( City city : attractionsByCity.keySet()) {

            List<Attraction> attractionPath = getShortestPath(attractionsByCity.get(city));

            int daysForCity = (int)Math.round(((double)attractionsByCity.get(city).size()/(double)attrNum)*(double)itinerary.getDaysNumber());
            System.out.println(daysForCity+"="+attractionsByCity.get(city).size()+"/"+attrNum+" * "+itinerary.getDaysNumber());
            if (daysForCity !=0) {
                int attrDayNum = Math.ceilDiv(attractionPath.size(),daysForCity);
                System.out.println("attrazioni al giorno:"+attrDayNum);
                int curAttr = 0;
                for (int i = 0; i<daysForCity ; i++) {
                    System.out.println(city.getName()+", day "+curDay+"("+i+")");
                    List<Attraction> attractionsForDay = new ArrayList<>();
                    int curAttrOnDay = 0;
                    while (curAttrOnDay < attrDayNum && curAttr<attractionPath.size()) {
                        attractionsForDay.add(attractionPath.get(curAttr));
                        System.out.print(attractionPath.get(curAttr).getName()+" - ");
                        curAttr++;
                        curAttrOnDay++;
                    }
                    System.out.println();
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

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(itinerary.getName()).append("-").append(itinerary.getDaysNumber()).append("\n");
        for (Day day : itinerary.getDays()) {
            stringBuilder.append(day.getDayNum()).append("\n");
            for (Attraction attraction : day.getAttractions()) {
                stringBuilder.append(attraction.getName()).append(" - ");
            }
            stringBuilder.append("\n");
            for (City city : day.getCities()) {
                stringBuilder.append(city.getName()).append(" - ");
            }
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
    }

    public List<Attraction> getShortestPath(List<Attraction> attractions) {
        System.out.println("Calcolo del percorso più corto per "+attractions.getFirst().getCity());
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

    public AgencyBean getAgency(AgencyBean agencyBean) {
        return agencyBean;
    }

    public List<ItineraryBean> getUserItineraries(UserBean userBean) {
        return null;
    }

}
