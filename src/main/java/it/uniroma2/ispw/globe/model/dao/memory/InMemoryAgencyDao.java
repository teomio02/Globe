package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.dao.AgencyDao;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAgencyDao extends AgencyDao {

    private static InMemoryAgencyDao instance = null;

        private List<Agency> agencies = new ArrayList<>();

    private InMemoryAgencyDao() {}

    public static InMemoryAgencyDao getInstance() {
        if (instance == null) {
            instance = new InMemoryAgencyDao();
        }
        return instance;
    }

    @Override
    public void addAgency(Agency agency) {
        for (Agency a : agencies) {
            if (agency.getUsername().equals(a.getUsername())) {
                // eccezione - agenzia già esistente
            }
        }
        agencies.add(agency);
    }

    @Override
    public Agency getAgency(String username) {
        for (Agency agency : agencies) {
            if (agency.getUsername().equals(username)) {
                return agency;
            }
        }
        return null;
    }

    @Override
    public void removeAgency(String username) {
        for (Agency agency : agencies) {
            if (agency.getUsername().equals(username)) {
                for (Proposal proposal : agency.getProposals()) {

                }
            }
        }
    }
}
