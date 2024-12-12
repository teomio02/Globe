package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.dao.AttractionDao;
import it.uniroma2.ispw.globe.model.Attraction;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAttractionDao extends AttractionDao {

    private static InMemoryAttractionDao instance = null;

    private List<Attraction> attractions = new ArrayList<>();

    private InMemoryAttractionDao() {}

    public static InMemoryAttractionDao getInstance() {
        if (instance == null) {
            instance = new InMemoryAttractionDao();
        }
        return instance;
    }

    @Override
    public void addAttraction(Attraction attraction) {
        attractions.add(attraction);
    }

    @Override
    public Attraction getAttraction(String attractionID) {
        for (Attraction attraction : attractions) {
            if (attraction.getPlaceID().equals(attractionID)) {
                return attraction;
            }
        }
        return null;
    }
}
