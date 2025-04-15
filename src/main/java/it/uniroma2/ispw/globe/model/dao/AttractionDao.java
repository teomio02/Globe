package it.uniroma2.ispw.globe.model.dao;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.controller.applicationcontroller.NominatimAPIClient;
import it.uniroma2.ispw.globe.exception.ItemAlreadyExistsException;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;

import java.io.IOException;

public abstract class AttractionDao {
    public Attraction createAttraction(String attractionID) {
        JsonObject jsonAttraction;
        jsonAttraction = new NominatimAPIClient().getPlaceByID(attractionID);
        return new PlaceAdapter(jsonAttraction);
    }
    public abstract void addAttraction(Attraction attraction);
    public abstract Attraction getAttraction(String attractionID);
}
