package it.uniroma2.ispw.globe.util.decorator;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.User;

import java.util.List;

public class OnTheRoadRequestDecorator extends RequestDecorator {
    private String travelMode;
    private double dayDrivingHours;
    // attributi

    public OnTheRoadRequestDecorator(Request request) {
        super(request);
    }

    //get set attributi

    public String getTravelMode() {
        return travelMode;
    }
    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public double getDayDrivingHours() {
        return dayDrivingHours;
    }
    public void setDayDrivingHours(double dayDrivingHours) {
        this.dayDrivingHours = dayDrivingHours;
    }
}
