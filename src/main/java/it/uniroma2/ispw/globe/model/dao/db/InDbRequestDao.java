package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.AgencyRequestBean;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.DBConnection;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;

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
                stmt.setString(5, request.getDescription());
                stmt.setInt(6, request.getDays());
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
                request = new Request();

                request.setId(resultSet.getString("id"));
                request.setAccepted(resultSet.getString("accepted"));
                request.setDescription(resultSet.getString("description"));
                request.setDays(resultSet.getInt("days"));

                AccountDao accountDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccountDao();
                User user = (User) accountDao.getAccount(resultSet.getString("user"));
                Agency agency = (Agency) accountDao.getAccount(resultSet.getString("agency"));

                request.setUser(user);
                request.setAgency(agency);

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
    public void removeRequest(String requestId) {

    }
}
