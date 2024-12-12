package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.model.dao.DayDao;

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
        days.add(day);
        int id = 0;
        if (!days.isEmpty()) {
            id = days.getLast().getId()+1;
        }
        day.setId(id);
        for (City city : day.getCities()) {
            InMemoryCityDao.getInstance().addCity(city);
        }
        for (Attraction attraction : day.getAttractions()) {
            InMemoryAttractionDao.getInstance().addAttraction(attraction);
        }
    }

    @Override
    public Day getDay(int dayID) {
        for (Day day : days) {
            if (day.getId() == dayID) {
                return day;
            }
        }
        return null;
    }
}
