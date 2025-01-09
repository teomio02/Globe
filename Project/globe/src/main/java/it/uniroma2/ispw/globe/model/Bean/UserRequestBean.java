package it.uniroma2.ispw.globe.model.Bean;

import java.util.List;

public class UserRequestBean {
    private List<String> cities;
    private List<String> attractions;
    private String otherRequests;
    private int dayNum;
    private List<String> agencies;
    private boolean flight;
    private boolean accommodation;
    private List<String> itineraryType;


    public UserRequestBean(List<String> cities, List<String> attractions, String otherRequests, int dayNum, List<String> agencies, boolean flight, boolean accommodation, List<String> itineraryType ) {
        this.cities = cities;
        this.attractions = attractions;
        this.otherRequests = otherRequests;
        this.dayNum = dayNum;
        this.agencies = agencies;
        this.flight = flight;
        this.accommodation = accommodation;
        this.itineraryType = itineraryType;
    }
}
