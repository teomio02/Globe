package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;

import java.util.ArrayList;
import java.util.List;

public class InMemoryItineraryDao extends ItineraryDao {

    private static InMemoryItineraryDao instance = null;

    List<Itinerary> itineraries = new ArrayList<>();

    private InMemoryItineraryDao() {}

    public static InMemoryItineraryDao getInstance() {
        if (instance == null) {
            instance = new InMemoryItineraryDao();
        }
        return instance;
    }

    @Override
    public void addItinerary(Itinerary itinerary) {
        //se l'itinerario è già presente sostituiscilo, altrimenti inserisci, verifica se è lo stesso itinerario
        itineraries.add(itinerary);
    }
}
