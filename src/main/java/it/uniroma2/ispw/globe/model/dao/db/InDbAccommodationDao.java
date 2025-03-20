package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.Accommodation;
import it.uniroma2.ispw.globe.model.dao.AccommodationDao;
import it.uniroma2.ispw.globe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InDbAccommodationDao extends AccommodationDao {

    @Override
    public void addAccommodation(Accommodation accommodation) {
        DBConnection connect = DBConnection.getInstance();

        if (getAccommodation(accommodation.getId()) != null) {
            System.out.println("Accommodation already exists");
        } else {
            String query = "insert into Accommodation (id,name,address) values (?,?,?)";

            PreparedStatement stmt = null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(1, accommodation.getId());
                stmt.setString(2, accommodation.getName());
                stmt.setString(3, accommodation.getAddress());
                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public Accommodation getAccommodation(String id) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select * from Accommodation where id = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet= null;

        Accommodation accommodation = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, id);

            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such accommodation");
            } else {
                accommodation = new Accommodation();
                accommodation.setId(resultSet.getString("id"));
                accommodation.setName(resultSet.getString("name"));
                accommodation.setAddress(resultSet.getString("address"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return accommodation;
    }
}
