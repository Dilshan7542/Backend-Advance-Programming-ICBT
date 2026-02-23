package lk.icbt.resort.model.dao;

import lk.icbt.resort.model.entity.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomDao {
    List<Room> findAll() throws Exception;
    List<Room> findAvailable() throws Exception;
    List<Room> findAvailableBetween(LocalDate checkIn, LocalDate checkOut) throws Exception;
    boolean isAvailableBetween(int roomId, LocalDate checkIn, LocalDate checkOut) throws Exception;
    Room findById(int roomId) throws Exception;
}
