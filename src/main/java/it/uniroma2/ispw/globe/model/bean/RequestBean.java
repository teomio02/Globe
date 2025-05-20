package it.uniroma2.ispw.globe.model.bean;

import it.uniroma2.ispw.globe.exception.IncorrectDataException;

import java.util.List;

public class RequestBean {
    private String id;
    private String name;
    private List<String> cities;
    private List<String> attractions;
    private String otherRequests;
    private int dayNum;
    private boolean flight;
    private boolean accommodation;
    private List<String> itineraryType;
    private List<String> agencies;
    private String trekkingDistance;
    private String trekkingDifficulty;
    private String travelMode;
    private String drivingHours;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IncorrectDataException {
        if (name == null || name.isEmpty()) {
            throw new IncorrectDataException("Proposal name not valid");
        }
        this.name = name;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) throws IncorrectDataException {
        if (cities == null || cities.isEmpty()) {
            throw new IncorrectDataException("Proposal cities not valid");
        }
        this.cities = cities;
    }

    public List<String> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<String> attractions) throws IncorrectDataException {
        if (attractions == null || attractions.isEmpty()) {
            throw new IncorrectDataException("Proposal attractions not valid");
        }
        this.attractions = attractions;
    }

    public String getOtherRequests() {
        return otherRequests;
    }

    public void setOtherRequests(String otherRequests) throws IncorrectDataException {
        if (otherRequests == null || otherRequests.isEmpty()) {
            throw new IncorrectDataException("Proposal other request not valid");
        }
        this.otherRequests = otherRequests;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) throws IncorrectDataException {
        if (dayNum < 1 || dayNum > 99) {
            throw new IncorrectDataException("Proposal dayNum not valid");
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
            throw new IncorrectDataException("Proposal itineraryType not valid");
        }
        this.itineraryType = itineraryType;
    }

    public List<String> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<String> agencies) throws IncorrectDataException {
        if (agencies == null || agencies.isEmpty()) {
            throw new IncorrectDataException("Proposal agencies not valid");
        }
        this.agencies = agencies;
    }

    public String getTrekkingDifficulty() { return this.trekkingDifficulty; }

    public void setTrekkingDifficulty(String trekkingDifficulty) { this.trekkingDifficulty = trekkingDifficulty; }

    public String getTrekkingDistance() { return this.trekkingDistance; }

    public void setTrekkingDistance(String trekkingDistance) { this.trekkingDistance = trekkingDistance; }

    public String getTravelMode() { return this.travelMode; }

    public void setTravelMode(String travelMode) { this.travelMode = travelMode; }

    public String getDrivingHours() { return this.drivingHours; }

    public void setDrivingHours(String drivingHours) { this.drivingHours = drivingHours; }
}

