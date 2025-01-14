package it.uniroma2.ispw.globe.model.dao;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.controller.applicationcontroller.NominatimAPIClient;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;

import java.io.IOException;

public abstract class CityDao {
    public City createCity(String cityID) {
        JsonObject jsonCity;
        try {
            jsonCity= new NominatimAPIClient().getPlaceByID(cityID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PlaceAdapter(jsonCity);
    }
    public abstract void addCity(City city);
    public abstract City getCity(String cityID);
}
