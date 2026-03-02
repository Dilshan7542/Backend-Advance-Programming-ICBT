package lk.icbt.resort.model.service;

import lk.icbt.resort.model.entity.Reservation;
import lk.icbt.resort.model.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    int create(int customerId, int roomId, LocalDate checkIn, LocalDate checkOut) throws Exception;

    List<Reservation> getAll() throws Exception;

    List<Reservation> getActive(LocalDate today) throws Exception;

    List<Reservation> getHistory(LocalDate today) throws Exception;

    Reservation getById(int reservationId) throws Exception;

    boolean cancel(int reservationId, User actor, String reason) throws Exception;
}
