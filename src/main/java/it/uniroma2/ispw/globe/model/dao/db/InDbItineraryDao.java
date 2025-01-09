package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;

public class InDbItineraryDao extends ItineraryDao {
    @Override
    public void addItinerary(ItineraryBean itinerary, User user) {
    }

    @Override
    public Itinerary getItinerary(String name) {
        return null;
    }

    @Override
    public void removeItinerary(String itineraryID) {
        
    }
}
