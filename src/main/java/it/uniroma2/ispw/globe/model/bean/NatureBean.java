package it.uniroma2.ispw.globe.model.bean;

public class NatureBean {
    private String difficulty;
    private int trekkingDistance;

    public NatureBean(String difficulty, int trekkingDistance) {
        this.difficulty = difficulty;
        this.trekkingDistance = trekkingDistance;
    }

    public int getTrekkingDistance() {
        return trekkingDistance;
    }

    public void setTrekkingDistance(int trekkingDistance) {
        this.trekkingDistance = trekkingDistance;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
