package it.uniroma2.ispw.globe.util.decorator;

import it.uniroma2.ispw.globe.model.Accommodation;

public class AccommodationDecorator extends ItineraryDecorator {

    private Accommodation accommodation;

    public AccommodationDecorator(GenericItinerary itinerary) {
        super(itinerary);
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }
}
