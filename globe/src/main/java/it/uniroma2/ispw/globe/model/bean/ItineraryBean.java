package it.uniroma2.ispw.globe.model.bean;

import java.util.List;
import java.util.Map;

public class ItineraryBean {
    private String name;
    private String description;
    private String type;
    private int duration;
    private List<String> cities;
    private List<String> attractions;
    private double outboundFlightDepartureTime;
    private double outboundFlightDuration;
    private double inboundFlightDepartureTime;
    private double inboundFlightDuration;
    private Map<String, String> accommodations;

    public ItineraryBean(String name, String description, String type, int duration, List<String> cities, List<String> attractions, double outboundFlightDepartureTime, double outboundFlightDuration, double inboundFlightDepartureTime, double inboundFlightDuration, Map<String, String> accommodations) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.duration = duration;
        this.cities = cities;
        this.attractions = attractions;
        this.outboundFlightDepartureTime = outboundFlightDepartureTime;
        this.outboundFlightDuration = outboundFlightDuration;
        this.inboundFlightDepartureTime = inboundFlightDepartureTime;
        this.inboundFlightDuration = inboundFlightDuration;
        this.accommodations = accommodations;
    }

    public ItineraryBean(String name, String description, String type, int duration, double outboundFlightDepartureTime, double outboundFlightDuration, double inboundFlightDepartureTime, double inboundFlightDuration, Map<String, String> accommodations) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.duration = duration;
        this.outboundFlightDepartureTime = outboundFlightDepartureTime;
        this.outboundFlightDuration = outboundFlightDuration;
        this.inboundFlightDepartureTime = inboundFlightDepartureTime;
        this.inboundFlightDuration = inboundFlightDuration;
        this.accommodations = accommodations;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public double getOutboundFlightDepartureTime() {
        return outboundFlightDepartureTime;
    }

    public void setOutboundFlightDepartureTime(double outboundFlightDepartureTime) {
        this.outboundFlightDepartureTime = outboundFlightDepartureTime;
    }

    public double getOutboundFlightDuration() {
        return outboundFlightDuration;
    }

    public void setOutboundFlightDuration(double outboundFlightDuration) {
        this.outboundFlightDuration = outboundFlightDuration;
    }

    public double getInboundFlightDepartureTime() {
        return inboundFlightDepartureTime;
    }

    public void setInboundFlightDepartureTime(double inboundFlightDepartureTime) {
        this.inboundFlightDepartureTime = inboundFlightDepartureTime;
    }

    public double getInboundFlightDuration() {
        return inboundFlightDuration;
    }

    public void setInboundFlightDuration(double inboundFlightDuration) {
        this.inboundFlightDuration = inboundFlightDuration;
    }

    public Map<String, String> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(Map<String, String> accommodations) {
        this.accommodations = accommodations;
    }
}