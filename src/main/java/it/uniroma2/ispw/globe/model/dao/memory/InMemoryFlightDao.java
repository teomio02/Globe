package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.Flight;
import it.uniroma2.ispw.globe.model.dao.FlightDao;

import java.util.ArrayList;
import java.util.List;

public class InMemoryFlightDao extends FlightDao {

    private static InMemoryFlightDao instance = null;

        private List<Flight> flights = new ArrayList<>();

    private InMemoryFlightDao() {}

    public static InMemoryFlightDao getInstance() {
        if (instance == null) {
            instance = new InMemoryFlightDao();
        }
        return instance;
    }
    @Override
    public void addFlight(Flight flight) {
        for (Flight f : flights) {
            if (f.getId().equals(flight.getId())) {
                return;
            }
        }
        flights.add(flight);
    }

    @Override
    public Flight getFlight(String flightID) throws ItemNotFoundException {
        for (Flight f : flights) {
            if (f.getId().equals(flightID)) {
                return f;
            }
        }
        throw new ItemNotFoundException("flight not found");
    }
}
