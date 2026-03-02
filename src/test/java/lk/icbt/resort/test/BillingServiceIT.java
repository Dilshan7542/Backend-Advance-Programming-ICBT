package lk.icbt.resort.test;

import lk.icbt.resort.model.dao.DaoFactory;
import lk.icbt.resort.model.dto.BillView;
import lk.icbt.resort.model.entity.Room;
import lk.icbt.resort.model.service.ServiceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class BillingServiceIT {

    private int customerId;
    private int reservationId;

    @AfterEach
    void cleanup() {
        TestDbSupport.cleanupReservationCascade(reservationId);
        TestDbSupport.cleanupCustomer(customerId);
    }

    @Test
    void generateBill_shouldCalculateTotalsCorrectly_andPersistBill() throws Exception {
        TestDbSupport.assumeDbUp();

        String s = TestDbSupport.suffix();
        customerId = TestDbSupport.createCustomer(s);

        LocalDate checkIn = LocalDate.now().plusDays(50);
        LocalDate checkOut = LocalDate.now().plusDays(53); // 3 nights

        Room room = TestDbSupport.pickAvailableRoom(checkIn, checkOut);

        reservationId = ServiceFactory.reservationService()
                .create(customerId, room.getRoomId(), checkIn, checkOut);

        BillView bill = ServiceFactory.billingService().generateBill(reservationId);

        assertNotNull(bill);
        assertEquals(reservationId, bill.getReservationId());
        assertEquals(3, bill.getNights());

        Room dbRoom = DaoFactory.roomDao().findById(room.getRoomId());
        BigDecimal rate = dbRoom.getPricePerNight();

        BigDecimal nights = BigDecimal.valueOf(bill.getNights());
        BigDecimal subTotal = rate.multiply(nights).setScale(2, RoundingMode.HALF_UP);
        BigDecimal serviceCharge = subTotal.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal tax = subTotal.multiply(BigDecimal.valueOf(0.02)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subTotal.add(serviceCharge).add(tax).setScale(2, RoundingMode.HALF_UP);

        assertEquals(0, subTotal.compareTo(bill.getSubTotal()));
        assertEquals(0, serviceCharge.compareTo(bill.getServiceCharge()));
        assertEquals(0, tax.compareTo(bill.getTax()));
        assertEquals(0, total.compareTo(bill.getTotal()));

        // bill should exist in DB
        var stored = DaoFactory.billDao().findByReservationId(reservationId);
        assertNotNull(stored);
        assertNotNull(stored.getStatus());
    }
}