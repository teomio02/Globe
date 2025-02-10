package it.uniroma2.ispw.globe.other;

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

    public String getType() {
        return type;
    }
}
