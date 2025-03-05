package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.DBConnection;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;
import it.uniroma2.ispw.globe.util.decorator.Request;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.other.UserType.AGENCY;

public class InDbAccountDao extends AccountDao {

    @Override
    public void addAccount(CredentialsBean credentials){
        DBConnection connect = DBConnection.getInstance();

        if (getAccount(credentials.getUsername()) != null) {
            System.out.println("Account already exists");
        } else {
            String query = "insert into Account (username, password, paymentCredential, rating, description, type) values (?,?,?,?,?,?)";
            PreparedStatement stmt = null;
            ResultSet resultSet= null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(1, credentials.getUsername());
                stmt.setString(2, credentials.getPassword());
                stmt.setString(3, credentials.getPaymentCredentials());
                stmt.setDouble(4, 0);
                stmt.setString(5, credentials.getDescription());
                stmt.setString(6, credentials.getType());
                stmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    public Account getAccount(String username) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select * from Account where username = ?";
        String proposalQuery = "select * from accountProposal where account = ?";
        String itineraryQuery = "select * from accountItinerary where account = ?";
        String requestQuery = "select * from accountRequest where account = ?";
        String agencyTypeQuery = "select * from agencyType where agency = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet= null;
        ResultSet otherResultSet= null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such account");
            } else {

                List<Proposal> proposals = new ArrayList<>();
                List<Itinerary> itineraries = new ArrayList<>();
                List<Request> requests = new ArrayList<>();
                List<String> types = new ArrayList<>();

                stmt = connection.prepareStatement(proposalQuery);

                stmt.setString(1, username);
                otherResultSet = stmt.executeQuery();

                while (otherResultSet.next()) {
                    ProposalDao proposalDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getProposalDao();
                    Proposal proposal = proposalDao.getProposal(otherResultSet.getString("proposalID"));
                    proposals.add(proposal);
                }

                stmt = connection.prepareStatement(itineraryQuery);

                stmt.setString(1, username);
                otherResultSet = stmt.executeQuery();

                while (otherResultSet.next()) {
                    ItineraryDao itineraryDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getItineraryDao();
                    Itinerary itinerary = itineraryDao.getItinerary(otherResultSet.getString("itineraryID"));
                    itineraries.add(itinerary);
                }

                stmt = connection.prepareStatement(requestQuery);

                stmt.setString(1, username);
                otherResultSet = stmt.executeQuery();

                while (otherResultSet.next()) {
                    RequestDao requestDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getRequestDao();
                    Request request = requestDao.getRequest(otherResultSet.getString("requestID"));
                    requests.add(request);
                }

                stmt = connection.prepareStatement(agencyTypeQuery);

                stmt.setString(1, username);
                otherResultSet = stmt.executeQuery();

                while (otherResultSet.next()) {
                    types.add(otherResultSet.getString("type"));
                }


                if ((resultSet.getString("type")).equals(AGENCY)) {

                    Agency agency = new Agency();
                    agency.setUsername(resultSet.getString("username"));
                    agency.setPassword(resultSet.getString("password"));
                    agency.setType(resultSet.getString("type"));
                    agency.setProposals(proposals);
                    agency.setItineraries(itineraries);
                    agency.setRequests(requests);
                    agency.setDescription(resultSet.getString("description"));
                    agency.setPreferences(types);
                    agency.setRating(resultSet.getDouble("rating"));

                    System.out.println(agency.getType()+" - "+agency.getUsername());

                    return agency;

                } else {

                    User user = new User();
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setType(resultSet.getString("type"));
                    user.setProposals(proposals);
                    user.setItineraries(itineraries);
                    user.setRequests(requests);

                    System.out.println(user.getType()+" - "+user.getUsername());

                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }

    @Override
    public void removeAccount(CredentialsBean credentials) {
        DBConnection connect = DBConnection.getInstance();
    }

    @Override
    public List<Agency> getAgenciesByType(List<String> types) {
        DBConnection connect = DBConnection.getInstance();

        // da implementare

        return null;
    }

    public Account getAccountPrimaryData(String username) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select * from Account where username = ?";
        String agencyTypeQuery = "select * from agencyType where agency = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet= null;
        ResultSet otherResultSet= null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such account");
            } else {
                List<String> types = new ArrayList<>();
                stmt = connection.prepareStatement(agencyTypeQuery);

                stmt.setString(1, username);
                otherResultSet = stmt.executeQuery();

                while (otherResultSet.next()) {
                    types.add(otherResultSet.getString("type"));
                }


                if ((resultSet.getString("type")).equals(AGENCY)) {

                    Agency agency = new Agency();
                    agency.setUsername(resultSet.getString("username"));
                    agency.setPassword(resultSet.getString("password"));
                    agency.setType(resultSet.getString("type"));
                    agency.setDescription(resultSet.getString("description"));
                    agency.setPreferences(types);
                    agency.setRating(resultSet.getDouble("rating"));

                    System.out.println(agency.getType());

                    return agency;

                } else {

                    User user = new User();
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setType(resultSet.getString("type"));

                    System.out.println(user.getType());

                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }
}
