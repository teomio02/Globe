package it.uniroma2.ispw.globe.model;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

import java.util.List;

public class Agency extends Account{
    private double rating;
    private String description;
    private List<Proposal> proposals;
    private List<String> preferences;

    @FXML
    private CheckBox ratingCheckBox;


    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Proposal> getProposals() {
        return proposals;
    }

    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }
}
