package it.uniroma2.ispw.globe.model.bean;

import java.util.List;

public class CityBean {
    private String name;
    private AccommodationBean accommodation;
    private List<AttractionBean> attractions;

    public CityBean(String name, AccommodationBean accommodation, List<AttractionBean> attractions) {
        this.name = name;
        this.accommodation = accommodation;
        this.attractions = attractions;
    }

    public String getName() { return name; }
    public void setName(String newName) { this.name = newName; }

    public AccommodationBean getAccommodation() { return accommodation; }
    public void setAccommodation(AccommodationBean newAccommodation) { this.accommodation = newAccommodation; }

    public List<AttractionBean> getAttractions() { return attractions; }
    public void setAttractions(List<AttractionBean> newAttractions) { this.attractions = newAttractions; }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name+"-"+accommodation.toString()+"\n");
        for(AttractionBean attraction : attractions) {
            builder.append(attraction.toString()+"\n");
        }
        return builder.toString();
    }
}
