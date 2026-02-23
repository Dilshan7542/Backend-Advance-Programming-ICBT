package lk.icbt.resort.model.dao.impl;

import lk.icbt.resort.model.dao.ReservationDao;
import lk.icbt.resort.model.entity.Reservation;
import lk.icbt.resort.util.CrudUtil;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDaoImpl implements ReservationDao {

    @Override
    public int save(Reservation reservation) throws Exception {
        String sql = "INSERT INTO reservations (customer_id, room_id, check_in, check_out, nights, status) VALUES (?,?,?,?,?,?)";
        long id = CrudUtil.executeInsertAndReturnKey(sql,
                reservation.getCustomerId(),
                reservation.getRoomId(),
                Date.valueOf(reservation.getCheckIn()),
                Date.valueOf(reservation.getCheckOut()),
                reservation.getNights(),
                reservation.getStatus() == null ? "CONFIRMED" : reservation.getStatus());
        return (int) id;
    }

    @Override
    public Reservation findByIdWithJoins(int reservationId) throws Exception {
        String sql = baseSelect() + "\nWHERE r.reservation_id = ?";
        return CrudUtil.executeQuery(sql, rs -> {
            if (!rs.next()) return null;
            return mapReservation(rs);
        }, reservationId);
    }

    @Override
    public List<Reservation> findAllWithJoins() throws Exception {
        String sql = baseSelect() + "\nORDER BY r.reservation_id DESC";
        return CrudUtil.executeQuery(sql, rs -> {
            List<Reservation> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapReservation(rs));
            }
            return list;
        });
    }

    @Override
    public List<Reservation> findActiveWithJoins(LocalDate today) throws Exception {
        String sql = baseSelect() + "\nWHERE r.status='CONFIRMED' AND r.check_out >= ?\nORDER BY r.reservation_id DESC";
        return CrudUtil.executeQuery(sql, rs -> {
            List<Reservation> list = new ArrayList<>();
            while (rs.next()) list.add(mapReservation(rs));
            return list;
        }, Date.valueOf(today));
    }

    @Override
    public List<Reservation> findHistoryWithJoins(LocalDate today) throws Exception {
        String sql = baseSelect() + "\nWHERE r.status='CANCELLED' OR r.check_out < ?\nORDER BY r.reservation_id DESC";
        return CrudUtil.executeQuery(sql, rs -> {
            List<Reservation> list = new ArrayList<>();
            while (rs.next()) list.add(mapReservation(rs));
            return list;
        }, Date.valueOf(today));
    }

    @Override
    public boolean cancelById(int reservationId) throws Exception {
        String sql = "UPDATE reservations SET status='CANCELLED' WHERE reservation_id=?";
        return CrudUtil.executeUpdate(sql, reservationId) > 0;
    }

    @Override
    public boolean existsDuplicateConfirmed(int customerId, int roomId, LocalDate checkIn, LocalDate checkOut) throws Exception {
        String sql = "SELECT COUNT(*) AS cnt FROM reservations WHERE customer_id=? AND room_id=? AND check_in=? AND check_out=? AND status='CONFIRMED'";
        Integer cnt = CrudUtil.executeQuery(sql, rs -> {
            if (!rs.next()) return 0;
            return rs.getInt("cnt");
        }, customerId, roomId, Date.valueOf(checkIn), Date.valueOf(checkOut));
        return cnt != null && cnt > 0;
    }

    @Override
    public boolean customerHasOverlapConfirmed(int customerId, LocalDate checkIn, LocalDate checkOut) throws Exception {
        // overlap: existing.check_in < new.checkOut AND existing.check_out > new.checkIn
        String sql = """
                SELECT COUNT(*) AS cnt
                FROM reservations r
                WHERE r.customer_id = ?
                  AND r.status='CONFIRMED'
                  AND r.check_in < ?
                  AND r.check_out > ?
                """;
        Integer cnt = CrudUtil.executeQuery(sql, rs -> {
            if (!rs.next()) return 0;
            return rs.getInt("cnt");
        }, customerId, Date.valueOf(checkOut), Date.valueOf(checkIn));
        return cnt != null && cnt > 0;
    }

    @Override
    public boolean existsAnyForCustomer(int customerId) throws Exception {
        String sql = "SELECT COUNT(*) AS cnt FROM reservations WHERE customer_id=?";
        Integer cnt = CrudUtil.executeQuery(sql, rs -> {
            if (!rs.next()) return 0;
            return rs.getInt("cnt");
        }, customerId);
        return cnt != null && cnt > 0;
    }

    @Override
    public List<Integer> findCustomerIdsWithAnyReservations() throws Exception {
        String sql = "SELECT DISTINCT customer_id FROM reservations";
        return CrudUtil.executeQuery(sql, rs -> {
            List<Integer> list = new ArrayList<>();
            while (rs.next()) list.add(rs.getInt("customer_id"));
            return list;
        });
    }

    private String baseSelect() {
        return """
                SELECT r.reservation_id, r.customer_id, r.room_id, r.check_in, r.check_out, r.nights, r.status, r.created_at,
                       c.full_name AS customer_name,
                       rm.room_no, rm.room_type,
                       COALESCE(b.status, 'UNPAID') AS bill_status
                FROM reservations r
                JOIN customers c ON r.customer_id = c.customer_id
                JOIN rooms rm ON r.room_id = rm.room_id
                LEFT JOIN bills b ON b.reservation_id = r.reservation_id
                """;
    }

    private Reservation mapReservation(java.sql.ResultSet rs) throws java.sql.SQLException {
        Reservation r = new Reservation();
        r.setReservationId(rs.getInt("reservation_id"));
        r.setCustomerId(rs.getInt("customer_id"));
        r.setRoomId(rs.getInt("room_id"));

        Date ci = rs.getDate("check_in");
        Date co = rs.getDate("check_out");
        r.setCheckIn(ci != null ? ci.toLocalDate() : null);
        r.setCheckOut(co != null ? co.toLocalDate() : null);

        r.setNights(rs.getInt("nights"));
        r.setStatus(rs.getString("status"));

        Timestamp ts = rs.getTimestamp("created_at");
        LocalDateTime created = (ts != null) ? ts.toLocalDateTime() : null;
        r.setCreatedAt(created);

        r.setCustomerName(rs.getString("customer_name"));
        r.setRoomNo(rs.getString("room_no"));
        r.setRoomType(rs.getString("room_type"));
        r.setBillStatus(rs.getString("bill_status"));
        return r;
    }
    @Override
    public Reservation findLatestByCustomerWithBillStatus(int customerId) throws Exception {
        String sql = """
            SELECT r.reservation_id, r.customer_id, r.room_id, r.check_in, r.check_out, r.nights, r.status, r.created_at,
                   COALESCE(b.status, 'UNPAID') AS bill_status
            FROM reservations r
            LEFT JOIN bills b ON b.reservation_id = r.reservation_id
            WHERE r.customer_id = ?
            ORDER BY r.check_out DESC, r.reservation_id DESC
            LIMIT 1
            """;

        return CrudUtil.executeQuery(sql, rs -> {
            if (!rs.next()) return null;

            Reservation r = new Reservation();
            r.setReservationId(rs.getInt("reservation_id"));
            r.setCustomerId(rs.getInt("customer_id"));
            r.setRoomId(rs.getInt("room_id"));

            Date ci = rs.getDate("check_in");
            Date co = rs.getDate("check_out");
            r.setCheckIn(ci != null ? ci.toLocalDate() : null);
            r.setCheckOut(co != null ? co.toLocalDate() : null);

            r.setNights(rs.getInt("nights"));
            r.setStatus(rs.getString("status"));

            Timestamp ts = rs.getTimestamp("created_at");
            r.setCreatedAt(ts != null ? ts.toLocalDateTime() : null);

            r.setBillStatus(rs.getString("bill_status"));
            return r;
        }, customerId);
    }
}
