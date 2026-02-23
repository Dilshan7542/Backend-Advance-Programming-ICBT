package lk.icbt.resort.model.dao;

import lk.icbt.resort.model.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {
    int save(Reservation reservation) throws Exception;

    Reservation findByIdWithJoins(int reservationId) throws Exception;

    List<Reservation> findAllWithJoins() throws Exception;

    /**
     * Active = CONFIRMED and check_out >= today.
     */
    List<Reservation> findActiveWithJoins(LocalDate today) throws Exception;

    /**
     * History = CANCELLED OR check_out < today.
     */
    List<Reservation> findHistoryWithJoins(LocalDate today) throws Exception;

    boolean cancelById(int reservationId) throws Exception;

    /** Prevent exact duplicate submissions (same customer+room+dates) */
    boolean existsDuplicateConfirmed(int customerId, int roomId, LocalDate checkIn, LocalDate checkOut) throws Exception;

    /** Prevent same customer holding overlapping stays */
    boolean customerHasOverlapConfirmed(int customerId, LocalDate checkIn, LocalDate checkOut) throws Exception;

    /** For UI rules: disable customer delete and filter customer dropdowns */
    boolean existsAnyForCustomer(int customerId) throws Exception;

    /** For UI: build a fast lookup map in the customers list */
    List<Integer> findCustomerIdsWithAnyReservations() throws Exception;

    Reservation findLatestByCustomerWithBillStatus(int customerId) throws Exception;
}
