package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;

public abstract class ItineraryDao {
    public abstract void addItinerary(ItineraryBean itinerary, User user);
    public abstract Itinerary getItinerary(String itineraryName);
    public abstract void removeItinerary(String itineraryID);
}
