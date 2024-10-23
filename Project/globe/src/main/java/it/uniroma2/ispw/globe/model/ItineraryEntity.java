package it.uniroma2.ispw.globe.model;

public class ItineraryEntity {
    private String name;
    private String description;
    private int numberOfDays;

    public ItineraryEntity(String name, String description, int numberOfDays) {
        this.name = name;
        this.description = description;
        this.numberOfDays = numberOfDays;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }
    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
}
