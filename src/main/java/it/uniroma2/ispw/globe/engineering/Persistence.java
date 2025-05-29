package it.uniroma2.ispw.globe.engineering;

import it.uniroma2.ispw.globe.dao.DaoFactory;
import it.uniroma2.ispw.globe.dao.db.InDbDaoFactory;
import it.uniroma2.ispw.globe.dao.fs.InFSDaoFactory;
import it.uniroma2.ispw.globe.dao.memory.InMemoryDaoFactory;

public class Persistence {
    private static Persistence instance = null;

    private static final String IN_MEMORY = "MEMORY";
    private static final String IN_DATABASE = "DB";
    private static final String IN_FILESYSTEM = "FS";

    private String type = IN_FILESYSTEM;

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
        } else if (s.equals(IN_MEMORY)) {
            return InMemoryDaoFactory.getInstance();
        } else if (s.equals(IN_FILESYSTEM)) {
            return InFSDaoFactory.getInstance();
        }
        return null;
    }

    public String getType() {
        return type;
    }
}

