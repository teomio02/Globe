package it.uniroma2.ispw.globe.model;

public class AttractionEntity {
    private String name;

    public AttractionEntity(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        return builder.toString();
    }
}
