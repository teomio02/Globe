package it.uniroma2.ispw.globe.other.session;

import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.User;

public class Session {
    private String id;
    private Account account;

    public Session(String id, Account account) {
        this.id = id;
        this.account = account;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
