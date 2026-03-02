package lk.icbt.resort.model.dao;

import lk.icbt.resort.model.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {
    int save(Reservation reservation) throws Exception;

    Reservation findByIdWithJoins(int reservationId) throws Exception;

    List<Reservation> findAllWithJoins() throws Exception;


    List<Reservation> findActiveWithJoins(LocalDate today) throws Exception;


    List<Reservation> findHistoryWithJoins(LocalDate today) throws Exception;

    boolean cancelById(int reservationId) throws Exception;


    boolean existsDuplicateConfirmed(int customerId, int roomId, LocalDate checkIn, LocalDate checkOut) throws Exception;


    boolean customerHasOverlapConfirmed(int customerId, LocalDate checkIn, LocalDate checkOut) throws Exception;

    boolean existsAnyForCustomer(int customerId) throws Exception;

    List<Integer> findCustomerIdsWithAnyReservations() throws Exception;

    Reservation findLatestByCustomerWithBillStatus(int customerId) throws Exception;
}
