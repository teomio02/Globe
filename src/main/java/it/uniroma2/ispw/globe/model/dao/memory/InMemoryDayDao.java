package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.dao.DayDao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public void addDay(String dayId, int dayNum, List<String> citiesID, List<String> attractionsID) {
        Day day = new Day();
        List<City> cities = new ArrayList<>();
        List<Attraction> attractions = new ArrayList<>();

        for (String id : citiesID) {
            InMemoryCityDao.getInstance().addCity(id);
            City city = InMemoryCityDao.getInstance().getCity(id);
            cities.add(city);
        }

        for (String id : attractionsID) {
            InMemoryAttractionDao.getInstance().addAttraction(id);
            Attraction attraction = InMemoryAttractionDao.getInstance().getAttraction(id);
            attractions.add(attraction);
        }

        day.setId(dayId);
        day.setDayNum(dayNum);
        day.setCities(cities);
        day.setAttractions(attractions);
        days.add(day);
    }

    @Override
    public Day getDay(String dayID) {
        for (Day day : days) {
            if (day.getId().equals(dayID)) {
                return day;
            }
        }
        return null;
    }
}
