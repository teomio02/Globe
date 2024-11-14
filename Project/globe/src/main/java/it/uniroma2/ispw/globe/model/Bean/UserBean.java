package it.uniroma2.ispw.globe.model.bean;

import java.util.List;

public class UserBean {
    private String username;
    private String password;
    private List<ItineraryBean> itineraries;

    public UserBean(String username, String password, List<ItineraryBean> itineraries) {
        this.username = username;
        this.password = password;
        this.itineraries = itineraries;
    }

    public List<ItineraryBean> getItineraries() { return itineraries; }
    public void setItineraries(List<ItineraryBean> itineraries) { this.itineraries = itineraries; }
}
