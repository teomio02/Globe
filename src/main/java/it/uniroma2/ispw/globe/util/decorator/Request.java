package it.uniroma2.ispw.globe.util.decorator;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.User;

import java.util.List;

public abstract class Request {

    public abstract String getId();
    public abstract void setId(String id);

    public abstract String getAccepted();
    public abstract void setAccepted(String accepted);

    public abstract String getOtherRequest();
    public abstract void setOtherRequest(String otherRequest);

    public abstract Boolean getFlightRequest();
    public abstract void setFlightRequest(Boolean flightRequest);

    public abstract Boolean getAccommodationRequest();
    public abstract void setAccommodationRequest(Boolean accommodationRequest);

    public abstract int getDayNum();
    public abstract void setDayNum(int dayNum);

    public abstract List<City> getCities();
    public abstract void setCities(List<City> cities);

    public abstract List<Attraction> getAttractions();
    public abstract void setAttractions(List<Attraction> attractions);

    public abstract List<String> getItineraryType();
    public abstract void setItineraryType(List<String> itineraryType);
}
