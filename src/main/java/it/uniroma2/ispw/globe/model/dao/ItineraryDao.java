package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.BaseItinerary;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ItineraryDao {
    public BaseItinerary createItinerary(String id, String name, String description, List<String> citiesID, List<String> attractionsID, int duration) {
        BaseItinerary itinerary = new BaseItinerary();
        List<Day> days = new ArrayList<>();

        DayDao dayDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getDayDao();

        Day day0 = dayDao.createDay(id,0,citiesID,attractionsID);
        days.add(day0);
        for (int i=1; i<=duration; i++) {
            Day day = dayDao.createDay(id, i,new ArrayList<>(),new ArrayList<>());
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
