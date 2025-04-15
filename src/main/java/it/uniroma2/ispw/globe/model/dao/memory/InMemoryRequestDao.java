package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import it.uniroma2.ispw.globe.util.decorator.Request;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.dao.RequestDao;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;

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
    public void addAgencyRequest(Request request, User user, Agency agency) throws DaoException {
        if (getRequest(request.getId()) == null) {
            requests.add(request);
            user.getRequests().add(request);
            agency.getRequests().add(request);
        } else {
            throw new DaoException("addAgencyRequest", DUPLICATE);
        }
    }

    @Override
    public void addUserRequest(RequestBean requestBean, User user, Agency agency) {

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
    public void updateRequest(Request request) {

    }
}
