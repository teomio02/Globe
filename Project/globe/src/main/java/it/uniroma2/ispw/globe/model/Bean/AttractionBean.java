package it.uniroma2.ispw.globe.model.bean;

public class AttractionBean {
    private String name;

    public AttractionBean(String name) {
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
