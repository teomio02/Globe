package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Day;

import java.util.List;

public abstract class DayDao {
    public abstract void addDay(String id, int dayNum, List<String> citiesID, List<String> attractionsID);
    public abstract Day getDay(String dayID);
}
