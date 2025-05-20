package it.uniroma2.ispw.globe.model.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

import java.util.List;

public class RequestBean {
    private String id;
    private List<String> cities;
    private List<String> attractions;
    private String otherRequests;
    private int dayNum;
    private boolean flight;
    private boolean accommodation;
    private List<String> itineraryType;
    private List<String> agencies;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) throws IncorrectDataException {
        if (cities == null || cities.isEmpty()) {
            throw new IncorrectDataException("Request cities not valid");
        }
        this.cities = cities;
    }

    public List<String> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<String> attractions) throws IncorrectDataException {
        if (attractions == null || attractions.isEmpty()) {
            throw new IncorrectDataException("Request attractions not valid");
        }
        this.attractions = attractions;
    }

    public String getOtherRequests() {
        return otherRequests;
    }

    public void setOtherRequests(String otherRequests)  {
        this.otherRequests = otherRequests;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) throws IncorrectDataException {
        if (dayNum < 1 || dayNum > 99) {
            throw new IncorrectDataException("Request dayNum not valid");
        }
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

    public void setItineraryType(List<String> itineraryType) throws IncorrectDataException {
        if (itineraryType == null || itineraryType.isEmpty()) {
            throw new IncorrectDataException("Request itineraryType not valid");
        }
        this.itineraryType = itineraryType;
    }

    public List<String> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<String> agencies) throws IncorrectDataException {
        if (agencies == null || agencies.isEmpty()) {
            throw new IncorrectDataException("Request agencies not valid");
        }
        this.agencies = agencies;
    }
}

