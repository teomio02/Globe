package it.uniroma2.ispw.globe.model.bean;

import java.util.List;

public class AgencyBean {
    private String name;
    private double rating;
    private List<String> itineraryTypes;

    public AgencyBean(String name, double rating, List<String> itineraryTypes) {
        this.name = name;
        this.rating = rating;
        this.itineraryTypes = itineraryTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getItineraryTypes() {
        return itineraryTypes;
    }

    public void setItineraryTypes(List<String> itineraryTypes) {
        this.itineraryTypes = itineraryTypes;
    }
}
