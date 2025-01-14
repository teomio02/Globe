package it.uniroma2.ispw.globe.model.bean;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.User;

import java.util.List;

public class RequestBean {
    private String Id;
    private String name;
    private List<String> cities;
    private List<String> attractions;
    private String otherRequests;
    private int dayNum;
    private boolean flight;
    private boolean accommodation;
    private List<String> itineraryType;
    private List<String> agencies;


    public RequestBean(List<String> cities, List<String> attractions, String otherRequests, int dayNum, List<String> agencies, boolean flight, boolean accommodation, List<String> itineraryType ) {
        this.cities = cities;
        this.attractions = attractions;
        this.otherRequests = otherRequests;
        this.dayNum = dayNum;
        this.agencies = agencies;
        this.flight = flight;
        this.accommodation = accommodation;
        this.itineraryType = itineraryType;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public List<String> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<String> attractions) {
        this.attractions = attractions;
    }

    public String getOtherRequests() {
        return otherRequests;
    }

    public void setOtherRequests(String otherRequests) {
        this.otherRequests = otherRequests;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public boolean isFlight() {
        return flight;
    }

    public void setFlight(boolean flight) {
        this.flight = flight;
    }

    public boolean isAccommodation() {
        return accommodation;
    }

    public void setAccommodation(boolean accommodation) {
        this.accommodation = accommodation;
    }

    public List<String> getItineraryType() {
        return itineraryType;
    }

    public void setItineraryType(List<String> itineraryType) {
        this.itineraryType = itineraryType;
    }

    public List<String> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<String> agencies) {
        this.agencies = agencies;
    }
}

