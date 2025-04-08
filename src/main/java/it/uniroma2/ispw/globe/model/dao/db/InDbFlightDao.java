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
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_CONNECTION;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_SQL;

public class InDbFlightDao extends FlightDao {

    @Override
    public void addFlight(Flight flight) {
        DBConnection connect = DBConnection.getInstance();

        try {
            getFlight(flight.getId());
        } catch (ItemNotFoundException exception) {
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
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL + e.getMessage());
            } catch (DBConnectionException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION + e.getMessage());
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public Flight getFlight(String flightID) throws ItemNotFoundException {
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

            if (resultSet.next()) {
                flight = new Flight();

                flight.setId(resultSet.getString("id"));
                flight.setDepartureTime(resultSet.getDouble("departureTime"));
                flight.setArrivalTime(resultSet.getDouble("arrivalTime"));
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL + e.getMessage());
        } catch (DBConnectionException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION + e.getMessage());
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        if (flight == null) {
            throw new ItemNotFoundException("flight not found");
        }
        return flight;
    }
}
