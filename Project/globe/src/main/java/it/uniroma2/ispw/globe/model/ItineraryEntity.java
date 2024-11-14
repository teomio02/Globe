package it.uniroma2.ispw.globe.model;

import it.uniroma2.ispw.globe.model.bean.CityBean;

import java.util.List;

public class ItineraryEntity {
    private String name;
    private String description;
    private int numberOfDays;
    private List<CityEntity> cities;

    public ItineraryEntity(String name, String description, int numberOfDays, List<CityEntity> cities) {
        this.name = name;
        this.description = description;
        this.numberOfDays = numberOfDays;
        this.cities = cities;
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

    public List<CityEntity> getCities() {
        return cities;
    }
    public void setCities(List<CityEntity> cities) {
        this.cities = cities;
    }

}
