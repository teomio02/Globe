package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.Itinerary;

public abstract class CityDao {
    public abstract void addCity(City city);
    public abstract City getCity(String cityID);
}
