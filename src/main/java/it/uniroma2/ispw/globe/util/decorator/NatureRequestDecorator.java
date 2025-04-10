package it.uniroma2.ispw.globe.util.decorator;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.User;

import java.util.List;

public class NatureRequestDecorator extends RequestDecorator {
    private String trekkingDistance;
    private String trekkingDifficulty;
    //attributi

    public NatureRequestDecorator(Request request) {
        super(request);
    }
    //get set attributi

    public String getTrekkingDistance() {
        return this.trekkingDistance;
    }
    public void setTrekkingDistance(String trekkingDistance) {
        this.trekkingDistance = trekkingDistance;
    }

    public String getTrekkingDifficulty() {
        return this.trekkingDifficulty;
    }
    public void setTrekkingDifficulty(String trekkingDifficulty) {
        this.trekkingDifficulty = trekkingDifficulty;
    }
}
