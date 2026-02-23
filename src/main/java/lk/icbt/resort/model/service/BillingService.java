package lk.icbt.resort.model.service;

import lk.icbt.resort.model.dto.BillView;

public interface BillingService {
    BillView generateBill(int reservationId) throws Exception;

    boolean updateBillStatus(int reservationId, String status) throws Exception;
}
