package it.uniroma2.ispw.globe.model.bean;

public class OnTheRoadBean {
    private String mode;
    private int dayDrivingHours;

    public OnTheRoadBean(String mode, int dayDrivingHours) {
        this.mode = mode;
        this.dayDrivingHours = dayDrivingHours;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getDayDrivingHours() {
        return dayDrivingHours;
    }

    public void setDayDrivingHours(int dayDrivingHours) {
        this.dayDrivingHours = dayDrivingHours;
    }
}
