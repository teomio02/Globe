package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.dao.AttractionDao;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.DayDao;
import it.uniroma2.ispw.globe.other.Persistence;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDayDao extends DayDao {

    private static InMemoryDayDao instance = null;

    private List<Day> days = new ArrayList<>();

    private InMemoryDayDao() {}

    public static InMemoryDayDao getInstance() {
        if (instance == null) {
            instance = new InMemoryDayDao();
        }
        return instance;
    }

    @Override
    public void addDay(Day day) {
        CityDao cityDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getCityDao();
        AttractionDao attractionDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAttractionDao();
        for (Day savedDay : days) {
            if (savedDay.getId().equals(day.getId())) {
                // errore
                return;
            }
        }
        for (City city : day.getCities()) {
            cityDao.addCity(city);
        }
        for (Attraction attraction : day.getAttractions()) {
            attractionDao.addAttraction(attraction);
        }
        days.add(day);
    }

    @Override
    public Day getDay(String itineraryID, int dayNum) {
        return null;
    }
}
