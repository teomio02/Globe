package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.exception.DBConnectionException;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.util.DBConnection;

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

public class InDbDayDao extends DayDao {

    @Override
    public void addDay(Day day) {
        DBConnection connect = DBConnection.getInstance();

        try {
            getDay(day.getId(), day.getDayNum());
        } catch (ItemNotFoundException exception) {
            String query = "insert into Day (dayNum,itineraryID) values (?,?)";
            String attractionQuery = "insert into dayAttraction (dayNum,itineraryID,attractionID) values (?,?,?)";
            String cityQuery = "insert into dayCity (dayNum,itineraryID,cityID) values (?,?,?)";

            PreparedStatement stmt = null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(2, day.getId());
                stmt.setInt(1, day.getDayNum());
                stmt.execute();

                stmt = connection.prepareStatement(attractionQuery);

                for (Attraction attraction : day.getAttractions()) {
                    InDbAttractionDao attractionDao = new InDbAttractionDao();
                    attractionDao.addAttraction(attraction);

                    stmt.setString(2, day.getId());
                    stmt.setInt(1, day.getDayNum());
                    stmt.setString(3, attraction.getPlaceID());
                    stmt.execute();
                }

                stmt = connection.prepareStatement(cityQuery);

                for (City city : day.getCities()) {
                    InDbCityDao cityDao = new InDbCityDao();
                    cityDao.addCity(city);

                    stmt.setString(2, day.getId());
                    stmt.setInt(1, day.getDayNum());
                    stmt.setString(3, city.getPlaceID());
                    stmt.execute();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (DBConnectionException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION + e.getMessage());
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public Day getDay(String itineraryID, int dayNum) throws ItemNotFoundException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select * from Day where itineraryID = ? and dayNum = ?";
        String attractionQuery = "select * from dayAttraction where itineraryID = ? and dayNum = ?";
        String cityQuery = "select * from dayCity where itineraryID = ? and dayNum = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet= null;

        Day day = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, itineraryID);
            stmt.setInt(2, dayNum);

            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such day");
            } else {
                day = new Day();
                List<Attraction> attractions = new ArrayList<>();
                List<City> cities = new ArrayList<>();

                day.setId(resultSet.getString("itineraryID"));
                day.setDayNum(resultSet.getInt("dayNum"));

                stmt = connection.prepareStatement(attractionQuery);

                stmt.setString(1, itineraryID);
                stmt.setInt(2, dayNum);

                resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    InDbAttractionDao attractionDao = new InDbAttractionDao();
                    Attraction attraction = attractionDao.getAttraction(resultSet.getString("attractionID"));
                    attractions.add(attraction);
                }

                stmt = connection.prepareStatement(cityQuery);

                stmt.setString(1, itineraryID);
                stmt.setInt(2, dayNum);

                resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    InDbCityDao cityDao = new InDbCityDao();
                    City city = cityDao.getCity(resultSet.getString("cityID"));
                    cities.add(city);
                }

                day.setAttractions(attractions);
                day.setCities(cities);
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL + e.getMessage());
            return null;
        } catch (DBConnectionException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION + e.getMessage());
            return null;
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        if (day == null) {
            throw new ItemNotFoundException("day not found");
        }

        return day;
    }
}
