package it.uniroma2.ispw.globe.model.bean;

public class OnTheRoadBean {
    private String mode;
    private String dayDrivingHours;

    public OnTheRoadBean(String mode, String dayDrivingHours) {
        this.mode = mode;
        this.dayDrivingHours = dayDrivingHours;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDayDrivingHours() {
        return dayDrivingHours;
    }

    public void setDayDrivingHours(String dayDrivingHours) {
        this.dayDrivingHours = dayDrivingHours;
    }
}
