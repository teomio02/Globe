package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.AccommodationEntity;
import it.uniroma2.ispw.globe.model.AttractionEntity;
import it.uniroma2.ispw.globe.model.CityEntity;
import it.uniroma2.ispw.globe.model.bean.AttractionBean;
import it.uniroma2.ispw.globe.model.bean.CityBean;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.ItineraryEntity;
import it.uniroma2.ispw.globe.model.dao.ItineraryDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManageItineraryController {

    public ManageItineraryController() {}

    public void modifyItinerary() {}

    public void deleteItinerary() {}

    public void addItinerary(ItineraryBean itinerary_bean) {
        List<CityEntity> cities = new ArrayList<>();
        for (CityBean city : itinerary_bean.getCities()) {
            List<AttractionEntity> attractions = new ArrayList<>();
            AccommodationEntity accommodation = new AccommodationEntity(city.getAccommodation().getName());
            for (AttractionBean attraction : city.getAttractions()) {
                AttractionEntity attractionEntity = new AttractionEntity(attraction.getName());
                attractions.add(attractionEntity);
            }
            CityEntity cityEntity = new CityEntity(city.getName(), accommodation, attractions);
            cities.add(cityEntity);
        }
        ItineraryEntity itinerary = new ItineraryEntity(itinerary_bean.getName(), itinerary_bean.getDescription(), itinerary_bean.getNumberOfDays(), cities);
        System.out.println(itinerary_bean.toString());
    }

    public void acceptItinerary() {}

    public void rejectItinerary() {}

}
