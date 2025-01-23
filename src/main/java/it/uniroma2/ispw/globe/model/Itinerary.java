package it.uniroma2.ispw.globe.model;

import it.uniroma2.ispw.globe.other.ItineraryType;
import it.uniroma2.ispw.globe.util.decorator.GenericItinerary;

import java.util.List;

public class Itinerary extends GenericItinerary {
    private String itineraryID;
    private String name;
    private String description;
    private int daysNumber;
    private List<Day> days;
    private List<String> types;

    public String getItineraryID() {
        return itineraryID;
    }

    public void setItineraryID(String itineraryID) {
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

    public void setDescription(String description) {
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

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
