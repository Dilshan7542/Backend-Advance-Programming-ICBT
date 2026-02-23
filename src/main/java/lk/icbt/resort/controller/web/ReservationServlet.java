package lk.icbt.resort.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.resort.model.dao.DaoFactory;
import lk.icbt.resort.model.entity.Customer;
import lk.icbt.resort.model.entity.Reservation;
import lk.icbt.resort.model.entity.Room;
import lk.icbt.resort.model.entity.User;
import lk.icbt.resort.model.exception.ValidationException;
import lk.icbt.resort.model.service.ServiceFactory;
import lk.icbt.resort.util.SecurityUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@WebServlet("/reservations/*")
public class ReservationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        try {
            if (path == null || "/".equals(path)) {
                // tab: all | active | history (default all)
                String tab = req.getParameter("tab");
                if (tab == null || tab.isBlank()) tab = "all";
                tab = tab.trim().toLowerCase();

                List<Reservation> reservations;
                LocalDate today = LocalDate.now();

                switch (tab) {
                    case "active" -> reservations = ServiceFactory.reservationService().getActive(today);
                    case "history" -> reservations = ServiceFactory.reservationService().getHistory(today);
                    default -> reservations = ServiceFactory.reservationService().getAll();
                }

                req.setAttribute("tab", tab);
                req.setAttribute("reservations", reservations);
                req.getRequestDispatcher("/WEB-INF/views/reservations/list.jsp").forward(req, resp);
                return;
            }

            switch (path) {
                case "/add" -> {
                    // IMPORTANT: Do not show customers who already have a reservation
                    List<Customer> customers = ServiceFactory.customerService().getSelectableForReservation();

                    String checkInStr = req.getParameter("checkIn");
                    String checkOutStr = req.getParameter("checkOut");

                    // Default check-in = today
                    if (checkInStr == null || checkInStr.isBlank()) {
                        checkInStr = LocalDate.now().toString();
                    }

                    boolean hasDates = checkOutStr != null && !checkOutStr.isBlank();

                    List<Room> rooms;
                    if (hasDates) {
                        LocalDate checkIn = LocalDate.parse(checkInStr);
                        LocalDate checkOut = LocalDate.parse(checkOutStr);
                        // basic sanity so UI won't show wrong list
                        if (!checkOut.isAfter(checkIn)) {
                            req.setAttribute("error", "Check-out must be after check-in");
                            rooms = Collections.emptyList();
                            hasDates = false;
                        } else {
                            rooms = DaoFactory.roomDao().findAvailableBetween(checkIn, checkOut);
                            req.setAttribute("checkIn", checkInStr);
                            req.setAttribute("checkOut", checkOutStr);
                        }
                    } else {
                        // IMPORTANT FIX: do not show rooms until dates are selected
                        rooms = Collections.emptyList();
                        req.setAttribute("checkIn", checkInStr);
                    }

                    req.setAttribute("hasDates", hasDates);
                    req.setAttribute("customers", customers);
                    req.setAttribute("rooms", rooms);
                    req.getRequestDispatcher("/WEB-INF/views/reservations/add.jsp").forward(req, resp);
                }
                case "/view" -> {
                    int id = Integer.parseInt(req.getParameter("id"));
                    Reservation r = ServiceFactory.reservationService().getById(id);
                    req.setAttribute("reservation", r);
                    req.getRequestDispatcher("/WEB-INF/views/reservations/view.jsp").forward(req, resp);
                }
                default -> resp.sendError(404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Something went wrong.");
            req.getRequestDispatcher("/WEB-INF/views/reservations/list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            resp.sendError(400);
            return;
        }

        try {
            switch (path) {
                case "/add" -> {
                    int customerId = Integer.parseInt(req.getParameter("customerId"));
                    int roomId = Integer.parseInt(req.getParameter("roomId"));
                    LocalDate checkIn = LocalDate.parse(req.getParameter("checkIn"));
                    LocalDate checkOut = LocalDate.parse(req.getParameter("checkOut"));

                    ServiceFactory.reservationService().create(customerId, roomId, checkIn, checkOut);
                    resp.sendRedirect(req.getContextPath() + "/reservations");
                }
                case "/cancel" -> {
                    int id = Integer.parseInt(req.getParameter("id"));
                    User actor = SecurityUtil.currentUser(req);
                    String reason = req.getParameter("reason");
                    ServiceFactory.reservationService().cancel(id, actor, reason);
                    resp.sendRedirect(req.getContextPath() + "/reservations");
                }
                default -> resp.sendError(404);
            }
        } catch (ValidationException ve) {
            ve.printStackTrace();
            req.setAttribute("error", ve.getMessage());
            if ("/add".equals(path)) {
                // Reload add page with dropdowns
                try {
                    req.setAttribute("customers", ServiceFactory.customerService().getSelectableForReservation());

                    String checkInStr = req.getParameter("checkIn");
                    String checkOutStr = req.getParameter("checkOut");

                    if (checkInStr == null || checkInStr.isBlank()) {
                        checkInStr = LocalDate.now().toString();
                    }

                    boolean hasDates = checkOutStr != null && !checkOutStr.isBlank();

                    if (hasDates) {
                        LocalDate checkIn = LocalDate.parse(checkInStr);
                        LocalDate checkOut = LocalDate.parse(checkOutStr);
                        if (checkOut.isAfter(checkIn)) {
                            req.setAttribute("rooms", DaoFactory.roomDao().findAvailableBetween(checkIn, checkOut));
                            req.setAttribute("checkIn", checkInStr);
                            req.setAttribute("checkOut", checkOutStr);
                        } else {
                            hasDates = false;
                            req.setAttribute("rooms", Collections.emptyList());
                            req.setAttribute("checkIn", checkInStr);
                        }
                    } else {
                        req.setAttribute("rooms", Collections.emptyList());
                        req.setAttribute("checkIn", checkInStr);
                    }
                    req.setAttribute("hasDates", hasDates);
                } catch (Exception e) {
                    e.printStackTrace();
                    req.setAttribute("rooms", Collections.emptyList());
                    req.setAttribute("hasDates", false);
                    req.setAttribute("checkIn", LocalDate.now().toString());
                }
                req.getRequestDispatcher("/WEB-INF/views/reservations/add.jsp").forward(req, resp);
            } else if ("/cancel".equals(path)) {
                try {
                    int id = Integer.parseInt(req.getParameter("id"));
                    Reservation r = ServiceFactory.reservationService().getById(id);
                    req.setAttribute("reservation", r);
                } catch (Exception e) { e.printStackTrace(); }
                req.getRequestDispatcher("/WEB-INF/views/reservations/view.jsp").forward(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/reservations");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/reservations");
        }
    }
}
