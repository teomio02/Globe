package it.uniroma2.ispw.globe;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.other.UserType.AGENCY;

public class ClasseTest {
    public void creaRichiesta(Account account) {
        RequestDao requestDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getRequestDao();

    }

    public void creaProposta(Account account) {
        ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
        ItineraryDao itineraryDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getItineraryDao();
        AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();

        List<String> cities = new ArrayList<>();
        cities.add(((City) new PlaceAdapter(new ManageItineraryController().getPlaces("roma","administrative").get(0))).getPlaceID());
        List<String> attractions = new ArrayList<>();
        attractions.add(((Attraction) new PlaceAdapter(new ManageItineraryController().getPlaces("colosseo","").get(0))).getPlaceID());
        attractions.add(((Attraction) new PlaceAdapter(new ManageItineraryController().getPlaces("altare della patria","").get(0))).getPlaceID());
        Itinerary itinerary = itineraryDao.createItinerary("1","itinerario di prova","descrizione itinerario di prova",cities,attractions,1);
        new ManageItineraryController().calculateItinerary(itinerary);
        new LogInController().signIn(new CredentialsBean("agenzia","agenzia",AGENCY));
        Agency agency = (Agency) accountDao.getAccount("agenzia");
        itineraryDao.addItinerary(itinerary,agency);

        Proposal proposal = proposalDao.createProposal("1", "proposta di prova", 99.99, "descrizione proposta di prova", itinerary, (User) account, agency);
        proposalDao.addProposal(proposal);
    }
}
