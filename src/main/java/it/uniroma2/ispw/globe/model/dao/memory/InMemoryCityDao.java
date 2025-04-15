package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.model.City;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;

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
    public void addCity(City city) throws DaoException {
        if (getCity(city.getPlaceID()) == null) {
            cities.add(city);
        } else {
            throw new DaoException("addCity", DUPLICATE);
        }
    }

    @Override
    public City getCity(String cityID) throws DaoException {
        City cityResult = null;
        for (City city : cities) {
            if (city.getPlaceID().equals(cityID)) {
                cityResult = city;
            }
        }
        if (cityResult == null) {
            cityResult = createCity(cityID);
            addCity(cityResult);
        }
        return cityResult;
    }
}
