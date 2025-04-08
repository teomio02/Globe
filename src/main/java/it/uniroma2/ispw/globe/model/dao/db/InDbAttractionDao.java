package it.uniroma2.ispw.globe.model.dao.db;

import com.google.gson.JsonObject;
import it.uniroma2.ispw.globe.exception.DBConnectionException;
import it.uniroma2.ispw.globe.model.Attraction;
import it.uniroma2.ispw.globe.model.dao.AttractionDao;
import it.uniroma2.ispw.globe.util.DBConnection;
import it.uniroma2.ispw.globe.util.adapter.PlaceAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_CONNECTION;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_SQL;

public class InDbAttractionDao extends AttractionDao {

    @Override
    public void addAttraction(Attraction attraction) {
        DBConnection connect = DBConnection.getInstance();

        if (getAttraction(attraction.getPlaceID()) == null) {
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
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL + e.getMessage());
            } catch (DBConnectionException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION + e.getMessage());
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public Attraction getAttraction(String attractionID) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Attraction.placeID, Attraction.name, Attraction.latitude, Attraction.longitude, Attraction.city, Attraction.address from Attraction where placeID = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        Attraction attraction = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, attractionID);

            rs = stmt.executeQuery();

            if (rs.next()) {
                JsonObject json = new JsonObject();

                json.addProperty("osm_type", rs.getString("placeID").substring(0,1));
                json.addProperty("osm_id", rs.getString("placeID").substring(1));

                json.addProperty("name", rs.getString("name"));
                json.addProperty("lat", String.valueOf(rs.getDouble("latitude")));
                json.addProperty("lon", String.valueOf(rs.getDouble("longitude")));

                JsonObject address = new JsonObject();
                address.addProperty("city", rs.getString("city"));
                address.addProperty("road", rs.getString("address"));

                json.add("address", address);

                attraction = new PlaceAdapter(json);
            }
        } catch (SQLException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_SQL + e.getMessage());
        } catch (DBConnectionException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_CONNECTION + e.getMessage());
        } finally {
            DBConnection.getInstance().closeConnection(stmt, rs);
        }

        return attraction;
    }
}
