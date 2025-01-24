package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;

public class InDbItineraryDao extends ItineraryDao {
    @Override
    public void addItinerary(Itinerary itinerary, Account account) {
    }

    @Override
    public Itinerary getItinerary(String name) {
        return null;
    }

    @Override
    public void removeItinerary(String itineraryID) {
        
    }
}
