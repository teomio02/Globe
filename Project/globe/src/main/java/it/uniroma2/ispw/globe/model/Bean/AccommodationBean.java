package it.uniroma2.ispw.globe.model.bean;

public class AccommodationBean {
    private String name;

    public AccommodationBean(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        return builder.toString();
    }
}
