package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import it.uniroma2.ispw.globe.model.dao.*;
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
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ACCOUNT = "account";

    @Override
    public void addAccount(CredentialsBean credentials){
        DBConnection connect = DBConnection.getInstance();

        if (getAccount(credentials.getUsername()) == null) {
            String query = "insert into Account (username, password, paymentCredential, rating, description, type) values (?,?,?,?,?,?)";
            PreparedStatement stmt = null;

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
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public Account getAccount(String username) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Account.username, Account.password, Account.type, Account.description, Account.rating from Account where username = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                List<Proposal> proposals = getAccountProposals(username, connection);
                List<Itinerary> itineraries = getAccountItineraries(username, connection);
                List<Request> requests = getAccountRequests(username, connection);

                if ((resultSet.getString("type")).equals(AGENCY)) {
                    List<String> types = getAgencyTypes(username, connection);

                    Agency agency = new Agency();
                    agency.setUsername(resultSet.getString(USERNAME));
                    agency.setPassword(resultSet.getString(PASSWORD));
                    agency.setType(resultSet.getString("type"));
                    agency.setProposals(proposals);
                    agency.setItineraries(itineraries);
                    agency.setRequests(requests);
                    agency.setDescription(resultSet.getString("description"));
                    agency.setPreferences(types);
                    agency.setRating(resultSet.getDouble("rating"));

                    return agency;

                } else {

                    User user = new User();
                    user.setUsername(resultSet.getString(USERNAME));
                    user.setPassword(resultSet.getString(PASSWORD));
                    user.setType(resultSet.getString("type"));
                    user.setProposals(proposals);
                    user.setItineraries(itineraries);
                    user.setRequests(requests);

                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return null;
    }

    @Override
    public void removeAccount(CredentialsBean credentials) {
        // da implementare
    }

    @Override
    public List<Agency> getAgenciesByType(List<String> types) {
        // da implementare
        return null;
    }

    @Override
    public Agency getAgencyByProposal(String proposalID) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select accountProposal.account from accountProposal where proposalID = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, proposalID);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Account account = getAccountPrimaryData(resultSet.getString(ACCOUNT));
                if (account instanceof Agency agency) {
                    return agency;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    @Override
    public User getUserByProposal(String proposalID) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select accountProposal.account from accountProposal where proposalID = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, proposalID);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Account account = getAccountPrimaryData(resultSet.getString(ACCOUNT));
                if (account instanceof User user) {
                    return user;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    @Override
    public Agency getAgencyByRequest(String requestID) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select accountRequest.account from accountRequest where requestID = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, requestID);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Account account = getAccountPrimaryData(resultSet.getString(ACCOUNT));
                if (account instanceof Agency agency) {
                    return agency;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    @Override
    public User getUserByRequest(String requestID) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select accountRequest.account from accountRequest where requestID = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, requestID);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Account account = getAccountPrimaryData(resultSet.getString(ACCOUNT));
                if (account instanceof User user) {
                    return user;
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }
    }

    public Account getAccountPrimaryData(String username) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select Account.username, Account.password, Account.type, Account.description, Account.rating from Account where username = ?";
        String agencyTypeQuery = "select agencyType.type from agencyType where agency = ?";


        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        ResultSet otherResultSet = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                List<String> types = new ArrayList<>();
                stmt = connection.prepareStatement(agencyTypeQuery);

                stmt.setString(1, username);
                otherResultSet = stmt.executeQuery();

                while (otherResultSet.next()) {
                    types.add(otherResultSet.getString("type"));
                }


                if ((resultSet.getString("type")).equals(AGENCY)) {

                    Agency agency = new Agency();
                    agency.setUsername(resultSet.getString(USERNAME));
                    agency.setPassword(resultSet.getString(PASSWORD));
                    agency.setType(resultSet.getString("type"));
                    agency.setDescription(resultSet.getString("description"));
                    agency.setPreferences(types);
                    agency.setRating(resultSet.getDouble("rating"));

                    return agency;

                } else {

                    User user = new User();
                    user.setUsername(resultSet.getString(USERNAME));
                    user.setPassword(resultSet.getString(PASSWORD));
                    user.setType(resultSet.getString("type"));

                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
            DBConnection.getInstance().closeConnection(null,otherResultSet);
        }

        return null;
    }

    public List<Proposal> getAccountProposals(String username, Connection connection) {
        String proposalQuery = "select accountProposal.proposalID from accountProposal where account = ?";
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Proposal> proposals = new ArrayList<>();


        try {
            stmt = connection.prepareStatement(proposalQuery);
            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                InDbProposalDao proposalDao = new InDbProposalDao();
                Proposal proposal = proposalDao.getProposal(resultSet.getString("proposalID"));
                proposals.add(proposal);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return proposals;
    }

    public List<Itinerary> getAccountItineraries(String username, Connection connection) {
        String itineraryQuery = "select accountItinerary.itineraryID from accountItinerary where account = ?";
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Itinerary> itineraries = new ArrayList<>();
        try {
            stmt = connection.prepareStatement(itineraryQuery);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                InDbItineraryDao itineraryDao = new InDbItineraryDao();
                Itinerary itinerary = itineraryDao.getItinerary(resultSet.getString("itineraryID"));
                itineraries.add(itinerary);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return itineraries;
    }

    public List<Request> getAccountRequests(String username, Connection connection) {
        String requestQuery = "select accountRequest.requestID from accountRequest where account = ?";
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<Request> requests = new ArrayList<>();
        try {
            stmt = connection.prepareStatement(requestQuery);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                InDbRequestDao requestDao = new InDbRequestDao();
                Request request = requestDao.getRequest(resultSet.getString("requestID"));
                requests.add(request);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return requests;
    }

    public List<String> getAgencyTypes(String username, Connection connection) {
        String agencyTypeQuery = "select agencyType.type from agencyType where agency = ?";
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        List<String> types = new ArrayList<>();
        try {
            stmt = connection.prepareStatement(agencyTypeQuery);

            stmt.setString(1, username);
            resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                types.add(resultSet.getString("type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return types;
    }
}
