package it.uniroma2.ispw.globe.model;

import it.uniroma2.ispw.globe.model.bean.ItineraryBean;

import java.util.List;

public class UserEntity {
    private String username;
    private String password;
    private List<ItineraryEntity> itineraries;

    public UserEntity(String username, String password, List<ItineraryEntity> itineraries) {
        this.username = username;
        this.password = password;
        this.itineraries = itineraries;
    }

    public List<ItineraryEntity> getItineraries() {
        return itineraries;
    }
    public void setItineraries(List<ItineraryEntity> itineraries) {
        this.itineraries = itineraries;
    }
}
