package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.exception.DBConnectionException;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.Accommodation;
import it.uniroma2.ispw.globe.model.dao.AccommodationDao;
import it.uniroma2.ispw.globe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_CONNECTION;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_SQL;

public class InDbAccommodationDao extends AccommodationDao {

    @Override
    public void addAccommodation(Accommodation accommodation) {
        DBConnection connect = DBConnection.getInstance();

        try {
            getAccommodation(accommodation.getId());
        } catch (ItemNotFoundException e) {
            String query = "insert into Accommodation (id,name,address) values (?,?,?)";

            PreparedStatement stmt = null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(1, accommodation.getId());
                stmt.setString(2, accommodation.getName());
                stmt.setString(3, accommodation.getAddress());
                stmt.execute();

            } catch (SQLException exception) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL, exception);
            } catch (DBConnectionException exception) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION, exception);
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public Accommodation getAccommodation(String id) throws ItemNotFoundException {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Accommodation.id , Accommodation.name , Accommodation.address from Accommodation where id = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet= null;

        Accommodation accommodation = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, id);

            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                accommodation = new Accommodation();
                accommodation.setId(resultSet.getString("id"));
                accommodation.setName(resultSet.getString("name"));
                accommodation.setAddress(resultSet.getString("address"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DBConnectionException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION, e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        if (accommodation == null) {
            throw new ItemNotFoundException("accommodation not found");
        }
        return accommodation;
    }
}
