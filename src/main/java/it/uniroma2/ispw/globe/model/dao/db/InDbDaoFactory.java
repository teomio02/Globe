package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryDayDao;
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

    @Override
    public DayDao getDayDao() {
        return new InDbDayDao();
    }
}
