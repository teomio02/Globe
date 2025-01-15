package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.AgencyRequestBean;
import it.uniroma2.ispw.globe.model.bean.RequestBean;

public abstract class RequestDao {
    public abstract void addAgencyRequest(AgencyRequestBean requestBean, User user, Agency agency);
    public abstract void addUserRequest(RequestBean requestBean, User user, Agency agency);
    public abstract Request getRequest(String requestId);
    public abstract void removeRequest(String requestId);
}
