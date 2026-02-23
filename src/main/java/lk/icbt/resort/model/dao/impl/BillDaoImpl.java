package lk.icbt.resort.model.dao.impl;

import lk.icbt.resort.model.dao.BillDao;
import lk.icbt.resort.model.entity.Bill;
import lk.icbt.resort.util.CrudUtil;

import java.sql.Timestamp;

public class BillDaoImpl implements BillDao {

    @Override
    public Bill findByReservationId(int reservationId) throws Exception {
        String sql = "SELECT bill_id, reservation_id, sub_total, service_charge, tax, total, status, paid_at FROM bills WHERE reservation_id=?";
        return CrudUtil.executeQuery(sql, rs -> {
            if (!rs.next()) return null;
            Bill b = new Bill();
            b.setBillId(rs.getInt("bill_id"));
            b.setReservationId(rs.getInt("reservation_id"));
            b.setSubTotal(rs.getBigDecimal("sub_total"));
            b.setServiceCharge(rs.getBigDecimal("service_charge"));
            b.setTax(rs.getBigDecimal("tax"));
            b.setTotal(rs.getBigDecimal("total"));
            b.setStatus(rs.getString("status"));
            Timestamp ts = rs.getTimestamp("paid_at");
            b.setPaidAt(ts != null ? ts.toLocalDateTime() : null);
            return b;
        }, reservationId);
    }

    @Override
    public void upsertTotals(Bill bill) throws Exception {
        // Keep status/paid_at untouched on updates (so payment history is preserved)
        String sql = """
                INSERT INTO bills (reservation_id, sub_total, service_charge, tax, total, status, paid_at)
                VALUES (?,?,?,?,?, 'UNPAID', NULL)
                ON DUPLICATE KEY UPDATE
                    sub_total=VALUES(sub_total),
                    service_charge=VALUES(service_charge),
                    tax=VALUES(tax),
                    total=VALUES(total)
                """;
        CrudUtil.executeUpdate(sql,
                bill.getReservationId(),
                bill.getSubTotal(),
                bill.getServiceCharge(),
                bill.getTax(),
                bill.getTotal());
    }

    @Override
    public boolean updateStatus(int reservationId, String status) throws Exception {
        String normalized = (status == null) ? "UNPAID" : status.trim().toUpperCase();
        if (!"PAID".equals(normalized)) normalized = "UNPAID";

        String sql;
        if ("PAID".equals(normalized)) {
            sql = "UPDATE bills SET status='PAID', paid_at=NOW() WHERE reservation_id=?";
        } else {
            sql = "UPDATE bills SET status='UNPAID', paid_at=NULL WHERE reservation_id=?";
        }
        return CrudUtil.executeUpdate(sql, reservationId) > 0;
    }
}
