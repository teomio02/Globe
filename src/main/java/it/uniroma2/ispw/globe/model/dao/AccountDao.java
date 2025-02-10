package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;

import java.sql.SQLException;
import java.util.List;

public abstract class AccountDao {
    public abstract void addAccount(CredentialsBean credentials);
    public abstract Account getAccount(String username);
    public abstract void removeAccount(CredentialsBean credentials);
    public abstract List<Agency> getAgenciesByType(List<String> types);
}
