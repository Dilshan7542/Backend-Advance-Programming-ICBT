package lk.icbt.resort.model.service;

import lk.icbt.resort.model.entity.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    List<Room> getAll() throws Exception;
    List<Room> getAvailable() throws Exception;
    List<Room> getAvailableBetween(LocalDate checkIn, LocalDate checkOut) throws Exception;
}
