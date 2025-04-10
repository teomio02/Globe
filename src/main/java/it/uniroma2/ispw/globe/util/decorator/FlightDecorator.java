package it.uniroma2.ispw.globe.util.decorator;

import it.uniroma2.ispw.globe.model.Day;
import it.uniroma2.ispw.globe.model.Flight;

import java.util.List;

public class FlightDecorator extends ItineraryDecorator {

    private Flight inFlight;
    private Flight outFlight;

    public FlightDecorator(Itinerary itinerary) {
        super(itinerary);
    }

    public Flight getInFlight() {
        return inFlight;
    }

    public void setInFlight(Flight inFlight) {
        this.inFlight = inFlight;
    }

    public Flight getOutFlight() {
        return outFlight;
    }

    public void setOutFlight(Flight outFlight) {
        this.outFlight = outFlight;
    }

}
