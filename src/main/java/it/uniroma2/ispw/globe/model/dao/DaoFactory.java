package it.uniroma2.ispw.globe.model.dao;


import it.uniroma2.ispw.globe.other.Persistence;

import java.lang.reflect.InvocationTargetException;

public abstract class DaoFactory {

    public static DaoFactory getFactory() {
        return Persistence.getInstance().getDaoFactoryClass();
    }

    public abstract AccommodationDao getAccommodationDao();
    public abstract AccountDao getAccountDao();
    public abstract AttractionDao getAttractionDao();
    public abstract CityDao getCityDao();
    public abstract DayDao getDayDao();
    public abstract FlightDao getFlightDao();
    public abstract ItineraryDao getItineraryDao();
    public abstract ProposalDao getProposalDao();
    public abstract RequestDao getRequestDao();

}
