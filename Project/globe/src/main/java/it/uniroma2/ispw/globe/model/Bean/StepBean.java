package it.uniroma2.ispw.globe.model.bean;

import java.util.List;

public class StepBean {
    private List<String> city;
    private List<String> attractions;

    public StepBean(List<String> city, List<String> attractions) {
        this.city = city;
        this.attractions = attractions;
    }

    public List<String> getCity() { return city; }

    public void setCity(List<String> city) { this.city = city; }

    public List<String> getAttractions() { return attractions;}

    public void setAttractions(List<String> attractions) { this.attractions = attractions; }

}
