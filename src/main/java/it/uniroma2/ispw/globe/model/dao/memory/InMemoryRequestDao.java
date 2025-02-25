package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.Request;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.AgencyRequestBean;
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
    public void addAgencyRequest(Request request, User user, Agency agency) {
        for (Request savedRequest : requests) {
            if (request.getId().equals(savedRequest.getId())){
                // proposta già esistente
                return;
            }
        }
        requests.add(request);
        request.getAgency().getRequests().add(request);
        request.getUser().getRequests().add(request);
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
