package it.uniroma2.ispw.globe.model.dao.db;

import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.Proposal;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.dao.AccountDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.model.dao.ItineraryDao;
import it.uniroma2.ispw.globe.model.dao.ProposalDao;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.util.DBConnection;
import it.uniroma2.ispw.globe.util.decorator.Itinerary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InDbProposalDao extends ProposalDao {

    @Override
    public void addProposal(Proposal proposal,User user, Agency agency) {
        DBConnection connect = DBConnection.getInstance();

        if (getProposal(proposal.getId()) != null) {
            System.out.println("Proposal already exists");
        } else {
            String query = "insert into Proposal (id,itineraryID,price,description,user,agency,accepted) values (?,?,?,?,?,?,?)";
            String accountQuery= "insert into accountProposal (account,proposalID) values (?,?)";

            PreparedStatement stmt = null;

            try {
                Connection connection = connect.getConnection();
                stmt = connection.prepareStatement(query);

                stmt.setString(1, proposal.getId());
                stmt.setString(2,proposal.getItinerary().getItineraryID());
                stmt.setDouble(3, proposal.getPrice());
                stmt.setString(4, proposal.getDescription());
                stmt.setString(5, user.getUsername());
                stmt.setString(6, agency.getUsername());
                stmt.setString(7, proposal.getAccepted());
                stmt.execute();

                stmt = connection.prepareStatement(accountQuery);
                stmt.setString(1, agency.getUsername());
                stmt.setString(2, proposal.getId());
                stmt.execute();

                stmt = connection.prepareStatement(accountQuery);
                stmt.setString(1, user.getUsername());
                stmt.setString(2, proposal.getId());
                stmt.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBConnection.getInstance().closeConnection(stmt,null);
            }
        }
    }

    @Override
    public Proposal getProposal(String id) {
        DBConnection connect = DBConnection.getInstance();

        String query = "select * from Proposal where id = ?";

        PreparedStatement stmt = null;
        ResultSet resultSet= null;

        Proposal proposal = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, id);

            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No such day");
            } else {
                proposal = new Proposal();

                proposal.setId(resultSet.getString("id"));
                proposal.setPrice(resultSet.getDouble("price"));
                proposal.setDescription(resultSet.getString("description"));
                proposal.setAccepted(resultSet.getString("accepted"));

                ItineraryDao itineraryDao = DaoFactory.getFactory(Persistence.getInstance().getType()).getItineraryDao();
                Itinerary itinerary = itineraryDao.getItinerary(resultSet.getString("itineraryID"));

                proposal.setItinerary(itinerary);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,resultSet);
        }

        return proposal;
    }

    @Override
    public void updateProposal(Proposal proposal) {
        DBConnection connect = DBConnection.getInstance();

        String query = "update Proposal set accepted = ? where id = ?";

        PreparedStatement stmt = null;

        try {
            Connection connection = connect.getConnection();
            stmt = connection.prepareStatement(query);

            stmt.setString(1, proposal.getAccepted());
            stmt.setString(2,proposal.getId());
            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBConnection.getInstance().closeConnection(stmt,null);
        }
    }
}
