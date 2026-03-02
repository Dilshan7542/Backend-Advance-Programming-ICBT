package lk.icbt.resort.test;

import lk.icbt.resort.model.entity.Room;
import lk.icbt.resort.model.exception.ValidationException;
import lk.icbt.resort.model.service.ServiceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceIT {

    private int customer1Id;
    private int customer2Id;
    private int reservation1Id;

    @AfterEach
    void cleanup() {
        TestDbSupport.cleanupReservationCascade(reservation1Id);
        TestDbSupport.cleanupCustomer(customer1Id);
        TestDbSupport.cleanupCustomer(customer2Id);
    }

    @Test
    void createReservation_validInput_shouldSaveReservation() throws Exception {
        TestDbSupport.assumeDbUp();

        String s = TestDbSupport.suffix();
        customer1Id = TestDbSupport.createCustomer(s);

        LocalDate checkIn = LocalDate.now().plusDays(30);
        LocalDate checkOut = LocalDate.now().plusDays(32);

        Room room = TestDbSupport.pickAvailableRoom(checkIn, checkOut);

        reservation1Id = ServiceFactory.reservationService()
                .create(customer1Id, room.getRoomId(), checkIn, checkOut);

        assertTrue(reservation1Id > 0);
    }

    @Test
    void createReservation_invalidDates_shouldFail() throws Exception {
        TestDbSupport.assumeDbUp();

        String s = TestDbSupport.suffix();
        customer1Id = TestDbSupport.createCustomer(s);

        LocalDate checkIn = LocalDate.now().plusDays(10);
        LocalDate checkOut = LocalDate.now().plusDays(10); // same day -> invalid

        Room room = TestDbSupport.pickAvailableRoom(checkIn, checkIn.plusDays(2));

        assertThrows(ValidationException.class, () ->
                ServiceFactory.reservationService().create(customer1Id, room.getRoomId(), checkIn, checkOut)
        );
    }

    @Test
    void createReservation_overlappingSameRoom_shouldFailNotAvailable() throws Exception {
        TestDbSupport.assumeDbUp();

        String s1 = TestDbSupport.suffix();
        customer1Id = TestDbSupport.createCustomer(s1);

        String s2 = TestDbSupport.suffix();
        customer2Id = TestDbSupport.createCustomer(s2);

        LocalDate checkIn = LocalDate.now().plusDays(40);
        LocalDate checkOut = LocalDate.now().plusDays(43);

        Room room = TestDbSupport.pickAvailableRoom(checkIn, checkOut);

        // First reservation (should pass)
        reservation1Id = ServiceFactory.reservationService()
                .create(customer1Id, room.getRoomId(), checkIn, checkOut);
        assertTrue(reservation1Id > 0);

        // Second reservation for same room & overlapping dates (should fail)
        ValidationException ex = assertThrows(ValidationException.class, () ->
                ServiceFactory.reservationService().create(customer2Id, room.getRoomId(), checkIn.plusDays(1), checkOut.minusDays(1))
        );

        assertTrue(ex.getMessage().toLowerCase().contains("not available"));
    }
}