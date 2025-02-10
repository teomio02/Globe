package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InDbFlightDao extends FlightDao {

    @Override
    public void addFlight(Flight flight) {
        DBConnection connect = DBConnection.getInstance();

        if (getFlight(flight.getId()) != null) {
            System.out.println("Flight already exists");
        } else {
            String query = "insert into Flight (id,departureTime,arrivalTime) values (?,?,?)";

            PreparedStatement stmt = null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(1, flight.getId());
                stmt.setDouble(2, flight.getDepartureTime());
                stmt.setDouble(3, flight.getArrivalTime());
                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public Flight getFlight(String flightID) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select * from Flight where id = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet= null;

        Flight flight = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, flightID);

            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such day");
            } else {
                flight = new Flight();

                flight.setId(resultSet.getString("id"));
                flight.setDepartureTime(resultSet.getDouble("departureTime"));
                flight.setArrivalTime(resultSet.getDouble("arrivalTime"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return flight;
    }
}
