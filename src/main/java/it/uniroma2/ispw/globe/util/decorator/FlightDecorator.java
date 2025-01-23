package it.uniroma2.ispw.globe.util.decorator;

import it.uniroma2.ispw.globe.model.Accommodation;
import it.uniroma2.ispw.globe.model.Flight;

public class FlightDecorator extends ItineraryDecorator {

    private Flight flight;

    public FlightDecorator(GenericItinerary itinerary) {
        super(itinerary);
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
