package it.uniroma2.ispw.globe.util.decorator;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.util.decorator.Request;

import java.util.List;

public class NatureRequestDecorator extends RequestDecorator {
    private String trekkingDistance;
    private String trekkingDifficulty;
    //attributi

    public NatureRequestDecorator(Request request) {
        super(request);
    }
    //get set attributi

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void setId(String id) {
        super.setId(id);
    }

    @Override
    public User getUser() {
        return super.getUser();
    }

    @Override
    public void setUser(User user) {
        super.setUser(user);
    }

    @Override
    public Agency getAgency() {
        return super.getAgency();
    }

    @Override
    public void setAgency(Agency agency) {
        super.setAgency(agency);
    }

    @Override
    public String getAccepted() {
        return super.getAccepted();
    }

    @Override
    public void setAccepted(String accepted) {
        super.setAccepted(accepted);
    }

    @Override
    public String getOtherRequest() {
        return super.getOtherRequest();
    }

    @Override
    public void setOtherRequest(String otherRequest) {
        super.setOtherRequest(otherRequest);
    }

    @Override
    public Boolean getFlightRequest() {
        return super.getFlightRequest();
    }

    @Override
    public void setFlightRequest(Boolean flightRequest) {
        super.setFlightRequest(flightRequest);
    }

    @Override
    public Boolean getAccommodationRequest() {
        return super.getAccommodationRequest();
    }

    @Override
    public void setAccommodationRequest(Boolean accommodationRequest) {
        super.setAccommodationRequest(accommodationRequest);
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
    public int getDayNum() {
        return super.getDayNum();
    }

    @Override
    public void setDayNum(int dayNum) {
        super.setDayNum(dayNum);
    }

    @Override
    public List<City> getCities() {
        return super.getCities();
    }

    @Override
    public void setCities(List<City> cities) {
        super.setCities(cities);
    }

    @Override
    public List<Attraction> getAttractions() {
        return super.getAttractions();
    }

    @Override
    public void setAttractions(List<Attraction> attractions) {
        super.setAttractions(attractions);
    }

    @Override
    public List<String> getItineraryType() {
        return super.getItineraryType();
    }

    @Override
    public void setItineraryType(List<String> itineraryType) {
        super.setItineraryType(itineraryType);
    }

    public String getTrekkingDistance() {
        return this.trekkingDistance;
    }
    public void setTrekkingDistance(String trekkingDistance) {
        this.trekkingDistance = trekkingDistance;
    }

    public String getTrekkingDifficulty() {
        return this.trekkingDifficulty;
    }
    public void setTrekkingDifficulty(String trekkingDifficulty) {
        this.trekkingDifficulty = trekkingDifficulty;
    }
}
