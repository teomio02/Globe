package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.City;
import it.uniroma2.ispw.globe.model.dao.CityDao;
import it.uniroma2.ispw.globe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InDbCityDao extends CityDao {

    @Override
    public void addCity(City city) {
        DBConnection connect = DBConnection.getInstance();

        if (getCity(city.getPlaceID()) != null) {
            System.out.println("City already exists");
        } else {
            String query = "insert into City (placeID,name,country,latitude,longitude) values (?,?,?,?,?)";

            PreparedStatement stmt = null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(1, city.getPlaceID());
                stmt.setString(2, city.getName());
                stmt.setString(3, city.getCountry());
                stmt.setDouble(4, city.getLatitude());
                stmt.setDouble(5, city.getLongitude());
                stmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public City getCity(String cityID) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select placeID from City where placeID = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        City city = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, cityID);

            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such city");
            } else {
                city = createCity(cityID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return city;
    }
}
