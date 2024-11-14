package it.uniroma2.ispw.globe.model;

import it.uniroma2.ispw.globe.model.bean.AccommodationBean;
import it.uniroma2.ispw.globe.model.bean.AttractionBean;

import java.util.List;

public class CityEntity {
    private String name;
    private AccommodationEntity accommodation;
    private List<AttractionEntity> attractions;

    public CityEntity(String name, AccommodationEntity accommodation, List<AttractionEntity> attractions) {
        this.name = name;
        this.accommodation = accommodation;
        this.attractions = attractions;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public AccommodationEntity getAccommodation() { return accommodation; }
    public void setAccommodation(AccommodationEntity accommodation) { this.accommodation = accommodation; }

    public List<AttractionEntity> getAttractions() { return attractions; }
    public void setAttractions(List<AttractionEntity> attractions) { this.attractions = attractions; }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name+"-"+accommodation.toString()+"\n");
        for(AttractionEntity attraction : attractions) {
            builder.append(attraction.toString()+"\n");
        }
        return builder.toString();
    }
}
