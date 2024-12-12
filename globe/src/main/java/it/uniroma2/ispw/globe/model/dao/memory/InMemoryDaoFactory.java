package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.dao.*;


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
    public DayDao getDayDao() {
        return InMemoryDayDao.getInstance();
    }

    @Override
    public ItineraryDao getItineraryDao() {
        return InMemoryItineraryDao.getInstance();
    }
}
