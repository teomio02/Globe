package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.exception.DBConnectionException;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.util.DBConnection;
import it.uniroma2.ispw.globe.util.decorator.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_CONNECTION;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_SQL;

public class InDbRequestDao extends RequestDao {

    @Override
    public void addAgencyRequest(Request request, User user, Agency agency) {
        DBConnection connect = DBConnection.getInstance();

        try {
            getRequest(request.getId());
        } catch (ItemNotFoundException exception) {
            String query = "insert into Request (id,user,agency,accepted,description,days) values (?,?,?,?,?,?)";
            String accountQuery= "insert into accountRequest (account,requestID) values (?,?)";
            String cityQuery = "insert into requestCity (requestID,cityID) values (?,?)";
            String attractionQuery = "insert into requestAttraction (requestID,attractionID) values (?,?)";

            PreparedStatement stmt = null;
            PreparedStatement firstAccountStmt = null;
            PreparedStatement secondAccountStmt = null;
            PreparedStatement cityStmt = null;
            PreparedStatement attrStmt = null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(1, request.getId());
                stmt.setString(2, user.getUsername());
                stmt.setString(3, agency.getUsername());
                stmt.setString(4, request.getAccepted());
                stmt.setString(5, request.getOtherRequest());
                stmt.setInt(6, request.getDayNum());
                stmt.execute();

                firstAccountStmt = connection.prepareStatement(accountQuery);
                firstAccountStmt.setString(1, agency.getUsername());
                firstAccountStmt.setString(2, request.getId());
                firstAccountStmt.execute();

                secondAccountStmt = connection.prepareStatement(accountQuery);
                secondAccountStmt.setString(1, user.getUsername());
                secondAccountStmt.setString(2, request.getId());
                secondAccountStmt.execute();

                cityStmt = connection.prepareStatement(cityQuery);
                for (City city : request.getCities()) {
                    cityStmt.setString(1, request.getId());
                    cityStmt.setString(2, city.getPlaceID());
                    cityStmt.execute();
                }
                attrStmt = connection.prepareStatement(attractionQuery);
                for (Attraction attraction : request.getAttractions()) {
                    attrStmt.setString(1, request.getId());
                    attrStmt.setString(2, attraction.getPlaceID());
                    attrStmt.execute();
                }

            } catch (SQLException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL + e.getMessage());
            } catch (DBConnectionException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION + e.getMessage());
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
                DBConnection.getInstance().closeConnection(firstAccountStmt,null);
                DBConnection.getInstance().closeConnection(secondAccountStmt,null);
                DBConnection.getInstance().closeConnection(cityStmt,null);
                DBConnection.getInstance().closeConnection(attrStmt,null);
            }
        }
    }

    @Override
    public void addUserRequest(RequestBean requestBean, User user, Agency agency) {

    }

    @Override
    public Request getRequest(String requestId) throws ItemNotFoundException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select * from Request where id = ?";
        String cityQuery = "select * from requestCity where requestID = ?";
        String attractionQuery = "select * from requestAttraction where requestID = ?";
        String typeQuery = "select * from requestType where requestID = ?";


        PreparedStatement stmt = null;
        PreparedStatement cityStmt = null;
        PreparedStatement attrStmt = null;
        PreparedStatement typeStmt = null;
        ResultSet resultSet= null;

        Request request = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, requestId);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                request = new BaseRequest();

                request.setId(resultSet.getString("id"));
                request.setAccepted(resultSet.getString("accepted"));
                request.setOtherRequest(resultSet.getString("description"));
                request.setDayNum(resultSet.getInt("days"));
                request.setDescription(resultSet.getString("description"));
                request.setDayNum(resultSet.getInt("days"));

                List<String> types = new ArrayList<>();
                typeStmt = connection.prepareStatement(typeQuery);
                typeStmt.setString(1, requestId);
                resultSet = typeStmt.executeQuery();
                while (resultSet.next()) {
                    types.add(resultSet.getString("type"));
                }
                request.setItineraryType(types);

                List<City> cities = new ArrayList<>();
                cityStmt = connection.prepareStatement(cityQuery);
                cityStmt.setString(1, requestId);
                resultSet = cityStmt.executeQuery();
                InDbCityDao cityDao = new InDbCityDao();
                while (resultSet.next()) {
                    City city = cityDao.getCity(resultSet.getString("cityID"));
                    cities.add(city);
                }
                request.setCities(cities);

                List<Attraction> attractions = new ArrayList<>();
                attrStmt = connection.prepareStatement(attractionQuery);
                attrStmt.setString(1, requestId);
                resultSet = attrStmt.executeQuery();
                InDbAttractionDao attractionDao = new InDbAttractionDao();
                while (resultSet.next()) {
                    Attraction attraction = attractionDao.getAttraction(resultSet.getString("attractionID"));
                    attractions.add(attraction);
                }
                request.setAttractions(attractions);
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL + e.getMessage());
        } catch (DBConnectionException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION + e.getMessage());
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
            DBConnection.getInstance().closeConnection(cityStmt,null);
            DBConnection.getInstance().closeConnection(attrStmt,null);
            DBConnection.getInstance().closeConnection(typeStmt,null);
        }

        if (request == null) {
            throw new ItemNotFoundException("request not found");
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL + e.getMessage());
        } catch (DBConnectionException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION + e.getMessage());
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }
}
