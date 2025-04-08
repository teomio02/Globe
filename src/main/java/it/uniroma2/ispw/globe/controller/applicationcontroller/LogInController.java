package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.exception.InvalidCredentialsException;
import it.uniroma2.ispw.globe.exception.ItemAlreadyExistsException;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.dao.AccountDao;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.SessionManager;

public class LogInController{

    private static final String GUEST = "guest";

    public String logIn(CredentialsBean credentials) throws ItemAlreadyExistsException, ItemNotFoundException, InvalidCredentialsException {
        AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();

        if (credentials.getType()!=null && credentials.getType().equals(GUEST)) {
            accountDao.addAccount(credentials);
        }
        Account account = accountDao.authenticate(credentials.getUsername(), credentials.getPassword());

        return SessionManager.getInstance().addSession(account);
    }

    public boolean signIn(CredentialsBean credentials) throws ItemAlreadyExistsException, ItemNotFoundException {
        AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
        if (accountDao.getAccount(credentials.getUsername()) != null) {
            return false;
        } else {
            accountDao.addAccount(credentials);

            return true;
        }
    }

    public void logOut(String sessionId) throws ItemNotFoundException {
        Account account = SessionManager.getInstance().getSession(sessionId).getAccount();
        if (account.getType().equals(GUEST)) {
            CredentialsBean credentials = new CredentialsBean(account.getUsername(),account.getPassword(), account.getType());
            AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();
            accountDao.removeAccount(credentials);
        }
    }

    public String getUserType(String sessionID) {
        Account account = SessionManager.getInstance().getSession(sessionID).getAccount();
        return account.getType();
    }
}
