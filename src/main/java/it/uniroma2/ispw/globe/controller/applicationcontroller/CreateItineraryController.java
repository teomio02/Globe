package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.exception.PlaceApiException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.Session;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;
import it.uniroma2.ispw.globe.util.decorator.AccommodationDecorator;
import it.uniroma2.ispw.globe.util.decorator.FlightDecorator;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;
import it.uniroma2.ispw.globe.util.decorator.ItineraryDecorator;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_API;

public class CreateItineraryController {
    private static final String CITY = "administrative";
    private static final String ATTRACTION = "";

    public void createItinerary(ItineraryBean itineraryBean, String sessionID) {
        ItineraryDao itineraryDao = Persistence.getFactory(Persistence.getInstance().getType()).getItineraryDao();
        DayDao dayDao = Persistence.getFactory(Persistence.getInstance().getType()).getDayDao();
        CityDao cityDao = Persistence.getFactory(Persistence.getInstance().getType()).getCityDao();
        AttractionDao attractionDao = Persistence.getFactory(Persistence.getInstance().getType()).getAttractionDao();

        String itineraryId = UUID.randomUUID().toString();
        itineraryBean.setId(itineraryId);

        Itinerary itinerary = itineraryDao.createItinerary(itineraryId,itineraryBean.getName(),itineraryBean.getDescription(), itineraryBean.getDuration());

        List<Day> days = new ArrayList<>();

        Day day0 = dayDao.createDay(itineraryId,0);
        List<City> cities = new ArrayList<>();
        List<Attraction> attractions = new ArrayList<>();

        for (String cityId : itineraryBean.getCities()) {
            City city = cityDao.createCity(cityId);
            cities.add(city);
        }

        for (String attractionId : itineraryBean.getAttractions()) {
            Attraction attraction = attractionDao.createAttraction(attractionId);
            attractions.add(attraction);
        }
        day0.setCities(cities);
        day0.setAttractions(attractions);

        days.add(day0);
        for (int i=1; i<=itineraryBean.getDuration(); i++) {
            Day day = dayDao.createDay(itineraryId, i);
            days.add(day);
        }

        itinerary.setDays(days);

        calculateItinerary(itinerary);

        if (itineraryBean.getAccommodations() != null) {
            AccommodationDao accommodationDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccommodationDao();

            List<Accommodation> accommodations = new ArrayList<>();
            for (Pair<String,String> a : itineraryBean.getAccommodations()) {
                Accommodation accommodation = accommodationDao.createAccommodation(a.getKey(), a.getValue());
                accommodationDao.addAccommodation(accommodation);
                accommodations.add(accommodation);
            }
            AccommodationDecorator accommodationItinerary = new AccommodationDecorator(itinerary);
            accommodationItinerary.setAccommodations(accommodations);
            itinerary = accommodationItinerary;
        }

        if (itineraryBean.getInboundFlightArrivalTime() != 0) {
            FlightDao flightDao = Persistence.getFactory(Persistence.getInstance().getType()).getFlightDao();

            Flight inFlight = flightDao.createFlight(itineraryBean.getInboundFlightDepartureTime(), itineraryBean.getInboundFlightArrivalTime());
            Flight outFlight = flightDao.createFlight(itineraryBean.getOutboundFlightDepartureTime(), itineraryBean.getOutboundFlightArrivalTime());
            flightDao.addFlight(inFlight);
            flightDao.addFlight(outFlight);

            FlightDecorator flightItinerary = new FlightDecorator(itinerary);
            flightItinerary.setInFlight(inFlight);
            flightItinerary.setOutFlight(outFlight);
            itinerary = flightItinerary;
        }

        SessionManager.getInstance().getSession(sessionID).setPendingItinerary(itinerary);
    }

    public void saveItinerary(String sessionID) {
        ItineraryDao itineraryDao = Persistence.getFactory(Persistence.getInstance().getType()).getItineraryDao();

        Session session = SessionManager.getInstance().getSession(sessionID);
        Account account = session.getAccount();

        Itinerary itinerary = session.getPendingItinerary();
        itineraryDao.addItinerary(itinerary, account);

        session.setPendingItinerary(null);
    }

    public void calculateItinerary(Itinerary itinerary) {
        Map<String,List<Attraction>> attractionsByCity = getAttractionsByCity(itinerary);
        int numAttraction = 0;
        for (List<Attraction> list : attractionsByCity.values()) {
            numAttraction += list.size();
        }
        itinerary.setDays(distributeAttraction(itinerary,attractionsByCity,numAttraction));
    }

    public Map<String, List<Attraction>> getAttractionsByCity(Itinerary itinerary) {
        List<City> cities = itinerary.getDays().get(0).getCities();
        List<Attraction> attractions = itinerary.getDays().get(0).getAttractions();
        List<Attraction> otherAttractions = new ArrayList<>();

        Map<String,List<Attraction>> attractionsByCity = new HashMap<>();

        for (City city : cities) {
            attractionsByCity.put(city.getName(), new ArrayList<>());
        }
        for (Attraction attraction : attractions) {
            if (attractionsByCity.containsKey(attraction.getCity())) {
                attractionsByCity.get(attraction.getCity()).add(attraction);
            } else {
                otherAttractions.add(attraction);
            }
        }
        return attractionsByCity;
    }

    public List<Day> distributeAttraction(Itinerary itinerary, Map<String, List<Attraction>> attractionsByCity, int attrNum) {
        List<Day> newDays = new ArrayList<>();
        int curDay = 1;

        for (Map.Entry<String, List<Attraction>> entry : attractionsByCity.entrySet()) {

            List<Attraction> attractionPath = getShortestPath(entry.getValue());

            int daysForCity = Math.max(1, (int) Math.round((double) entry.getValue().size() / attrNum * itinerary.getDaysNumber()));

            int attrDayNum = (int)Math.ceil(attractionPath.size()/(double)daysForCity);

            int curAttr = 0;

            for (int i = 0; i<daysForCity ; i++) {
                Day day = itinerary.getDays().get(curDay);
                for (City city : itinerary.getDays().get(0).getCities()) {
                    if (city.getName().equals(entry.getKey())) {
                        day.getCities().add(city);
                    }
                }

                day.setAttractions(new ArrayList<>(attractionPath.subList(curAttr, Math.min(curAttr + attrDayNum, attractionPath.size()))));
                curAttr += attrDayNum;
                newDays.add(day);
                curDay++;
            }
        }
        return newDays;
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

    public List<StepBean> getSteps(String itineraryId, String sessionID) throws ItemNotFoundException {
        List<StepBean> steps = new ArrayList<>();
        Itinerary itinerary;

        if (itineraryId == null) {
            itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
        } else {
            ItineraryDao itineraryDao = Persistence.getFactory(Persistence.getInstance().getType()).getItineraryDao();
            itinerary = itineraryDao.getItinerary(itineraryId);
        }

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
            StepBean stepBean = new StepBean(day.getDayNum()-1,cities,attractions);
            steps.add(stepBean);
        }
        return steps;
    }

    public List<JsonObject> getPlaces(String name, String type) throws PlaceApiException {
        NominatimAPIClient api = new NominatimAPIClient();
        List<JsonObject> apiPlaces;
        try {
            apiPlaces = api.getPlaces(name,type);
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_API, e);
            throw new PlaceApiException("Error with external Api");
        }
        return apiPlaces;
    }

    public List<AttractionBean> getAttractions(String name) throws PlaceApiException {
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

    public List<CityBean> getCities(String name) throws PlaceApiException {
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

    public ItineraryBean getItinerary(String itineraryId, String sessionID) throws ItemNotFoundException {
        Itinerary itinerary;

        if (itineraryId == null) {
            itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
            if (itinerary == null) {
                return null;
            }
        } else {
            ItineraryDao itineraryDao = Persistence.getFactory(Persistence.getInstance().getType()).getItineraryDao();
            itinerary = itineraryDao.getItinerary(itineraryId);
        }

        List<String> types = new ArrayList<>();

        List<Pair<String,String>> accommodations = new ArrayList<>();
        double inDepartureTime = -1;
        double inArrivalTime = -1;
        double outDepartureTime = -1;
        double outArrivalTime = -1;

        Itinerary current = itinerary;
        while (current instanceof ItineraryDecorator itineraryDecorator) {
            if (current instanceof AccommodationDecorator accommodationDecorator) {
                for (Accommodation accommodation : accommodationDecorator.getAccommodations()) {
                    accommodations.add(new Pair<>(accommodation.getName(), accommodation.getAddress()));
                }
            }
            if (current instanceof FlightDecorator flightDecorator) {
                inDepartureTime = flightDecorator.getInFlight().getDepartureTime();
                inArrivalTime = flightDecorator.getInFlight().getArrivalTime();
                outDepartureTime = flightDecorator.getOutFlight().getDepartureTime();
                outArrivalTime = flightDecorator.getOutFlight().getArrivalTime();
            }
            current = itineraryDecorator.getItinerary();
        }

        ItineraryBean itineraryBean = new ItineraryBean(itinerary.getItineraryID(),itinerary.getName(),itinerary.getDescription(), types, itinerary.getDaysNumber());
        itineraryBean.setInboundFlightDepartureTime(inDepartureTime);
        itineraryBean.setInboundFlightArrivalTime(inArrivalTime);
        itineraryBean.setOutboundFlightDepartureTime(outDepartureTime);
        itineraryBean.setOutboundFlightArrivalTime(outArrivalTime);
        itineraryBean.setAccommodations(accommodations);

        return itineraryBean;
    }

    public CityBean getCity(int stepNum,String cityID,String sessionID) throws ItemNotFoundException {
        CityDao cityDao = Persistence.getFactory(Persistence.getInstance().getType()).getCityDao();
        City city = null;

        if (sessionID != null) {
            Itinerary itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
            for (City savedCity : itinerary.getDays().get(stepNum).getCities()) {
                if (cityID.equals(savedCity.getPlaceID())) {
                    city = savedCity;
                }
            }
        } else {
            city = cityDao.getCity(cityID);
            if (city == null) {
                city = cityDao.createCity(cityID);
            }
        }

        if (city != null) {
            return new CityBean(city.getPlaceID(), city.getName(), city.getCountry());
        } else {
            return null;
        }
    }

    public AttractionBean getAttraction(int stepNum,String attractionID,String sessionID) throws ItemNotFoundException {
        AttractionDao attractionDao = Persistence.getFactory(Persistence.getInstance().getType()).getAttractionDao();
        Attraction attraction = null;

        if (sessionID != null) {
            Itinerary itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
            for (Attraction savedeAttraction : itinerary.getDays().get(stepNum).getAttractions()) {
                if (attractionID.equals(savedeAttraction.getPlaceID())) {
                    attraction = savedeAttraction;
                }
            }
        } else {
            attraction = attractionDao.getAttraction(attractionID);
            if (attraction == null) {
                attraction = attractionDao.createAttraction(attractionID);
            }
        }

        if (attraction != null) {
            return new AttractionBean(attraction.getPlaceID(), attraction.getName(), attraction.getAddress(), attraction.getCity(),0,0);
        } else {
            return null;
        }
    }
}
