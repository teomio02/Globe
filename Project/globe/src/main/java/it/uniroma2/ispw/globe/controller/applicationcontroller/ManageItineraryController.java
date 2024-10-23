package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.Bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.ItineraryEntity;

public class ManageItineraryController {
    public ManageItineraryController() {}

    public void addItinerary(ItineraryBean itineraryBean){
        ItineraryEntity itineraryEntity= new ItineraryEntity(itineraryBean.getName(),itineraryBean.getDescription(),itineraryBean.getNumberOfDays());

    }
}
