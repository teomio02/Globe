package it.uniroma2.ispw.globe.util.decorator;

import it.uniroma2.ispw.globe.model.Accommodation;
import it.uniroma2.ispw.globe.model.Day;

import java.util.List;

public class AccommodationDecorator extends ItineraryDecorator {

    private List<Accommodation> accommodations;

    public AccommodationDecorator(Itinerary itinerary) {
        super(itinerary);
    }

    public List<Accommodation> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    @Override
    public String getItineraryID() {
        return super.getItineraryID();
    }

    @Override
    public void setItineraryID(String itineraryID) {
        super.setItineraryID(itineraryID);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    public int getDaysNumber() {
        return super.getDaysNumber();
    }

    @Override
    public void setDaysNumber(int daysNumber) {
        super.setDaysNumber(daysNumber);
    }

    @Override
    public List<Day> getDays() {
        return super.getDays();
    }

    @Override
    public void setDays(List<Day> days) {
        super.setDays(days);
    }

    @Override
    public List<String> getTypes() {
        return super.getTypes();
    }

    @Override
    public void setTypes(List<String> types) {
        super.setTypes(types);
    }
}
