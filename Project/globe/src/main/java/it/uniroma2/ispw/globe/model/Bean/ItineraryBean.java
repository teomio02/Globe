package it.uniroma2.ispw.globe.model.bean;

import java.util.List;

public class ItineraryBean {
    private String name;
    private String description;
    private int numberOfDays;
    private List<CityBean> cities;

    public ItineraryBean(String name, String description, int numberOfDays, List<CityBean> cities) {
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

    public List<CityBean> getCities() {
        return cities;
    }
    public void setCities(List<CityBean> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name+"-"+description+"-"+numberOfDays+"\n");
        for (CityBean city : cities) {
            builder.append(city.toString()+"\n");
        }
        return builder.toString();
    }
}
