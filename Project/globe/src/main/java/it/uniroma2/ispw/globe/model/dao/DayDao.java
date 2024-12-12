package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.Itinerary;

public abstract class DayDao {
    public abstract void addDay(Day day);
    public abstract Day getDay(int dayID);
}
