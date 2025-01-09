package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Attraction;

public abstract class AttractionDao {
    public abstract void addAttraction(String attractionID);
    public abstract Attraction getAttraction(String attractionID);
}
