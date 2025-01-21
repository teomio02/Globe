package it.uniroma2.ispw.globe.other.session;

import it.uniroma2.ispw.globe.model.*;

public class Session {
    private String id;
    private Account account;
    private Itinerary pendingItinerary;
    private Proposal pendingProposal;
    private Request pendingRequest;

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

    //prova
    public Itinerary getTryItinerary() {
        return tryItinerary;
    }
    public void setTryItinerary(Itinerary tryItinerary) {
        this.tryItinerary = tryItinerary;
    }

}
