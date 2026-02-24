package lk.icbt.resort.model.dao.impl;

import lk.icbt.resort.model.dao.RoomDao;
import lk.icbt.resort.model.entity.Room;
import lk.icbt.resort.util.CrudUtil;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomDaoImpl implements RoomDao {

    @Override
    public List<Room> findAll() throws Exception {
        String sql = "SELECT room_id, room_no, room_type, ac_type, price_per_night, status FROM rooms ORDER BY room_no";
        return queryList(sql);
    }

    @Override
    public List<Room> findAvailable() throws Exception {
        String sql = "SELECT room_id, room_no, room_type, ac_type, price_per_night, status FROM rooms WHERE status='AVAILABLE' ORDER BY room_no";
        return queryList(sql);
    }

    @Override
    public List<Room> findAvailableBetween(LocalDate checkIn, LocalDate checkOut) throws Exception {
        // Overlap rule: existing.check_in < new.checkOut AND existing.check_out > new.checkIn
        String sql = """
                SELECT rm.room_id, rm.room_no, rm.room_type, rm.ac_type, rm.price_per_night, rm.status
                FROM rooms rm
                WHERE rm.status='AVAILABLE'
                  AND NOT EXISTS (
                    SELECT 1
                    FROM reservations r
                    WHERE r.room_id = rm.room_id
                      AND r.status='CONFIRMED'
                      AND r.check_in < ?
                      AND r.check_out > ?
                  )
                ORDER BY rm.room_no
                """;

        return queryList(sql, Date.valueOf(checkOut), Date.valueOf(checkIn));
    }

    @Override
    public boolean isAvailableBetween(int roomId, LocalDate checkIn, LocalDate checkOut) throws Exception {
        String sql = """
                SELECT COUNT(*) AS cnt
                FROM rooms rm
                WHERE rm.room_id = ?
                  AND rm.status='AVAILABLE'
                  AND NOT EXISTS (
                    SELECT 1
                    FROM reservations r
                    WHERE r.room_id = rm.room_id
                      AND r.status='CONFIRMED'
                      AND r.check_in < ?
                      AND r.check_out > ?
                  )
                """;

        Integer cnt = CrudUtil.executeQuery(sql, rs -> {
            if (!rs.next()) return 0;
            return rs.getInt("cnt");
        }, roomId, Date.valueOf(checkOut), Date.valueOf(checkIn));

        return cnt != null && cnt > 0;
    }
    @Override
    public List<Room> findUnavailableBetween(LocalDate checkIn, LocalDate checkOut) throws Exception {

        String sql = """
            SELECT rm.room_id, rm.room_no, rm.room_type, rm.ac_type, rm.price_per_night, rm.status
            FROM rooms rm
            WHERE rm.status <> 'AVAILABLE'
               OR EXISTS (
                SELECT 1
                FROM reservations r
                WHERE r.room_id = rm.room_id
                  AND r.status='CONFIRMED'
                  AND r.check_in < ?
                  AND r.check_out > ?
               )
            ORDER BY rm.room_no
            """;

        return queryList(sql, java.sql.Date.valueOf(checkOut), java.sql.Date.valueOf(checkIn));
    }

    @Override
    public Room findById(int roomId) throws Exception {
        String sql = "SELECT room_id, room_no, room_type, ac_type, price_per_night, status FROM rooms WHERE room_id=?";
        return CrudUtil.executeQuery(sql, rs -> {
            if (!rs.next()) return null;
            return new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_no"),
                    rs.getString("room_type"),
                    rs.getString("ac_type"),
                    rs.getBigDecimal("price_per_night"),
                    rs.getString("status")
            );
        }, roomId);
    }

    private List<Room> queryList(String sql, Object... params) throws Exception {
        return CrudUtil.executeQuery(sql, rs -> {
            List<Room> list = new ArrayList<>();
            while (rs.next()) {
                BigDecimal price = rs.getBigDecimal("price_per_night");
                list.add(new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_no"),
                        rs.getString("room_type"),
                        rs.getString("ac_type"),
                        price,
                        rs.getString("status")
                ));
            }
            return list;
        }, params);
    }
}
