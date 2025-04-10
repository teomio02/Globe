package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.model.bean.NavigationData;
import it.uniroma2.ispw.globe.util.observer.Subject;
import it.uniroma2.ispw.globe.util.observer.ViewObserver;

import java.util.ArrayList;
import java.util.List;

public class ViewManager implements Subject {
    private static ViewManager instance;
    private final List<ViewObserver> observers = new ArrayList<>();

    private ViewManager() {}

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    @Override
    public void addObserver(ViewObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(ViewObserver o) {
        observers.remove(o);
    }

    public void notifyViewChange(String viewPath, NavigationData data) {
        for (ViewObserver o : observers) {
            o.onViewChanged(viewPath, data);
        }
    }
}


