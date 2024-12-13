package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.dao.*;

public class InDbDaoFactory extends DaoFactory {
    @Override
    public UserDao getUserDao() {
        return new InDbUserDao();
    }

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
