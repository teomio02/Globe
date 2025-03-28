package it.uniroma2.ispw.globe.other;

import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.db.InDbDaoFactory;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryDaoFactory;

public class Persistence {
    private static Persistence instance = null;

    private static final String IN_MEMORY = "MEMORY";
    private static final String IN_DATABASE = "DB";

    private String type = IN_DATABASE;

    private Persistence() {}

    public static Persistence getInstance() {
        if (instance == null) {
            instance = new Persistence();
        }
        return instance;
    }

    public static DaoFactory getFactory(String s) {
        if (s.equals(IN_DATABASE)) {
            return InDbDaoFactory.getInstance();
        }
        return InMemoryDaoFactory.getInstance();
    }

    public String getType() {
        return type;
    }
}

