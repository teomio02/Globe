package it.ispw.globeapp.Model.Bean;

import it.ispw.globeapp.Model.ItineraryEntity;

public class ItineraryBean {
    private String name;
    private String description;
    private int days;

    public ItineraryBean(String name, String description, int days) {
        this.name = name;
        this.description = description;
        this.days = days;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDays() {
        return this.days;
    }

    public void setDays(int days) {
        this.days = days;
    }

}
