package it.uniroma2.ispw.globe.model.dao.memory;

import it.uniroma2.ispw.globe.model.Accommodation;
import it.uniroma2.ispw.globe.model.dao.AccommodationDao;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAccommodationDao extends AccommodationDao {

    private static InMemoryAccommodationDao instance = null;

    private List<Accommodation> accommodations = new ArrayList<>();

    private InMemoryAccommodationDao() {}

    public static InMemoryAccommodationDao getInstance() {
        if (instance == null) {
            instance = new InMemoryAccommodationDao();
        }
        return instance;
    }

    @Override
    public void addAccommodation(Accommodation accommodation) {
        for (Accommodation a : accommodations) {
            if (a.getId().equals(accommodation.getId())) {
                return;
            }
        }
        accommodations.add(accommodation);
    }

    @Override
    public Accommodation getAccommodation(String id) {
        for (Accommodation a : accommodations) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }
}
