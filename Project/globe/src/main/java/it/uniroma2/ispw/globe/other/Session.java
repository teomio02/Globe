package it.uniroma2.ispw.globe.other;

//singleton

import it.uniroma2.ispw.globe.model.UserEntity;
import it.uniroma2.ispw.globe.model.bean.UserBean;

public class Session {

    private static Session instance = new Session();

    private UserBean user;

    protected Session() {
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
    public UserBean getUser() {
        return user;
    }

    public synchronized static Session getInstance() {
        if (Session.instance == null)
            Session.instance = new Session();
        return instance;
    }
}
