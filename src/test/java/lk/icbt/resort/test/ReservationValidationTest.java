package lk.icbt.resort.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationValidationTest {

    @Test
    void checkoutMustBeAfterCheckin() {
        LocalDate checkIn = LocalDate.of(2026, 3, 10);
        LocalDate checkOut = LocalDate.of(2026, 3, 10);

        boolean valid = checkOut.isAfter(checkIn);
        assertFalse(valid);
    }
}