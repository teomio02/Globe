package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.exception.FailedOperationException;

public abstract class AbstractGUIController {

    public abstract void initialize(String sessionID) throws FailedOperationException;

}
