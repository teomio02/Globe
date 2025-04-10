package it.uniroma2.ispw.globe.util.observer;

import it.uniroma2.ispw.globe.model.bean.NavigationData;

public interface Subject {
    void addObserver(ViewObserver observer);
    void removeObserver(ViewObserver observer);
    void notifyViewChange(String viewPath, NavigationData data);
}
