package lk.icbt.resort.model.service.impl;

import lk.icbt.resort.model.dao.DaoFactory;
import lk.icbt.resort.model.entity.Room;
import lk.icbt.resort.model.exception.ValidationException;
import lk.icbt.resort.model.service.RoomService;

import java.time.LocalDate;
import java.util.List;

public class RoomServiceImpl implements RoomService {

    @Override
    public List<Room> getAll() throws Exception {
        return DaoFactory.roomDao().findAll();
    }

    @Override
    public List<Room> getAvailable() throws Exception {
        return DaoFactory.roomDao().findAvailable();
    }

    @Override
    public List<Room> getAvailableBetween(LocalDate checkIn, LocalDate checkOut) throws Exception {
        if (checkIn == null || checkOut == null) {
            throw new ValidationException("Check-in and check-out dates are required");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new ValidationException("Check-out must be after check-in");
        }
        return DaoFactory.roomDao().findAvailableBetween(checkIn, checkOut);
    }
    @Override
    public List<Room> getUnavailableBetween(LocalDate checkIn, LocalDate checkOut) throws Exception {
        if (checkIn == null || checkOut == null) {
            throw new ValidationException("Check-in and check-out dates are required");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new ValidationException("Check-out must be after check-in");
        }
        return DaoFactory.roomDao().findUnavailableBetween(checkIn, checkOut);
    }
}
