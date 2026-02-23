package lk.icbt.resort.model.dao;

import lk.icbt.resort.model.entity.Bill;

public interface BillDao {
    Bill findByReservationId(int reservationId) throws Exception;
    void upsertTotals(Bill bill) throws Exception;
    boolean updateStatus(int reservationId, String status) throws Exception;
}
