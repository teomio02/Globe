package it.uniroma2.ispw.globe.model;

import it.uniroma2.ispw.globe.other.ItineraryType;

import java.util.List;

public class Itinerary {
    private int itineraryID;
    private String name;
    private String description;
    private int daysNumber;
    private List<Day> days;
    private ItineraryType type;

    public Itinerary() {}

    public int getItineraryID() {
        return itineraryID;
    }

    public void setItineraryID(int itineraryID) {
        this.itineraryID = itineraryID;
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

    public void setDescription(String name) {
        this.description = description;
    }

    public int getDaysNumber() {
        return daysNumber;
    }

    public void setDaysNumber(int daysNumber) {
        this.daysNumber = daysNumber;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public ItineraryType getType() {
        return type;
    }

    public void setType(ItineraryType type) {
        this.type = type;
    }
}
