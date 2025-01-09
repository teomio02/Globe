package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.model.Agency;

public abstract class AgencyDao {
    public abstract void addAgency(Agency agency);
    public abstract Agency getAgency(String username);
    public abstract void removeAgency(String username);
}
