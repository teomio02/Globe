package it.uniroma2.ispw.globe.other.session;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;
import it.uniroma2.ispw.globe.util.decorator.Request;

import java.util.List;

public class Session {
    private String id;
    private Account account;
    private Itinerary pendingItinerary;
    private Proposal pendingProposal;
    private Request pendingRequest;
    private List<Agency> pendingAgencies;
    //prova
    private Itinerary tryItinerary;

    public Session(String id, Account account) {
        this.id = id;
        this.account = account;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Itinerary getPendingItinerary() {
        return pendingItinerary;
    }

    public void setPendingItinerary(Itinerary pendingItinerary) {
        this.pendingItinerary = pendingItinerary;
    }

    public Proposal getPendingProposal() {
        return pendingProposal;
    }

    public void setPendingProposal(Proposal pendingProposal) {
        this.pendingProposal = pendingProposal;
    }

    public Request getPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(Request pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    public List<Agency> getPendingAgencies() {
        return pendingAgencies;
    }

    public void setPendingAgencies(List<Agency> pendingAgencies) {
        this.pendingAgencies = pendingAgencies;
    }

    //prova
    public Itinerary getTryItinerary() {
        return tryItinerary;
    }
    public void setTryItinerary(Itinerary tryItinerary) {
        this.tryItinerary = tryItinerary;
    }

}
