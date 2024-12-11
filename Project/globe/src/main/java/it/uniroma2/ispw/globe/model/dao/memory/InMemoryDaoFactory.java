package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.dao.AttractionDao;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;


public class InMemoryDaoFactory extends DaoFactory {

    @Override
    public AttractionDao getAttractionDao() {
        return InMemoryAttractionDao.getInstance();
    }

    @Override
    public CityDao getCityDao() {
        return InMemoryCityDao.getInstance();
    }

    @Override
    public ItineraryDao getItineraryDao() {
        return InMemoryItineraryDao.getInstance();
    }
}
