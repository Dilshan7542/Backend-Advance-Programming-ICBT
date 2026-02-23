package lk.icbt.resort.model.service;

import lk.icbt.resort.model.service.impl.*;

public final class ServiceFactory {
    private ServiceFactory() {}

    private static final AuthService AUTH_SERVICE = new AuthServiceImpl();
    private static final CustomerService CUSTOMER_SERVICE = new CustomerServiceImpl();
    private static final ReservationService RESERVATION_SERVICE = new ReservationServiceImpl();
    private static final BillingService BILLING_SERVICE = new BillingServiceImpl();
    private static final RoomService ROOM_SERVICE = new RoomServiceImpl();

    public static AuthService authService() { return AUTH_SERVICE; }
    public static CustomerService customerService() { return CUSTOMER_SERVICE; }
    public static ReservationService reservationService() { return RESERVATION_SERVICE; }
    public static BillingService billingService() { return BILLING_SERVICE; }
    public static RoomService roomService() { return ROOM_SERVICE; }
}
