package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Day;

public abstract class DayDao {
    public abstract void addDay(Day day);
    public abstract Day getDay(int dayID);
}
