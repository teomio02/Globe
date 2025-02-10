package it.uniroma2.ispw.globe.util.decorator;

import it.uniroma2.ispw.globe.model.Day;

import java.util.List;

public abstract class Itinerary {

    public abstract String getItineraryID();

    public abstract void setItineraryID(String itineraryID);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract int getDaysNumber();

    public abstract void setDaysNumber(int daysNumber);

    public abstract List<Day> getDays();

    public abstract void setDays(List<Day> days);

    public abstract List<String> getTypes();

    public abstract void setTypes(List<String> types);
}
