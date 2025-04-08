package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Day;
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
        for (Day savedDay : days) {
            if (savedDay.getId().equals(day.getId())) {
                return;
            }
        }
        for (City city : day.getCities()) {
            InMemoryCityDao.getInstance().addCity(city);
        }
        for (Attraction attraction : day.getAttractions()) {
            InMemoryAttractionDao.getInstance().addAttraction(attraction);
        }
        days.add(day);
    }

    @Override
    public Day getDay(String itineraryID, int dayNum) throws ItemNotFoundException {
        for (Day day : days) {
            if (day.getId().equals(itineraryID)) {
                if (day.getDayNum() == dayNum) {
                    return day;
                }
            }
        }
        
        throw new ItemNotFoundException("day not found");
    }
}
