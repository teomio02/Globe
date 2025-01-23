package it.uniroma2.ispw.globe.model;

public class Accommodation {
    private String name;
    private String address;
    private int firstDay;
    private int lastDay;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getFirstDay() {
        return firstDay;
    }
    public void setFirstDay(int firstDay) {
        this.firstDay = firstDay;
    }
    public int getLastDay() {
        return lastDay;
    }
    public void setLastDay(int lastDay) {
        this.lastDay = lastDay;
    }
}
