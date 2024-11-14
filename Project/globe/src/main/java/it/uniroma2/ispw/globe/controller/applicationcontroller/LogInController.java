package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.ItineraryEntity;
import it.uniroma2.ispw.globe.model.UserEntity;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

public class LogInController {
    public static UserBean logIn(String name, String password) {
        //mettere in entity e verifica con DB
        List<ItineraryEntity> itineraries = new ArrayList<>();
        List<ItineraryBean> itineraries_b = new ArrayList<>();

        UserEntity user = new UserEntity(name,password,itineraries);
        UserBean userBean = new UserBean(name,password,itineraries_b);
        return userBean;
    }
}
