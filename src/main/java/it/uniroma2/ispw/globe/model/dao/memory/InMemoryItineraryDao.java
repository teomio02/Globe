package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;

import java.util.ArrayList;
import java.util.List;

public class InMemoryItineraryDao extends ItineraryDao {

    private static InMemoryItineraryDao instance = null;

    private List<Itinerary> itineraries = new ArrayList<>();

    private InMemoryItineraryDao() {}

    public static InMemoryItineraryDao getInstance() {
        if (instance == null) {
            instance = new InMemoryItineraryDao();
        }
        return instance;
    }

    @Override
    public void addItinerary(Itinerary itinerary, Account account) {
        for (Day day : itinerary.getDays()) {
            InMemoryDayDao.getInstance().addDay(day);
        }

        if (account != null) {
            for (Itinerary savedItinerary : itineraries) {
                if (savedItinerary.getItineraryID().equals(itinerary.getItineraryID())) {
                    // errore
                    return;
                }
            }
            itineraries.add(itinerary);
            account.getItineraries().add(itinerary);
        } else {
            // errore
        }
    }

    @Override
    public Itinerary getItinerary(String id) {
        for (Itinerary itinerary : itineraries) {
            System.out.println("       id: "+itinerary.getItineraryID());
            if (itinerary.getItineraryID().equals(id)) {
                System.out.println("       itinerary found");
                return itinerary;
            }
        }
        return null;
    }

    @Override
    public void removeItinerary(String itineraryID) {
        // rimuovi day
    }
}
