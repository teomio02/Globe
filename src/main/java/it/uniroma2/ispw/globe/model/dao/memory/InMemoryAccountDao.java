package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.dao.AccountDao;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.other.UserType.AGENCY;
import static it.uniroma2.ispw.globe.other.UserType.USER;

public class InMemoryAccountDao extends AccountDao {

    private static InMemoryAccountDao instance = null;

    private List<User> users = new ArrayList<>();
    private List<Agency> agencies = new ArrayList<>();

    private InMemoryAccountDao() {}

    public static InMemoryAccountDao getInstance() {
        if (instance == null) {
            instance = new InMemoryAccountDao();
        }
        return instance;
    }

    @Override
    public void addAccount(CredentialsBean credentials) {
        if (credentials.getType().equals(AGENCY)) {
            for (Agency a : agencies) {
                if (credentials.getUsername().equals(a.getUsername())) {
                    // eccezione - utente già esistente
                }
            }
            Agency agency = new Agency();
            agency.setUsername(credentials.getUsername());
            agency.setPassword(credentials.getPassword());
            agency.setType(credentials.getType());
            agency.setProposals(new ArrayList<>());
            agencies.add(agency);
        } else {
            if (credentials.getType().equals(USER)) {
                for (User u : users) {
                    if (credentials.getUsername().equals(u.getUsername())) {
                        // eccezione - utente già esistente
                        return;
                    }
                }
            }
            User user = new User();
            user.setUsername(credentials.getUsername());
            user.setPassword(credentials.getPassword());
            user.setType(credentials.getType());
            user.setItineraries(new ArrayList<>());
            user.setProposals(new ArrayList<>());
            users.add(user);
        }
    }

    @Override
    public Account getAccount(CredentialsBean credentials) {
        for (Agency a : agencies) {
            if (a.getUsername().equals(credentials.getUsername())) {
                return a;
            }
        }
        for (User u : users) {
            if (u.getUsername().equals(credentials.getUsername())) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void removeAccount(CredentialsBean credentials) {
        if (credentials.getType().equals(AGENCY)) {
            agencies.remove(getAccount(credentials));
        } else {
            users.remove(getAccount(credentials));
        }
    }

    @Override
    public List<Agency> getAgenciesByType(List<String> types) {
        List<Agency> resultAgency = new ArrayList<>();
        for ( Agency agency : agencies){
            int count = 0;
            for ( String type : types){
                if (agency.getPreferences().contains(type)) {
                    count ++;

                }
            }
            if(count == types.size()){
                resultAgency.add(agency);
            }
        }
        return resultAgency;
    }
}
