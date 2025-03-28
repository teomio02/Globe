package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.dao.AccountDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryAccountDao;
import it.uniroma2.ispw.globe.other.session.SessionManager;

public class LogInController{

    private static final String GUEST = "guest";

    public String logIn(CredentialsBean credentials) {
        AccountDao accountDao = DaoFactory.getFactory().getAccountDao();

        if (credentials.getType()!=null && credentials.getType().equals(GUEST)) {
            accountDao.addAccount(credentials);
        }
        Account account = accountDao.getAccount(credentials.getUsername());

        return SessionManager.getInstance().addSession(account);
    }

    public boolean signIn(CredentialsBean credentials) {
        AccountDao accountDao = DaoFactory.getFactory().getAccountDao();
        if (accountDao.getAccount(credentials.getUsername()) != null) {
            return false;
        } else {
            accountDao.addAccount(credentials);

            return true;
        }
    }

    public void logOut(String sessionId) {
        Account account = SessionManager.getInstance().getSession(sessionId).getAccount();
        if (account.getType().equals(GUEST)) {
            CredentialsBean credentials = new CredentialsBean(account.getUsername(),account.getPassword(), account.getType());
            InMemoryAccountDao.getInstance().removeAccount(credentials);
        }
    }

    public String getUserType(String sessionID) {
        Account account = SessionManager.getInstance().getSession(sessionID).getAccount();
        return account.getType();
    }
}
