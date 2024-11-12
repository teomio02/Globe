package it.uniroma2.ispw.globe.model.dao;

import it.uniroma2.ispw.globe.controller.applicationcontroller.Connect;
import it.uniroma2.ispw.globe.model.ItineraryEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItineraryDAO {
    private Connect connect;

    public void addItinerary(ItineraryEntity itinerary) throws SQLException {
        connect = new Connect();
        String query = "insert into Itinerary (itName, daysNum, itDescription) values (?,?,?)";
        PreparedStatement stmt = null;
        ResultSet resultSet= null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, itinerary.getName());
            stmt.setInt(2, itinerary.getNumberOfDays());
            stmt.setString(3, itinerary.getDescription());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) stmt.close();
        }

    }
}
