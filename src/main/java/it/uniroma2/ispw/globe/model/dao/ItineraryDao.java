package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.User;

public abstract class ItineraryDao {
    public abstract void addItinerary(Itinerary itinerary, User user);
    public abstract Itinerary getItinerary(String itineraryName);
}
