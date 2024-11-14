package it.uniroma2.ispw.globe.model;

public class AccommodationEntity {
    private String name;

    public AccommodationEntity(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        return builder.toString();
    }
}
