package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.exception.AccountAlreadyExistsException;
import it.uniroma2.ispw.globe.exception.AccountNotFoundException;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;

import java.util.List;

public abstract class AccountDao {
    public abstract Account authenticate(String username, String password) throws AccountNotFoundException;
    public abstract void addAccount(CredentialsBean credentials) throws AccountAlreadyExistsException;
    public abstract Account getAccount(String username) throws AccountNotFoundException;
    public abstract void removeAccount(CredentialsBean credentials);
    public abstract List<Agency> getAgenciesByType(List<String> types);
    public abstract Agency getAgencyByProposal(String proposalID) throws AccountNotFoundException;
    public abstract User getUserByProposal(String proposalID) throws AccountNotFoundException;
    public abstract Agency getAgencyByRequest(String requestID) throws AccountNotFoundException;
    public abstract User getUserByRequest(String requestID) throws AccountNotFoundException;
}
