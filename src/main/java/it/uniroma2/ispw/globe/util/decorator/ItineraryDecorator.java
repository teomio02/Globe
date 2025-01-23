package it.uniroma2.ispw.globe.util.decorator;

public abstract class ItineraryDecorator extends GenericItinerary {
    private GenericItinerary itinerary;

    public ItineraryDecorator( GenericItinerary itinerary){
        this.itinerary = itinerary;
    }

}
