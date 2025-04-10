package it.uniroma2.ispw.globe.controller.dao.memory;

import it.uniroma2.ispw.globe.model.Accommodation;
import it.uniroma2.ispw.globe.model.dao.memory.InMemoryAccommodationDao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InMemoryAccommodationDaoTest {
    @Test
    void test() {
        InMemoryAccommodationDao dao = InMemoryAccommodationDao.getInstance();

        Accommodation accommodation = new Accommodation();
        accommodation.setId("000");
        accommodation.setName("hotel test");
        accommodation.setAddress("via test 11");

        dao.addAccommodation(accommodation);

        assertDoesNotThrow(() -> dao.getAccommodation("000"));

    }
}
