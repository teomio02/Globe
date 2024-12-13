package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.User;

public abstract class UserDao {
    public abstract void addUser(User user);
    public abstract User getUser(String username);
}
