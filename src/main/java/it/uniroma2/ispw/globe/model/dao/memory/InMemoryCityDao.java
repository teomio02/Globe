package it.uniroma2.ispw.globe.model.dao.memory;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.controller.applicationcontroller.NominatimAPIClient;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InMemoryCityDao extends CityDao {

    private static InMemoryCityDao instance = null;

    private List<City> cities = new ArrayList<>();

    private InMemoryCityDao() {}

    public static InMemoryCityDao getInstance() {
        if (instance == null) {
            instance = new InMemoryCityDao();
        }
        return instance;
    }

    @Override
    public void addCity(City city) {
        for (City c : cities) {
            if (c.getName().equals(city.getName())) {
                // errore
                return;
            }
        }
        cities.add(city);
    }

    @Override
    public City getCity(String cityID) {
        for (City city : cities) {
            if (city.getPlaceID().equals(cityID)) {
                return city;
            }
        }
        return null;
    }
}
