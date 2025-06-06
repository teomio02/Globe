package it.uniroma2.ispw.globe.controller.applicationcontroller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.bean.AttractionBean;
import it.uniroma2.ispw.globe.bean.ItineraryBean;
import it.uniroma2.ispw.globe.dao.*;
import it.uniroma2.ispw.globe.engineering.Persistence;
import it.uniroma2.ispw.globe.engineering.adapter.PlaceAdapter;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import it.uniroma2.ispw.globe.exception.*;
import it.uniroma2.ispw.globe.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateItineraryControllerTest {
    private static final String CITY = "administrative";
    private static final String ATTRACTION = "";

    @Test
    void calculateItineraryTest() throws DaoException, FailedOperationException, AttractionNotAddedException, PlaceApiException, DuplicateItemException, IncorrectDataException {
        Persistence.getInstance().setType(Persistence.IN_DATABASE);
        CityDao cityDao = Persistence.getInstance().getFactory().getCityDao();
        AttractionDao attractionDao = Persistence.getInstance().getFactory().getAttractionDao();
        AccountDao accountDao = Persistence.getInstance().getFactory().getAccountDao();


        User user = (User) accountDao.getAccount("teo");
        String sessionID = SessionManager.getInstance().addSession(user);

        CreateItineraryController controller = new CreateItineraryController();

        int dayNum = 15;

        String[] cityNames = {
                "roma",
                "milano",
                "napoli",
                "firenze",
                "venezia",
                "torino",
                "bologna",
                "genova"
        };

        String[] attractionNames = {
                // Roma (6)
                "Colosseo", "Basilica di San Pietro", "Fontana di Trevi",
                "Pantheon", "Foro Romano", "Piazza del Plebiscito",

                // Milano (3)
                "Teatro alla Scala", "Castello Sforzesco", "Galleria Vittorio Emanuele II",

                // Napoli (5)
                "Castel dell'Ovo", "Maschio Angioino", "Piazza del Gesù Nuovo",
                "Museo Cappella Sansevero",

                // Firenze (7)
                "Duomo di Firenze", "Ponte Vecchio", "Palazzo Pitti",
                "Basilica di Santa Croce", "Palazzo Vecchio", "Piazza della Signoria", "Galleria degli Uffizi",

                // Venezia (4)
                "Piazza San Marco", "Basilica di San Marco", "Palazzo Ducale", "Ponte di Rialto",

                // Torino (2)
                "Mole Antonelliana", "Piazza Castello",

                // Bologna (8)
                "Piazza Maggiore", "Fontana del Nettuno",
                "Basilica di San Petronio", "Archiginnasio di Bologna", "Santuario di San Luca",
                "Teatro Comunale di Bologna", "Palazzo d'Accursio",

                // Genova (1)
                "Acquario di Genova"
        };

        List<String> cities = new ArrayList<>();
        for (int i = 0; i < cityNames.length; i++) {
            String id = controller.getCities(cityNames[i]).get(0).getId();
            City city = cityDao.createCity(id);
            cities.add(city.getPlaceID());
        }
        List<String> attractions = new ArrayList<>();
        for (int i = 0; i < attractionNames.length; i++) {
            List<AttractionBean> attractionBeans = controller.getAttractions(attractionNames[i]);
            String id = attractionBeans.get(0).getId();
            Attraction attraction = attractionDao.createAttraction(id);
            attractions.add(attraction.getPlaceID());
        }

        ItineraryBean itineraryBean = new ItineraryBean();

        itineraryBean.setId(null);
        itineraryBean.setName("itinerary");
        itineraryBean.setDescription("description");
        itineraryBean.setTypes(List.of("culture"));
        itineraryBean.setCities(cities);
        itineraryBean.setAttractions(attractions);
        itineraryBean.setDuration(dayNum);


        controller.createItinerary(itineraryBean,sessionID);
        controller.saveItinerary(sessionID);

    }
}