package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.exception.InvalidCredentialsException;
import it.uniroma2.ispw.globe.exception.ItemAlreadyExistsException;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;

import java.util.List;

public abstract class AccountDao {
    public abstract Account authenticate(String username, String password) throws ItemNotFoundException, InvalidCredentialsException;
    public abstract void addAccount(CredentialsBean credentials) throws ItemAlreadyExistsException;
    public abstract Account getAccount(String username) throws ItemNotFoundException;
    public abstract void removeAccount(CredentialsBean credentials) throws ItemNotFoundException;
    public abstract List<Agency> getAgenciesByType(List<String> types);
    public abstract Agency getAgencyByProposal(String proposalID) throws ItemNotFoundException;
    public abstract User getUserByProposal(String proposalID) throws ItemNotFoundException;
    public abstract Agency getAgencyByRequest(String requestID) throws ItemNotFoundException;
    public abstract User getUserByRequest(String requestID) throws ItemNotFoundException;
}
