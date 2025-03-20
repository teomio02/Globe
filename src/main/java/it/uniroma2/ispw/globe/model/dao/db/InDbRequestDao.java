package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.DBConnection;
import it.uniroma2.ispw.globe.util.decorator.Request;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;
import it.uniroma2.ispw.globe.util.decorator.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InDbRequestDao extends RequestDao {

    @Override
    public void addAgencyRequest(Request request, User user, Agency agency) {
        DBConnection connect = DBConnection.getInstance();

        if (getRequest(request.getId()) != null) {
            System.out.println("Request already exists");
        } else {
            String query = "insert into Request (id,user,agency,accepted,description,days) values (?,?,?,?,?,?)";
            String accountQuery= "insert into accountRequest (account,requestID) values (?,?)";
            String cityQuery = "insert into requestCity (requestID,cityID) values (?,?)";
            String attractionQuery = "insert into requestAttraction (requestID,attractionID) values (?,?)";

            PreparedStatement stmt = null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(1, request.getId());
                stmt.setString(2, request.getUser().getUsername());
                stmt.setString(3, request.getAgency().getUsername());
                stmt.setString(4, request.getAccepted());
                stmt.setString(5, request.getOtherRequest());
                stmt.setInt(6, request.getDayNum());
                stmt.execute();

                stmt = connection.prepareStatement(accountQuery);
                stmt.setString(1, request.getAgency().getUsername());
                stmt.setString(2, request.getId());
                stmt.execute();

                stmt = connection.prepareStatement(accountQuery);
                stmt.setString(1, request.getUser().getUsername());
                stmt.setString(2, request.getId());
                stmt.execute();

                stmt = connection.prepareStatement(cityQuery);
                for (City city : request.getCities()) {
                    stmt.setString(1, request.getId());
                    stmt.setString(2, city.getPlaceID());
                    stmt.execute();
                }
                stmt = connection.prepareStatement(attractionQuery);
                for (Attraction attraction : request.getAttractions()) {
                    stmt.setString(1, request.getId());
                    stmt.setString(2, attraction.getPlaceID());
                    stmt.execute();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public void addUserRequest(RequestBean requestBean, User user, Agency agency) {

    }

    @Override
    public Request getRequest(String requestId) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select * from Request where id = ?";
        String cityQuery = "select * from requestCity where requestID = ?";
        String attractionQuery = "select * from requestAttraction where requestID = ?";
        String typeQuery = "select * from requestType where requestID = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet= null;

        Request request = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, requestId);

            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such request");
            } else {
                request = new BaseRequest();

                request.setId(resultSet.getString("id"));
                request.setAccepted(resultSet.getString("accepted"));
                request.setOtherRequest(resultSet.getString("description"));
                request.setDayNum(resultSet.getInt("days"));
                request.setDescription(resultSet.getString("description"));
                request.setDayNum(resultSet.getInt("days"));

                User user;
                Agency agency;
                AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();
                if (accountDao instanceof InDbAccountDao) {
                    System.out.println("Using In-Db account, request: "+ requestId);
                    user = (User) ((InDbAccountDao) accountDao).getAccountPrimaryData(resultSet.getString("user"));
                    agency = (Agency) ((InDbAccountDao) accountDao).getAccountPrimaryData(resultSet.getString("agency"));
                } else {
                    user = (User) accountDao.getAccount(resultSet.getString("user"));
                    agency = (Agency) accountDao.getAccount(resultSet.getString("agency"));
                }

                request.setUser(user);
                request.setAgency(agency);

                List<String> types = new ArrayList<>();
                stmt = connection.prepareStatement(typeQuery);
                stmt.setString(1, requestId);
                resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    types.add(resultSet.getString("type"));
                }
                request.setItineraryType(types);

                List<City> cities = new ArrayList<>();
                stmt = connection.prepareStatement(cityQuery);
                stmt.setString(1, requestId);
                resultSet = stmt.executeQuery();
                CityDao cityDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getCityDao();
                while (resultSet.next()) {
                    City city = cityDao.getCity(resultSet.getString("cityID"));
                    cities.add(city);
                }
                request.setCities(cities);

                List<Attraction> attractions = new ArrayList<>();
                stmt = connection.prepareStatement(attractionQuery);
                stmt.setString(1, requestId);
                resultSet = stmt.executeQuery();
                AttractionDao attractionDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAttractionDao();
                while (resultSet.next()) {
                    Attraction attraction = attractionDao.getAttraction(resultSet.getString("attractionID"));
                    attractions.add(attraction);
                }
                request.setAttractions(attractions);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return request;
    }

    @Override
    public void updateRequest(Request request) {
        DBConnection connect = DBConnection.getInstance();

        String query = "update Proposal set accepted = ? where id = ?";

        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, request.getAccepted());
            stmt.setString(2, request.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }
}
