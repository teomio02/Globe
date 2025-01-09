package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.dao.DayDao;

import java.util.List;

public class InDbDayDao extends DayDao {
    @Override
    public void addDay(String id, int dayNum, List<String> citiesID, List<String> attractionsID) {

    }

    @Override
    public Day getDay(String dayID) {
        return null;
    }
}
