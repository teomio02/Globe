package it.uniroma2.ispw.globe;

import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static it.uniroma2.ispw.globe.other.ItineraryType.NATURE;
import static it.uniroma2.ispw.globe.other.ItineraryType.ON_THE_ROAD;
import static it.uniroma2.ispw.globe.other.ProposalState.PENDING;
import static it.uniroma2.ispw.globe.other.UserType.AGENCY;
import static it.uniroma2.ispw.globe.other.UserType.USER;

public class ClasseTest {
    public void creaRichiesta(Account account) {
        RequestDao requestDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getRequestDao();
        AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();

        List<String> cities = new ArrayList<>();
        cities.add(((City) new PlaceAdapter(new CreateItineraryController().getPlaces("roma","administrative").get(0))).getPlaceID());
        List<String> attractions = new ArrayList<>();
        attractions.add(((Attraction) new PlaceAdapter(new CreateItineraryController().getPlaces("colosseo","").get(0))).getPlaceID());
        attractions.add(((Attraction) new PlaceAdapter(new CreateItineraryController().getPlaces("altare della patria","").get(0))).getPlaceID());

        AttractionDao attractionDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAttractionDao();
        CityDao cityDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getCityDao();
        cityDao.addCity((new PlaceAdapter(new CreateItineraryController().getPlaces("roma","administrative").get(0))));
        attractionDao.addAttraction(new PlaceAdapter(new CreateItineraryController().getPlaces("colosseo","").get(0)));
        attractionDao.addAttraction((new PlaceAdapter(new CreateItineraryController().getPlaces("altare della patria","").get(0))));

        System.out.println("cities: " + cities);
        System.out.println("attractions: " + attractions);

        List<String> types = new ArrayList<>();
        types.add(NATURE);
        types.add(ON_THE_ROAD);
        System.out.println("types: " + types);

        User user = (User) accountDao.getAccount("t");

        System.out.println("Agenzia: "+account.getUsername()+" "+account.getRequests());
        System.out.println("User: "+user.getUsername()+" "+user.getRequests());

        Request request = requestDao.createAgencyRequest(UUID.randomUUID().toString(),user.getUsername(),account.getUsername(),PENDING,"description",3,cities,attractions, types);
        requestDao.addAgencyRequest(request,user,(Agency) account);
    }

    public void creaProposta(Account account) {
        ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
        ItineraryDao itineraryDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getItineraryDao();
        AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();

        List<String> cities = new ArrayList<>();
        cities.add(((City) new PlaceAdapter(new CreateItineraryController().getPlaces("roma","administrative").get(0))).getPlaceID());
        List<String> attractions = new ArrayList<>();
        attractions.add(((Attraction) new PlaceAdapter(new CreateItineraryController().getPlaces("colosseo","").get(0))).getPlaceID());
        attractions.add(((Attraction) new PlaceAdapter(new CreateItineraryController().getPlaces("altare della patria","").get(0))).getPlaceID());
        Itinerary itinerary = itineraryDao.createItinerary("1","itinerario di prova","descrizione itinerario di prova",cities,attractions,1);
        new CreateItineraryController().calculateItinerary(itinerary);
        new LogInController().signIn(new CredentialsBean("agenzia","agenzia",AGENCY));
        Agency agency = (Agency) accountDao.getAccount("agenzia");
        itineraryDao.addItinerary(itinerary,agency);

        Proposal proposal = proposalDao.createProposal("1", 99.99, "descrizione proposta di prova", itinerary, (User) account, agency);
        proposalDao.addProposal(proposal);
    }
}
