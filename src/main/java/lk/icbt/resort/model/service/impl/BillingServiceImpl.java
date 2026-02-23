package lk.icbt.resort.model.service.impl;

import lk.icbt.resort.model.dao.DaoFactory;
import lk.icbt.resort.model.dto.BillView;
import lk.icbt.resort.model.entity.Bill;
import lk.icbt.resort.model.entity.Reservation;
import lk.icbt.resort.model.entity.Room;
import lk.icbt.resort.model.exception.ValidationException;
import lk.icbt.resort.model.service.BillingService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BillingServiceImpl implements BillingService {

    @Override
    public BillView generateBill(int reservationId) throws Exception {
        if (reservationId <= 0) throw new ValidationException("Reservation ID is required");

        Reservation r = DaoFactory.reservationDao().findByIdWithJoins(reservationId);
        if (r == null) throw new ValidationException("Reservation not found");

        Room room = DaoFactory.roomDao().findById(r.getRoomId());
        if (room == null) throw new ValidationException("Room not found");

        BigDecimal nights = BigDecimal.valueOf(r.getNights());
        BigDecimal rate = room.getPricePerNight();
        BigDecimal subTotal = rate.multiply(nights);

        // Simple demo charges (change if your assignment has specific values)
        BigDecimal serviceCharge = subTotal.multiply(BigDecimal.valueOf(0.10));
        BigDecimal tax = subTotal.multiply(BigDecimal.valueOf(0.02));
        BigDecimal total = subTotal.add(serviceCharge).add(tax);

        // Persist bill totals + keep payment status in DB
        Bill dbBill = new Bill();
        dbBill.setReservationId(r.getReservationId());
        dbBill.setSubTotal(scale(subTotal));
        dbBill.setServiceCharge(scale(serviceCharge));
        dbBill.setTax(scale(tax));
        dbBill.setTotal(scale(total));
        DaoFactory.billDao().upsertTotals(dbBill);

        Bill stored = DaoFactory.billDao().findByReservationId(r.getReservationId());

        BillView bill = new BillView();
        bill.setReservationId(r.getReservationId());
        bill.setCustomerName(r.getCustomerName());
        bill.setRoomNo(room.getRoomNo());
        bill.setRoomType(room.getRoomType());
        bill.setNights(r.getNights());
        bill.setPricePerNight(rate);

        bill.setSubTotal(scale(subTotal));
        bill.setServiceCharge(scale(serviceCharge));
        bill.setTax(scale(tax));
        bill.setTotal(scale(total));

        if (stored != null) {
            bill.setStatus(stored.getStatus());
            bill.setPaidAt(stored.getPaidAt());
        } else {
            bill.setStatus("UNPAID");
            bill.setPaidAt(null);
        }

        return bill;
    }

    @Override
    public boolean updateBillStatus(int reservationId, String status) throws Exception {
        if (reservationId <= 0) throw new ValidationException("Reservation ID is required");

        // Ensure bill exists in DB
        generateBill(reservationId);
        return DaoFactory.billDao().updateStatus(reservationId, status);
    }

    private BigDecimal scale(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP);
    }
}
