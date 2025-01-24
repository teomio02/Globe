package it.uniroma2.ispw.globe.model.dao;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.controller.applicationcontroller.NominatimAPIClient;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.Flight;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;

import java.io.IOException;
import java.util.UUID;

public abstract class FlightDao {
    public Flight createFlight(double departureTime, double arrivalTime) {
        Flight flight = new Flight();

        String id = UUID.randomUUID().toString();
        flight.setId(id);
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);

        return flight;
    }
    public abstract void addFlight(Flight flight);
    public abstract Flight getFlight(String flightID);
}
