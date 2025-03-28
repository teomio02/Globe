package it.uniroma2.ispw.globe.other;

import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.db.InDbDaoFactory;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryDaoFactory;

public class Persistence {
    private static Persistence instance = null;

    public static final String IN_MEMORY = "MEMORY";
    public static final String IN_DATABASE = "DB";

    private static String type;
    private DaoFactory daoFactoryClass;

    private Persistence() {
    }

    public static Persistence getInstance() {
        if (instance == null) {
            instance = new Persistence();
        }
        return instance;
    }

    public void setType(String type) {
        Persistence.type = type;
        if (type.equals(Persistence.IN_MEMORY)) {
            daoFactoryClass = InMemoryDaoFactory.getInstance();
        } else if (type.equals(Persistence.IN_DATABASE)) {
            daoFactoryClass = InDbDaoFactory.getInstance();
        }
    }

    public DaoFactory getDaoFactoryClass() {
        return daoFactoryClass;
    }
}
