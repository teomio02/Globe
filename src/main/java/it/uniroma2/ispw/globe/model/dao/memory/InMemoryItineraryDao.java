package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public void addItinerary(ItineraryBean itineraryBean, User user) {
        Itinerary itinerary = new Itinerary();
        List<Day> days = new ArrayList<>();

        String id = UUID.randomUUID().toString();
        InMemoryDayDao.getInstance().addDay(id,0,itineraryBean.getCities(),itineraryBean.getAttractions());
        Day day0 = InMemoryDayDao.getInstance().getDay(id);
        days.add(day0);
        for (int i=1; i<=itineraryBean.getDuration(); i++) {
            id = UUID.randomUUID().toString();
            InMemoryDayDao.getInstance().addDay(id, i,new ArrayList<>(),new ArrayList<>());
            Day day = InMemoryDayDao.getInstance().getDay(id);
            days.add(day);
        }

        itinerary.setItineraryID(itineraryBean.getId());
        itinerary.setName(itineraryBean.getName());
        itinerary.setDescription(itineraryBean.getDescription());
        itinerary.setDaysNumber(itineraryBean.getDuration());
        itinerary.setDays(days);
        //itinerary.setType(itineraryBean.getType());

        //se l'itinerario è già presente sostituiscilo, altrimenti inserisci, verifica se è lo stesso itinerario
        if (user != null) {
            itineraries.add(itinerary);
            user.getItineraries().add(itinerary);
        } else {
            // errore
        }
    }

    @Override
    public Itinerary getItinerary(String id) {
        for (Itinerary itinerary : itineraries) {
            if (itinerary.getItineraryID().equals(id)) {
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
