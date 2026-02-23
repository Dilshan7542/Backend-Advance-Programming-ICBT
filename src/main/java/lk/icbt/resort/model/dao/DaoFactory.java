package lk.icbt.resort.model.dao;

import lk.icbt.resort.model.dao.impl.*;

public final class DaoFactory {
    private DaoFactory() {}

    private static final UserDao USER_DAO = new UserDaoImpl();
    private static final CustomerDao CUSTOMER_DAO = new CustomerDaoImpl();
    private static final RoomDao ROOM_DAO = new RoomDaoImpl();
    private static final ReservationDao RESERVATION_DAO = new ReservationDaoImpl();
    private static final BillDao BILL_DAO = new BillDaoImpl();
    private static final ReservationActionDao RESERVATION_ACTION_DAO = new ReservationActionDaoImpl();

    public static UserDao userDao() { return USER_DAO; }
    public static CustomerDao customerDao() { return CUSTOMER_DAO; }
    public static RoomDao roomDao() { return ROOM_DAO; }
    public static ReservationDao reservationDao() { return RESERVATION_DAO; }
    public static BillDao billDao() { return BILL_DAO; }
    public static ReservationActionDao reservationActionDao() { return RESERVATION_ACTION_DAO; }
}
