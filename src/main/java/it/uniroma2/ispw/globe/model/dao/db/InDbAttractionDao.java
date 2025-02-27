package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.dao.AttractionDao;
import it.uniroma2.ispw.globe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InDbAttractionDao extends AttractionDao {

    @Override
    public void addAttraction(Attraction attraction) {
        DBConnection connect = DBConnection.getInstance();

        if (getAttraction(attraction.getPlaceID()) != null) {
            System.out.println("Attraction already exists");
            return;
        } else {
            String query = "insert into Attraction (placeID,name,city,address,latitude,longitude) values (?,?,?,?,?,?)";

            PreparedStatement stmt = null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(1, attraction.getPlaceID());
                stmt.setString(2, attraction.getName());
                stmt.setString(3, attraction.getCity());
                stmt.setString(4, attraction.getAddress());
                stmt.setDouble(5, attraction.getLatitude());
                stmt.setDouble(6, attraction.getLongitude());
                stmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public Attraction getAttraction(String attractionID) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select placeID from Attraction where placeID = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        Attraction attraction = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, attractionID);

            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such attraction");
            } else {
                attraction = createAttraction(attractionID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return attraction;
    }
}
