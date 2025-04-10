package it.uniroma2.ispw.globe.util.observer;

import it.uniroma2.ispw.globe.model.bean.NavigationData;

public interface ViewObserver {
    void onViewChanged(String viewName, NavigationData data);
}
