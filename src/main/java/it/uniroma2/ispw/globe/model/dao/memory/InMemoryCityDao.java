package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
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
        for (City c : cities) {
            if (c.getPlaceID().equals(city.getPlaceID())) {
                // errore
                return;
            }
        }
        cities.add(city);
    }

    @Override
    public City getCity(String cityID) throws ItemNotFoundException {
        City cityResult = null;
        for (City city : cities) {
            if (city.getPlaceID().equals(cityID)) {
                cityResult = city;
            }
        }
        if (cityResult == null) {
            cityResult = createCity(cityID);
            if (cityResult != null) {
                addCity(cityResult);
            }
        }
        if (cityResult == null) {
            throw new ItemNotFoundException("city not found");
        }
        return cityResult;
    }
}
