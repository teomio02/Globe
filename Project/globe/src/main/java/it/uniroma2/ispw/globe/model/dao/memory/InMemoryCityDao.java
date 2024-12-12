package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.model.City;

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
