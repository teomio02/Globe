package it.uniroma2.ispw.globe.model.bean;

import java.util.List;

public class StepBean {
    private String city;
    private List<String> attractions;

    public StepBean(String city, List<String> attractions) {
        this.city = city;
        this.attractions = attractions;
    }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public List<String> getAttractions() { return attractions;}

    public void setAttractions(List<String> attractions) { this.attractions = attractions; }

}
