package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Request;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import it.uniroma2.ispw.globe.model.dao.RequestDao;

import java.util.ArrayList;
import java.util.List;

public class InMemoryRequestDao extends RequestDao {

    private static InMemoryRequestDao instance = null;

    private List<Request> requests = new ArrayList<>();

    private InMemoryRequestDao() {}

    public static InMemoryRequestDao getInstance() {
        if (instance == null) {
            instance = new InMemoryRequestDao();
        }
        return instance;
    }

    @Override
    public void addRequest(RequestBean requestBean, User user, Agency agency) {
        Request request = new Request();
        request.setId(requestBean.getID());
        request.setUser(user);
        request.setAgency(agency);
        request.setAccepted(requestBean.getAccepted());
        requests.add(request);
    }

    @Override
    public Request getRequest(String requestId) {
        for (Request request : requests) {
            if (request.getId().equals(requestId)) {
                return request;
            }
        }
        return null;
    }

    @Override
    public void removeRequest(String requestId) {
        requests.remove(getRequest(requestId));
    }
}
