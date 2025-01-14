package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryDayDao;
import it.uniroma2.ispw.globe.other.Persistence;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ItineraryDao {
    public Itinerary createItinerary(ItineraryBean itineraryBean) {
        Itinerary itinerary = new Itinerary();
        List<Day> days = new ArrayList<>();

        DayDao dayDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getDayDao();

        Day day0 = dayDao.createDay(itineraryBean.getId(),0,itineraryBean.getCities(),itineraryBean.getAttractions());
        days.add(day0);
        for (int i=1; i<=itineraryBean.getDuration(); i++) {
            String id = UUID.randomUUID().toString();

            Day day = dayDao.createDay(id, i,new ArrayList<>(),new ArrayList<>());
            days.add(day);
        }

        itinerary.setItineraryID(itineraryBean.getId());
        itinerary.setName(itineraryBean.getName());
        itinerary.setDescription(itineraryBean.getDescription());
        itinerary.setDaysNumber(itineraryBean.getDuration());
        itinerary.setDays(days);
        //itinerary.setType(itineraryBean.getType());

        return itinerary;
    }
    public abstract void addItinerary(Itinerary itinerary, User user);
    public abstract Itinerary getItinerary(String itineraryName);
    public abstract void removeItinerary(String itineraryID);
}
