package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.util.DBConnection;
import it.uniroma2.ispw.globe.util.decorator.AccommodationDecorator;
import it.uniroma2.ispw.globe.util.decorator.FlightDecorator;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;
import it.uniroma2.ispw.globe.util.decorator.ItineraryDecorator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InDbItineraryDao extends ItineraryDao {

    @Override
    public void addItinerary(Itinerary itinerary, Account account) {
        if (account != null) {
            DBConnection connect = DBConnection.getInstance();

            if (getItinerary(itinerary.getItineraryID()) == null) {
                String query = "insert into Itinerary (itineraryID,name,description,daysNumber,inFlight,outFlight) values (?,?,?,?,?,?)";
                String accommodationQuery = "insert into itineraryAccommodation (itineraryID,accommodationID) values (?,?)";
                String flightQuery = "update Itinerary set inFlight = ?, outFlight = ? where itineraryID = ?";
                String finalQuery = "insert into accountItinerary (account,itineraryID) values (?,?)";

                PreparedStatement stmt = null;
                PreparedStatement accommodationStmt = null;
                PreparedStatement flightStmt = null;
                PreparedStatement finalStmt = null;

                try {
                    Connection connection = connect.getConnection();
                    stmt = connection.prepareStatement(query);

                    stmt.setString(1, itinerary.getItineraryID());
                    stmt.setString(2, itinerary.getName());
                    stmt.setString(3, itinerary.getDescription());
                    stmt.setInt(4, itinerary.getDaysNumber());
                    stmt.setString(5, null);
                    stmt.setString(6, null);

                    stmt.execute();

                    Itinerary current = itinerary;
                    while (current instanceof ItineraryDecorator) {
                        if (current instanceof AccommodationDecorator) {
                            for (Accommodation accommodation : ((AccommodationDecorator) current).getAccommodations()) {
                                InDbAccommodationDao accommodationDao = new InDbAccommodationDao();
                                accommodationDao.addAccommodation(accommodation);

                                try {

                                    accommodationStmt = connection.prepareStatement(accommodationQuery);

                                    accommodationStmt.setString(1, itinerary.getItineraryID());
                                    accommodationStmt.setString(2, accommodation.getId());
                                    accommodationStmt.execute();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        if (current instanceof FlightDecorator) {

                            try {
                                flightStmt = connection.prepareStatement(flightQuery);

                                InDbFlightDao flightDao = new InDbFlightDao();

                                flightDao.addFlight(((FlightDecorator) current).getInFlight());
                                flightDao.addFlight(((FlightDecorator) current).getOutFlight());

                                flightStmt.setString(1, ((FlightDecorator) current).getInFlight().getId());
                                flightStmt.setString(2, ((FlightDecorator) current).getOutFlight().getId());
                                flightStmt.setString(3, itinerary.getItineraryID());
                                flightStmt.execute();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        current = ((ItineraryDecorator) current).getItinerary();
                    }

                    for (Day day : itinerary.getDays()) {
                        InDbDayDao dayDao = new InDbDayDao();
                        dayDao.addDay(day);
                    }

                    account.getItineraries().add(itinerary);

                    finalStmt = connection.prepareStatement(finalQuery);
                    finalStmt.setString(1,account.getUsername());
                    finalStmt.setString(2, itinerary.getItineraryID());
                    finalStmt.execute();


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    DBConnection.getInstance().closeConnection(stmt,null);
                    DBConnection.getInstance().closeConnection(accommodationStmt,null);
                    DBConnection.getInstance().closeConnection(flightStmt,null);
                    DBConnection.getInstance().closeConnection(finalStmt,null);
                }
            }
        }
    }

    @Override
    public Itinerary getItinerary(String id) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select * from Itinerary where itineraryID = ?";
        String dayQuery = "select * from Day where itineraryID = ?";
        String accommodationQuery = "select * from itineraryAccommodation where itineraryID = ?";
        String typeQuery = "select * from itineraryType where itineraryID = ?";

        PreparedStatement stmt = null;
        PreparedStatement dayStmt = null;
        PreparedStatement accommodationStmt = null;
        PreparedStatement typeStmt = null;

        ResultSet resultSet= null;
        ResultSet otherResultSet= null;

        Itinerary itinerary = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, id);
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {

                List<Day> days = new ArrayList<>();
                List<Accommodation> accommodations = new ArrayList<>();
                List<String> types = new ArrayList<>();

                dayStmt = connection.prepareStatement(dayQuery);

                dayStmt.setString(1, id);
                otherResultSet = dayStmt.executeQuery();

                while (otherResultSet.next()) {
                    InDbDayDao dayDao = new InDbDayDao();
                    Day day = dayDao.getDay(otherResultSet.getString("itineraryID"),otherResultSet.getInt("dayNum"));
                    days.add(day);
                }

                accommodationStmt = connection.prepareStatement(accommodationQuery);

                accommodationStmt.setString(1, id);
                otherResultSet = accommodationStmt.executeQuery();

                while (otherResultSet.next()) {
                    InDbAccommodationDao accommodationDao = new InDbAccommodationDao();
                    Accommodation accommodation = accommodationDao.getAccommodation(otherResultSet.getString("accommodationID"));
                    accommodations.add(accommodation);
                }

                typeStmt = connection.prepareStatement(typeQuery);

                typeStmt.setString(1, id);
                otherResultSet = typeStmt.executeQuery();

                while (otherResultSet.next()) {
                    types.add(otherResultSet.getString("type"));
                }

                itinerary = new BaseItinerary();

                itinerary.setItineraryID(resultSet.getString("itineraryID"));
                itinerary.setName(resultSet.getString("name"));
                itinerary.setDescription(resultSet.getString("description"));
                itinerary.setDaysNumber(resultSet.getInt("daysNumber"));
                itinerary.setDays(days);
                itinerary.setTypes(types);

                if (!accommodations.isEmpty()) {
                    AccommodationDecorator accommodationItinerary = new AccommodationDecorator(itinerary);
                    accommodationItinerary.setAccommodations(accommodations);
                    itinerary = accommodationItinerary;
                }

                if (resultSet.getString("inFlight")!=null) {
                    InDbFlightDao flightDao = new InDbFlightDao();
                    Flight inFlight = flightDao.getFlight(resultSet.getString("inFlight"));
                    Flight outFlight = flightDao.getFlight(resultSet.getString("outFlight"));

                    FlightDecorator flightItinerary = new FlightDecorator(itinerary);
                    flightItinerary.setInFlight(inFlight);
                    flightItinerary.setOutFlight(outFlight);
                    itinerary = flightItinerary;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
            DBConnection.getInstance().closeConnection(dayStmt,otherResultSet);
            DBConnection.getInstance().closeConnection(accommodationStmt,null);
            DBConnection.getInstance().closeConnection(typeStmt,null);
        }

        return itinerary;
    }

    @Override
    public void removeItinerary(String itineraryID) {
        
    }
}
