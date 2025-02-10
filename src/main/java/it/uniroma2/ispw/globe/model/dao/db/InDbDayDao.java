package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.DBConnection;
import it.uniroma2.ispw.globe.util.decorator.AccommodationDecorator;
import it.uniroma2.ispw.globe.util.decorator.FlightDecorator;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;
import it.uniroma2.ispw.globe.util.decorator.ItineraryDecorator;
import org.w3c.dom.Attr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InDbDayDao extends DayDao {

    @Override
    public void addDay(Day day) {
        DBConnection connect = DBConnection.getInstance();

        if (getDay(day.getId(), day.getDayNum()) != null) {
            System.out.println("Day already exists");
        } else {
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
                    AttractionDao attractionDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAttractionDao();
                    attractionDao.addAttraction(attraction);

                    stmt.setString(2, day.getId());
                    stmt.setInt(1, day.getDayNum());
                    stmt.setString(3, attraction.getPlaceID());
                    stmt.execute();
                }

                stmt = connection.prepareStatement(cityQuery);

                for (City city : day.getCities()) {
                    CityDao cityDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getCityDao();
                    cityDao.addCity(city);

                    stmt.setString(2, day.getId());
                    stmt.setInt(1, day.getDayNum());
                    stmt.setString(3, city.getPlaceID());
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
    public Day getDay(String itineraryID, int dayNum) {
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
                    AttractionDao attractionDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAttractionDao();
                    Attraction attraction = attractionDao.getAttraction(resultSet.getString("attractionID"));
                    attractions.add(attraction);
                }

                stmt = connection.prepareStatement(cityQuery);

                stmt.setString(1, itineraryID);
                stmt.setInt(2, dayNum);

                resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    CityDao cityDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getCityDao();
                    City city = cityDao.getCity(resultSet.getString("cityID"));
                    cities.add(city);
                }

                day.setAttractions(attractions);
                day.setCities(cities);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return day;
    }
}
