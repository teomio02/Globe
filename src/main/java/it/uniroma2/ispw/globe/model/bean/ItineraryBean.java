package it.uniroma2.ispw.globe.model.bean;

import javafx.util.Pair;

import java.util.List;

public class ItineraryBean {
    private String id;
    private String name;
    private String description;
    private List<String> types;
    private int duration;
    private List<String> cities;
    private List<String> attractions;
    private double outboundFlightDepartureTime;
    private double outboundFlightArrivalTime;
    private double inboundFlightDepartureTime;
    private double inboundFlightArrivalTime;
    private List<Pair<String, String>> accommodations;

    public ItineraryBean(String id,String name, String description, List<String> types, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.types = types;
        this.duration = duration;
    }

    public ItineraryBean(String id,String name, String description, List<String> types, int duration, List<String> cities, List<String> attractions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.types = types;
        this.duration = duration;
        this.cities = cities;
        this.attractions = attractions;
    }

    public ItineraryBean(String id,String name, String description, List<String> types, int duration, double outboundFlightDepartureTime, double outboundFlightArrivalTime, double inboundFlightDepartureTime, double inboundFlightArrivalTime, List<Pair<String, String>> accommodations) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.types = types;
        this.duration = duration;
        this.outboundFlightDepartureTime = outboundFlightDepartureTime;
        this.outboundFlightArrivalTime = outboundFlightArrivalTime;
        this.inboundFlightDepartureTime = inboundFlightDepartureTime;
        this.inboundFlightArrivalTime = inboundFlightArrivalTime;
        this.accommodations = accommodations;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

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

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
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

    public double getOutboundFlightArrivalTime() {
        return outboundFlightArrivalTime;
    }

    public void setOutboundFlightArrivalTime(double outboundFlightArrivalTime) {
        this.outboundFlightArrivalTime = outboundFlightArrivalTime;
    }

    public double getInboundFlightDepartureTime() {
        return inboundFlightDepartureTime;
    }

    public void setInboundFlightDepartureTime(double inboundFlightDepartureTime) {
        this.inboundFlightDepartureTime = inboundFlightDepartureTime;
    }

    public double getInboundFlightArrivalTime() {
        return inboundFlightArrivalTime;
    }

    public void setInboundFlightArrivalTime(double inboundFlightArrivalTime) {
        this.inboundFlightArrivalTime = inboundFlightArrivalTime;
    }

    public  List<Pair<String, String>> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations( List<Pair<String, String>> accommodations) {
        this.accommodations = accommodations;
    }
}