package lk.icbt.resort.test;

import lk.icbt.resort.model.dao.DaoFactory;
import lk.icbt.resort.model.entity.Customer;
import lk.icbt.resort.model.entity.Room;
import lk.icbt.resort.util.CrudUtil;
import org.junit.jupiter.api.Assumptions;

import java.time.LocalDate;
import java.util.List;

public final class TestDbSupport {

    private TestDbSupport() {}

    public static void assumeDbUp() {
        try {
            System.out.println("Test===>> DB");
            Integer one = CrudUtil.executeQuery("SELECT 1", rs -> {
                rs.next();
                return rs.getInt(1);
            });
            Assumptions.assumeTrue(one != null && one == 1, "DB is not reachable");
        } catch (Exception e) {
            Assumptions.assumeTrue(false, "DB is not reachable: " + e.getMessage());
        }
    }

    public static int createCustomer(String suffix) throws Exception {
        Customer c = new Customer();
        c.setFullName("Test Customer " + suffix);
        c.setPhone("071000" + suffix.substring(Math.max(0, suffix.length() - 4)));
        c.setEmail("test_" + suffix + "@mail.com");
        c.setNic("NIC-" + suffix);
        return DaoFactory.customerDao().save(c);
    }

    public static Room pickAvailableRoom(LocalDate checkIn, LocalDate checkOut) throws Exception {
        List<Room> available = DaoFactory.roomDao().findAvailableBetween(checkIn, checkOut);
        Assumptions.assumeTrue(available != null && !available.isEmpty(),
                "No available rooms for test date range. Change test dates.");
        return available.get(0);
    }

    public static void cleanupReservationCascade(int reservationId) {
        if (reservationId <= 0) return;
        try { CrudUtil.executeUpdate("DELETE FROM reservation_actions WHERE reservation_id=?", reservationId); } catch (Exception ignored) {}
        try { CrudUtil.executeUpdate("DELETE FROM bills WHERE reservation_id=?", reservationId); } catch (Exception ignored) {}
        try { CrudUtil.executeUpdate("DELETE FROM reservations WHERE reservation_id=?", reservationId); } catch (Exception ignored) {}
    }

    public static void cleanupCustomer(int customerId) {
        if (customerId <= 0) return;
        try { DaoFactory.customerDao().deleteById(customerId); } catch (Exception ignored) {}
    }

    public static String suffix() {
        return String.valueOf(System.currentTimeMillis());
    }
}