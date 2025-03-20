package it.uniroma2.ispw.globe.model.dao;


import it.uniroma2.ispw.globe.model.dao.db.InDbDaoFactory;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryDaoFactory;

public abstract class DaoFactory {
    public static final String IN_MEMORY = "MEMORY";
    public static final String IN_DATABASE = "DB";

    public static DaoFactory getFactory(String s) {
        if (s.equals(IN_DATABASE)) {
            return new InMemoryDaoFactory();
        }
        return new InDbDaoFactory();
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
