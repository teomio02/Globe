package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.dao.AccountDao;
import it.uniroma2.ispw.globe.model.dao.CityDao;

import java.util.List;

public class InDbAccountDao extends AccountDao {

    @Override
    public void addAccount(CredentialsBean credentials) {

    }

    @Override
    public Account getAccount(String username) {
        return null;
    }

    @Override
    public void removeAccount(CredentialsBean credentials) {

    }

    @Override
    public List<Agency> getAgenciesByType(List<String> types) {
        return null;
    }
}
