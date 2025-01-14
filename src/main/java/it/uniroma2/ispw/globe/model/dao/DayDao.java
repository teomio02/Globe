package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryAttractionDao;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryCityDao;
import it.uniroma2.ispw.globe.other.Persistence;

import java.util.ArrayList;
import java.util.List;

public abstract class DayDao {
    public Day createDay(String id, int dayNum, List<String> citiesID, List<String> attractionsID) {
        Day day = new Day();
        List<City> cities = new ArrayList<>();
        List<Attraction> attractions = new ArrayList<>();

        CityDao cityDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getCityDao();
        AttractionDao attractionDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAttractionDao();

        for (String cityId : citiesID) {
            City city = cityDao.createCity(cityId);
            cities.add(city);
        }

        for (String attractionId : attractionsID) {
            Attraction attraction = attractionDao.createAttraction(attractionId);
            attractions.add(attraction);
        }

        day.setId(id);
        day.setDayNum(dayNum);
        day.setCities(cities);
        day.setAttractions(attractions);
        return day;
    }
    public abstract void addDay(Day day);
    public abstract Day getDay(String dayID);
}
