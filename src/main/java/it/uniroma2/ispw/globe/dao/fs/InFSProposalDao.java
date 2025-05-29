package it.uniroma2.ispw.globe.dao.fs;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import it.uniroma2.ispw.globe.dao.AccountDao;
import it.uniroma2.ispw.globe.dao.ItineraryDao;
import it.uniroma2.ispw.globe.dao.ProposalDao;
import it.uniroma2.ispw.globe.exception.DaoException;
import it.uniroma2.ispw.globe.model.*;
import it.uniroma2.ispw.globe.model.dao.*;
import it.uniroma2.ispw.globe.engineering.Persistence;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static it.uniroma2.ispw.globe.exception.DaoException.DUPLICATE;
import static it.uniroma2.ispw.globe.exception.DaoException.GENERAL;

public class InFSProposalDao extends ProposalDao {
    private static final String FILE_PATH = "src/main/resources/it/uniroma2/ispw/data/proposal.csv";


    @Override
    public void addProposal(Proposal proposal, User user, Agency agency) throws DaoException {
        AccountDao accountDao = Persistence.getFactory(Persistence.getInstance().getType()).getAccountDao();

        if (getProposal(proposal.getId()) != null) {
            throw new DaoException("addProposal", DUPLICATE);
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeNext(toCSV(proposal));
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }

        user.getProposals().add(proposal);
        accountDao.updateAccount(user);
        agency.getProposals().add(proposal);
        accountDao.updateAccount(agency);
    }

    @Override
    public Proposal getProposal(String proposalID) throws DaoException {
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].equals(proposalID)) {
                    return fromCsv(nextLine);
                }
            }
            return null;
        } catch (IOException | CsvValidationException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    @Override
    public void updateProposal(Proposal proposal) throws DaoException {
        String[] requestCsv = toCSV(getProposal(proposal.getId()));
        List<String[]> allRows;

        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            allRows = reader.readAll();
        } catch (CsvException | IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }

        for (int i = 0; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            if (row[0].equals(proposal.getId())) {
                allRows.set(i, requestCsv);
                break;
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH, true))) {
            writer.writeAll(allRows);
        } catch (IOException e) {
            throw new DaoException(e.getMessage(),GENERAL);
        }
    }

    public String[] toCSV(Proposal proposal) {
        return new String[] {proposal.getId(),proposal.getItinerary().getItineraryID(),String.valueOf(proposal.getPrice()),proposal.getDescription(),proposal.getAccepted(),};
    }

    public Proposal fromCsv(String[] requestCsv) throws DaoException {
        ItineraryDao itineraryDao = Persistence.getFactory(Persistence.getInstance().getType()).getItineraryDao();

        Proposal proposal = new Proposal();

        proposal.setId(requestCsv[0]);
        proposal.setItinerary(itineraryDao.getItinerary(requestCsv[1]));
        proposal.setPrice(Double.parseDouble(requestCsv[2]));
        proposal.setDescription(requestCsv[3]);
        proposal.setAccepted(requestCsv[4]);

        return proposal;
    }
}
