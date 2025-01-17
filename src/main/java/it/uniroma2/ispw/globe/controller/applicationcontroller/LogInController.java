package it.uniroma2.ispw.globe.controller.applicationcontroller;

import it.uniroma2.ispw.globe.ClasseTest;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Itinerary;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;
import it.uniroma2.ispw.globe.model.dao.AccountDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryAccountDao;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.SessionManager;

import static it.uniroma2.ispw.globe.other.UserType.AGENCY;

public class LogInController{

    private static final String GUEST = "guest";

    public String logIn(CredentialsBean credentials) {
        AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();

        if (credentials.getType()!=null && credentials.getType().equals(GUEST)) {
            accountDao.addAccount(credentials);
        }
        Account account = accountDao.getAccount(credentials.getUsername());

        //test
        if (account.getType() != AGENCY) {
            //new ClasseTest().creaProposta(account);
        }
        //----

        return SessionManager.getInstance().addSession(account);
    }

    public boolean signIn(CredentialsBean credentials) {
        AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();
        if (accountDao.getAccount(credentials.getUsername()) != null) {
            return false;
        } else {
            InMemoryAccountDao.getInstance().addAccount(credentials);

            return true;
        }
    }

    public void logOut(String sessionId) {
        Account account = SessionManager.getInstance().getSession(sessionId).getAccount();
        if (account.getType().equals(GUEST)) {
            CredentialsBean credentials = new CredentialsBean(account.getUsername(),account.getPassword(), account.getType());
            InMemoryAccountDao.getInstance().removeAccount(credentials);
        }
        // SessionManager.getInstance().removeSession(sessionId);
    }

    public String getUserType(String username) {
        Account account = SessionManager.getInstance().getSession(username).getAccount();
        return account.getType();
    }
}
