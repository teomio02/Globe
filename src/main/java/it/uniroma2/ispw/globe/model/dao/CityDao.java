package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.City;

public abstract class CityDao {
    public abstract void addCity(String cityID);
    public abstract City getCity(String cityID);
}
