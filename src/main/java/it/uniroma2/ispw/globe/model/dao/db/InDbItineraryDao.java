package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
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

            if (getItinerary(itinerary.getItineraryID()) != null) {
                System.out.println("itinerary already exists");
            } else {
                String query = "insert into Itinerary (itineraryID,name,description,daysNumber,inFlight,outFlight) values (?,?,?,?,?,?)";
                PreparedStatement stmt = null;

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
                                AccommodationDao accommodationDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccommodationDao();
                                accommodationDao.addAccommodation(accommodation);

                                String accommodationQuery = "insert into itineraryAccommodation (itineraryID,accommodationID) values (?,?)";

                                try {

                                    stmt = connection.prepareStatement(accommodationQuery);

                                    stmt.setString(1, itinerary.getItineraryID());
                                    stmt.setString(2, accommodation.getId());
                                    stmt.execute();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        if (current instanceof FlightDecorator) {
                            String flightQuery = "update Itinerary set inFlight = ?, outFlight = ? where itineraryID = ?";

                            try {
                                stmt = connection.prepareStatement(flightQuery);

                                FlightDao flightDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getFlightDao();
                                flightDao.addFlight(((FlightDecorator) current).getInFlight());
                                flightDao.addFlight(((FlightDecorator) current).getOutFlight());

                                stmt.setString(1, ((FlightDecorator) current).getInFlight().getId());
                                stmt.setString(2, ((FlightDecorator) current).getOutFlight().getId());
                                stmt.setString(3, itinerary.getItineraryID());
                                stmt.execute();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        current = ((ItineraryDecorator) current).getItinerary();
                    }

                    for (Day day : itinerary.getDays()) {
                        DayDao dayDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getDayDao();
                        dayDao.addDay(day);
                    }

                    account.getItineraries().add(itinerary);

                    query = "insert into accountItinerary (account,itineraryID) values (?,?)";
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1,account.getUsername());
                    stmt.setString(2, itinerary.getItineraryID());
                    stmt.execute();


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    DBConnection.getInstance().closeConnection(stmt,null);
                }
            }



        } else {
            // errore
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
        ResultSet resultSet= null;
        ResultSet otherResultSet= null;

        Itinerary itinerary = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, id);
            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such itinerary");
            } else {

                List<Day> days = new ArrayList<>();
                List<Accommodation> accommodations = new ArrayList<>();
                List<String> types = new ArrayList<>();

                stmt = connection.prepareStatement(dayQuery);

                stmt.setString(1, id);
                otherResultSet = stmt.executeQuery();

                while (otherResultSet.next()) {
                    DayDao dayDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getDayDao();
                    Day day = dayDao.getDay(otherResultSet.getString("itineraryID"),otherResultSet.getInt("dayNum"));
                    days.add(day);
                }

                stmt = connection.prepareStatement(accommodationQuery);

                stmt.setString(1, id);
                otherResultSet = stmt.executeQuery();

                while (otherResultSet.next()) {
                    AccommodationDao accommodationDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getAccommodationDao();
                    Accommodation accommodation = accommodationDao.getAccommodation(otherResultSet.getString("accommodationID"));
                    accommodations.add(accommodation);
                }

                stmt = connection.prepareStatement(typeQuery);

                stmt.setString(1, id);
                otherResultSet = stmt.executeQuery();

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
                    FlightDao flightDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getFlightDao();

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
            if (otherResultSet != null) {
                try {
                    otherResultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return itinerary;
    }

    @Override
    public void removeItinerary(String itineraryID) {
        
    }
}
