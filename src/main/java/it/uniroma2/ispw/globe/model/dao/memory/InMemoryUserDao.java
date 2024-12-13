package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.dao.UserDao;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserDao extends UserDao {

    private static InMemoryUserDao instance = null;

        private List<User> users = new ArrayList<>();

    private InMemoryUserDao() {}

    public static InMemoryUserDao getInstance() {
        if (instance == null) {
            instance = new InMemoryUserDao();
        }
        return instance;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
