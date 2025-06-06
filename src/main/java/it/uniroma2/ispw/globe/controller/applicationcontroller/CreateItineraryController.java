package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.exception.*;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.Accommodation;
import it.uniroma2.ispw.globe.model.Flight;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.bean.AttractionBean;
import it.uniroma2.ispw.globe.bean.CityBean;
import it.uniroma2.ispw.globe.dao.ItineraryDao;
import it.uniroma2.ispw.globe.dao.DayDao;
import it.uniroma2.ispw.globe.dao.CityDao;
import it.uniroma2.ispw.globe.dao.AttractionDao;
import it.uniroma2.ispw.globe.dao.AccommodationDao;
import it.uniroma2.ispw.globe.dao.FlightDao;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.session.Session;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.engineering.adapter.PlaceAdapter;
import it.uniroma2.ispw.globe.engineering.decorator.AccommodationDecorator;
import it.uniroma2.ispw.globe.engineering.decorator.FlightDecorator;
import it.uniroma2.ispw.globe.model.Itinerary;
import javafx.util.Pair;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_API;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_DAO;

public class CreateItineraryController {
    private static final String CITY = "administrative";
    private static final String ATTRACTION = "";

    public void createItinerary(ItineraryBean itineraryBean, String sessionID) throws FailedOperationException, DuplicateItemException, AttractionNotAddedException {
        try {
            ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();
            DayDao dayDao = Persistence.getInstance().getFactory().getDayDao();
            CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
            AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();

            String itineraryId = UUID.randomUUID().toString();
            itineraryBean.setId(itineraryId);

            Itinerary itinerary = itineraryDao.createItinerary(itineraryId,itineraryBean.getName(),itineraryBean.getDescription(), itineraryBean.getDuration(), itineraryBean.getTypes());

            List<Day> days = new ArrayList<>();

            Day day0 = dayDao.createDay(itineraryId,0);
            List<City> cities = new ArrayList<>();
            List<Attraction> attractions = new ArrayList<>();

            for (String cityId : itineraryBean.getCities()) {
                City city;
                city = cityDao.createCity(cityId);
                cities.add(city);
            }

            for (String attractionId : itineraryBean.getAttractions()) {
                Attraction attraction;
                attraction = attractionDao.createAttraction(attractionId);
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
                AccommodationDao accommodationDao = Persistence.getInstance().getFactory().getAccommodationDao();

                List<Accommodation> accommodations = new ArrayList<>();
                for (Pair<String,String> a : itineraryBean.getAccommodations()) {
                    Accommodation accommodation = accommodationDao.createAccommodation(a.getKey(), a.getValue());
                    accommodations.add(accommodation);
                }
                AccommodationDecorator accommodationItinerary = new AccommodationDecorator(itinerary);
                accommodationItinerary.setAccommodations(accommodations);
                itinerary = accommodationItinerary;
            }

            if (itineraryBean.getInboundFlightArrivalTime() != 0) {
                FlightDao flightDao = Persistence.getInstance().getFactory().getFlightDao();

                Flight inFlight = flightDao.createFlight(itineraryBean.getInboundFlightDepartureTime(), itineraryBean.getInboundFlightArrivalTime());
                Flight outFlight = flightDao.createFlight(itineraryBean.getOutboundFlightDepartureTime(), itineraryBean.getOutboundFlightArrivalTime());

                FlightDecorator flightItinerary = new FlightDecorator(itinerary);
                flightItinerary.setInFlight(inFlight);
                flightItinerary.setOutFlight(outFlight);
                itinerary = flightItinerary;
            }

            SessionManager.getInstance().getSession(sessionID).setPendingItinerary(itinerary);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Create itinerary");
        }
    }

    public void saveItinerary(String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();

            Session session = SessionManager.getInstance().getSession(sessionID);
            Account account = session.getAccount();

            Itinerary itinerary = session.getPendingItinerary();
            itinerary.getDays().removeIf(day -> day.getDayNum() == 0);
            itineraryDao.addItinerary(itinerary, account);

            session.setPendingItinerary(null);
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Save itinerary");
        }
    }

    public void calculateItinerary(Itinerary itinerary) throws AttractionNotAddedException {
        Map<String,List<Attraction>> attractionsByCity = getAttractionsByCity(itinerary);
        int numAttraction = 0;
        for (List<Attraction> list : attractionsByCity.values()) {
            numAttraction += list.size();
        }
        itinerary.setDays(distributeAttraction(itinerary,attractionsByCity,numAttraction));
    }

    public Map<String, List<Attraction>> getAttractionsByCity(Itinerary itinerary) throws AttractionNotAddedException {
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
        if (!otherAttractions.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Attraction attraction : otherAttractions) {
                stringBuilder.append("- ").append(attraction.getName()).append(", ").append(attraction.getCity()).append("\n");
            }
            throw new AttractionNotAddedException(stringBuilder.toString());
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

    public List<JsonObject> getPlaces(String name, String type) throws FailedOperationException {
        NominatimAPIClient api = new NominatimAPIClient();
        List<JsonObject> apiPlaces;
        try {
            apiPlaces = api.getPlaces(name,type);
        } catch (PlaceApiException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_API, e);
            throw new FailedOperationException("Api error in get places");
        }
        return apiPlaces;
    }

    public List<AttractionBean> getAttractions(String name) throws FailedOperationException {
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

    public List<CityBean> getCities(String name) throws FailedOperationException {
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

    public CityBean getCity(int stepNum,String cityID,String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
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
                CityBean cityBean = new CityBean();
                cityBean.setId(city.getPlaceID());
                cityBean.setName(city.getName());
                cityBean.setCountry(city.getCountry());
                return cityBean;
            } else {
                return null;
            }
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Get city");
        }
    }

    public AttractionBean getAttraction(int stepNum,String attractionID,String sessionID) throws FailedOperationException, DuplicateItemException {
        try {
            AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();
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
                AttractionBean attractionBean = new AttractionBean();
                attractionBean.setId(attraction.getPlaceID());
                attractionBean.setName(attraction.getName());
                attractionBean.setAddress(attraction.getAddress());
                attractionBean.setCity(attraction.getCity());

                return attractionBean;
            } else {
                return null;
            }
        } catch (DaoException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
            if (e.getType() == DUPLICATE) {
                throw new DuplicateItemException();
            }
            throw new FailedOperationException("Get attraction");
        }
    }

    public void setItineraryPhoto (File file, String itineraryID, String sessionID) throws DuplicateItemException, FailedOperationException {
        if (itineraryID == null) {
            Itinerary itinerary = SessionManager.getInstance().getSession(sessionID).getPendingItinerary();
            itinerary.setPhotoFile(file);
        } else {
            ItineraryDao itineraryDao = Persistence.getInstance().getFactory().getItineraryDao();
            try {
                itineraryDao.addPhotoFile(file, itineraryID);
            } catch (DaoException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_DAO, e);
                if (e.getType() == DUPLICATE) {
                    throw new DuplicateItemException();
                }
                throw new FailedOperationException("Set Photo");
            }
        }

    }
}
