package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Request;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import it.uniroma2.ispw.globe.model.dao.RequestDao;

public class InDbRequestDao extends RequestDao {
    @Override
    public void addRequest(RequestBean requestBean, User user, Agency agency) {

    }

    @Override
    public Request getRequest(String requestId) {
        return null;
    }

    @Override
    public void removeRequest(String requestId) {

    }
}
