package it.uniroma2.ispw.globe.util;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.controller.applicationcontroller.APIClient;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelFactory {
    public Itinerary createItinerary(ItineraryBean itineraryBean) {
        Itinerary itinerary = new Itinerary();
        List<Day> days = new ArrayList<>();

        Day day0 = createDay(0,itineraryBean.getCities(),itineraryBean.getAttractions());
        days.add(day0);
        for (int i=0; i<=itineraryBean.getDuration(); i++) {
            Day day = createDay(i,new ArrayList<>(),new ArrayList<>());
            days.add(day);
        }

        itinerary.setItineraryID(1);
        itinerary.setName(itineraryBean.getName());
        itinerary.setDescription(itineraryBean.getDescription());
        itinerary.setDaysNumber(itineraryBean.getDuration());
        itinerary.setDays(days);
        //itinerary.setType(itineraryBean.getType());
        return itinerary;
    }

    public Day createDay(int dayNum, List<String> citiesID, List<String> attractionsID) {
        Day day = new Day();
        List<City> cities = new ArrayList<>();
        List<Attraction> attractions = new ArrayList<>();

        for (String i : citiesID) {
            City city = createCity(i);
            cities.add(city);
        }

        for (String i : attractionsID) {
            Attraction attraction = createAttraction(i);
            attractions.add(attraction);
        }

        day.setDayNum(dayNum);
        day.setCities(cities);
        day.setAttractions(attractions);

        return day;

    }

    public City createCity(String cityId) {
        JsonObject jsonCity;
        try {
            jsonCity= new APIClient().getPlaceByID(cityId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        City city = new PlaceAdapter(jsonCity);
        return city;
    }

    public Attraction createAttraction(String attractionId) {
        JsonObject jsonAttraction;
        try {
            jsonAttraction = new APIClient().getPlaceByID(attractionId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Attraction attraction = new PlaceAdapter(jsonAttraction);
        return attraction;
    }
}
