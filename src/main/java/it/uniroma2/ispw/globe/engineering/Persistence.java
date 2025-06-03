package it.uniroma2.ispw.globe.engineering;

import it.uniroma2.ispw.globe.dao.DaoFactory;
import it.uniroma2.ispw.globe.dao.db.InDbDaoFactory;
import it.uniroma2.ispw.globe.dao.fs.InFSDaoFactory;
import it.uniroma2.ispw.globe.dao.memory.InMemoryDaoFactory;

public class Persistence {
    private static Persistence instance = null;

    public static final String IN_MEMORY = "MEMORY";
    public static final String IN_DATABASE = "DB";
    public static final String IN_FILESYSTEM = "FS";

    private String type = IN_DATABASE;
    private String defaultType = IN_DATABASE;

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

    public void setType(String type) {
        this.type = type;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    public void setDefault() {
        setType(defaultType);
    }
}

