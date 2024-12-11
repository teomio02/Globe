package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.dao.AttractionDao;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryItineraryDao;

public class InDbDaoFactory extends DaoFactory {
    @Override
    public ItineraryDao getItineraryDao() {
        return new InDbItineraryDao();
    }

    @Override
    public AttractionDao getAttractionDao() {
        return new InDbAttractionDao();
    }

    @Override
    public CityDao getCityDao() {
        return new InDbCityDao();
    }
}
