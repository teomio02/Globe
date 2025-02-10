package it.uniroma2.ispw.globe.model.bean;

public class NatureBean {
    private String difficulty;
    private String trekkingDistance;

    public NatureBean(String difficulty, String trekkingDistance) {
        this.difficulty = difficulty;
        this.trekkingDistance = trekkingDistance;
    }

    public String getTrekkingDistance() {
        return trekkingDistance;
    }

    public void setTrekkingDistance(String trekkingDistance) {
        this.trekkingDistance = trekkingDistance;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
