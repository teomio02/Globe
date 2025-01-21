package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.other.Persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ItineraryDao {
    public Itinerary createItinerary(String id, String name, String description, List<String> citiesID, List<String> attractionsID, int duration) {
        Itinerary itinerary = new Itinerary();
        List<Day> days = new ArrayList<>();

        DayDao dayDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getDayDao();

        Day day0 = dayDao.createDay(id,0,citiesID,attractionsID);
        days.add(day0);
        for (int i=1; i<=duration; i++) {
            String newId = UUID.randomUUID().toString();

            Day day = dayDao.createDay(newId, i,new ArrayList<>(),new ArrayList<>());
            days.add(day);
        }

        itinerary.setItineraryID(id);
        itinerary.setName(name);
        itinerary.setDescription(description);
        itinerary.setDaysNumber(duration);
        itinerary.setDays(days);
        //itinerary.setType(itineraryBean.getType());

        return itinerary;
    }
    public abstract void addItinerary(Itinerary itinerary, Account account);
    public abstract Itinerary getItinerary(String itineraryName);
    public abstract void removeItinerary(String itineraryID);
}
