package it.uniroma2.ispw.globe.controller.guicontroller;

import javafx.scene.Node;

public class GUIControllerFactory {

    public ManageItineraryGUIController getManageItineraryGUIController(String sessionID) {
        return new ManageItineraryGUIController(sessionID);
    }

    public DisplayItineraryGUIController getDisplayItineraryGUIController(String sessionID, String itineraryID, String requestID, String proposalID, Node root) {
        return new DisplayItineraryGUIController(sessionID, itineraryID, requestID, proposalID, root);
    }

    public CreateProposalGUIController getCreateProposalGUIController(String sessionID, String proposalID, Node root) {
        return new CreateProposalGUIController(sessionID, proposalID, root);
    }
}
