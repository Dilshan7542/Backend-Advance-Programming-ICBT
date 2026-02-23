package lk.icbt.resort.model.service.impl;

import lk.icbt.resort.model.dao.DaoFactory;
import lk.icbt.resort.model.entity.Reservation;
import lk.icbt.resort.model.entity.ReservationAction;
import lk.icbt.resort.model.entity.User;
import lk.icbt.resort.model.exception.ValidationException;
import lk.icbt.resort.model.service.ReservationService;
import lk.icbt.resort.util.DateUtil;
import lk.icbt.resort.util.SecurityUtil;

import java.time.LocalDate;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    @Override
    public int create(int customerId, int roomId, LocalDate checkIn, LocalDate checkOut) throws Exception {
        if (customerId <= 0) throw new ValidationException("Customer is required");
        if (roomId <= 0) throw new ValidationException("Room is required");
        if (checkIn == null) throw new ValidationException("Check-in date is required");
        if (checkOut == null) throw new ValidationException("Check-out date is required");
        if (!checkOut.isAfter(checkIn)) throw new ValidationException("Check-out must be after check-in");

        int nights = DateUtil.nights(checkIn, checkOut);
        if (nights <= 0) throw new ValidationException("Nights must be at least 1");

        Reservation last = DaoFactory.reservationDao().findLatestByCustomerWithBillStatus(customerId);

        if (last != null) {
            boolean paid = last.getBillStatus() != null && last.getBillStatus().equalsIgnoreCase("PAID");
            boolean checkoutPassed = last.getCheckOut() != null && last.getCheckOut().isBefore(LocalDate.now());
            // If you want to allow on the same day of checkout, use: !last.getCheckOut().isAfter(LocalDate.now())

            if (!(paid && checkoutPassed)) {
                throw new ValidationException(
                        "Customer already has a reservation. New reservation is allowed only after previous check-out AND only if the previous bill is PAID."
                );
            }
        }
        // 1) Prevent accidental double-submit duplicates
        if (DaoFactory.reservationDao().existsDuplicateConfirmed(customerId, roomId, checkIn, checkOut)) {
            throw new ValidationException("This reservation already exists (duplicate submission)");
        }

        // 2) Prevent same customer having overlapping stays (business rule)
        if (DaoFactory.reservationDao().customerHasOverlapConfirmed(customerId, checkIn, checkOut)) {
            throw new ValidationException("This customer already has another CONFIRMED reservation for the selected dates");
        }

        // 3) Prevent double-booking for the same room/date range
        boolean available = DaoFactory.roomDao().isAvailableBetween(roomId, checkIn, checkOut);
        if (!available) {
            throw new ValidationException("Selected room is not available for the given dates");
        }

        Reservation r = new Reservation();
        r.setCustomerId(customerId);
        r.setRoomId(roomId);
        r.setCheckIn(checkIn);
        r.setCheckOut(checkOut);
        r.setNights(nights);
        r.setStatus("CONFIRMED");

        return DaoFactory.reservationDao().save(r);
    }

    @Override
    public List<Reservation> getAll() throws Exception {
        return DaoFactory.reservationDao().findAllWithJoins();
    }

    @Override
    public List<Reservation> getActive(LocalDate today) throws Exception {
        if (today == null) today = LocalDate.now();
        return DaoFactory.reservationDao().findActiveWithJoins(today);
    }

    @Override
    public List<Reservation> getHistory(LocalDate today) throws Exception {
        if (today == null) today = LocalDate.now();
        return DaoFactory.reservationDao().findHistoryWithJoins(today);
    }

    @Override
    public Reservation getById(int reservationId) throws Exception {
        if (reservationId <= 0) throw new ValidationException("Reservation ID is required");
        return DaoFactory.reservationDao().findByIdWithJoins(reservationId);
    }

    @Override
    public boolean cancel(int reservationId, User actor, String reason) throws Exception {
        if (reservationId <= 0) throw new ValidationException("Reservation ID is required");

        // If this reservation has a PAID bill, only MANAGER can cancel
        var bill = DaoFactory.billDao().findByReservationId(reservationId);
        boolean isPaid = bill != null && bill.getStatus() != null && bill.getStatus().equalsIgnoreCase("PAID");

        if (isPaid) {
            if (actor == null) {
                throw new ValidationException("This reservation is PAID. Only MANAGER can cancel it.");
            }
            if (!SecurityUtil.isManager(actor)) {
                throw new ValidationException("This reservation is PAID. Only MANAGER can cancel it.");
            }
            if (reason == null || reason.trim().isBlank()) {
                throw new ValidationException("Cancellation reason is required for PAID reservations");
            }
        }

        boolean ok = DaoFactory.reservationDao().cancelById(reservationId);

        // Audit log (best practice for preventing misuse)
        if (ok && actor != null) {
            ReservationAction action = new ReservationAction();
            action.setReservationId(reservationId);
            action.setUserId(actor.getUserId());
            action.setUsername(actor.getUsername());
            action.setRole(actor.getRole());
            action.setAction(isPaid ? "CANCEL_PAID_OVERRIDE" : "CANCEL");
            action.setReason(isPaid ? reason.trim() : null);
            try {
                DaoFactory.reservationActionDao().insert(action);
            } catch (Exception e) {
                e.printStackTrace();
                // Do not block main business flow if audit insert fails
            }
        }

        return ok;
    }
}
