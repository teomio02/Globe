package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.BaseItinerary;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;

import java.util.List;

public abstract class ItineraryDao {
    public BaseItinerary createItinerary(String id, String name, String description, int duration) {
        BaseItinerary itinerary = new BaseItinerary();

        itinerary.setItineraryID(id);
        itinerary.setName(name);
        itinerary.setDescription(description);
        itinerary.setDaysNumber(duration);
        //itinerary.setType(itineraryBean.getType());

        return itinerary;
    }
    public abstract void addItinerary(Itinerary itinerary, Account account);
    public abstract Itinerary getItinerary(String itineraryName);
    public abstract void removeItinerary(String itineraryID);
}
