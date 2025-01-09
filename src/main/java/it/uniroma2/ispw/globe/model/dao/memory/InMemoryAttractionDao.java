package it.uniroma2.ispw.globe.model.dao.memory;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.controller.applicationcontroller.APIClient;
import it.uniroma2.ispw.globe.model.dao.AttractionDao;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;

import java.io.IOException;
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
    public void addAttraction(String attractionID) {
        JsonObject jsonAttraction;
        try {
            jsonAttraction = new APIClient().getPlaceByID(attractionID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        attractions.add(new PlaceAdapter(jsonAttraction));
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
