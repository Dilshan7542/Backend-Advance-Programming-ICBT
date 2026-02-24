package lk.icbt.resort.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.resort.model.entity.Room;
import lk.icbt.resort.model.service.ServiceFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/rooms")
public class RoomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String checkInStr = req.getParameter("checkIn");
            String checkOutStr = req.getParameter("checkOut");

            // UI tab (available | unavailable)
            String tab = req.getParameter("tab");
            if (tab == null || tab.isBlank()) tab = "available";
            tab = tab.trim().toLowerCase();
            if (!tab.equals("available") && !tab.equals("unavailable")) tab = "available";
            req.setAttribute("tab", tab);

            boolean hasDates = checkInStr != null && !checkInStr.isBlank()
                    && checkOutStr != null && !checkOutStr.isBlank();

            List<Room> availableRooms;
            List<Room> unavailableRooms;

            if (hasDates) {
                LocalDate checkIn = LocalDate.parse(checkInStr);
                LocalDate checkOut = LocalDate.parse(checkOutStr);

                availableRooms = ServiceFactory.roomService().getAvailableBetween(checkIn, checkOut);
                unavailableRooms = ServiceFactory.roomService().getUnavailableBetween(checkIn, checkOut);

                req.setAttribute("mode", "DATES");
                req.setAttribute("checkIn", checkInStr);
                req.setAttribute("checkOut", checkOutStr);
            } else {
                List<Room> all = ServiceFactory.roomService().getAll();
                availableRooms = all.stream()
                        .filter(r -> "AVAILABLE".equalsIgnoreCase(r.getStatus()))
                        .toList();
                unavailableRooms = all.stream()
                        .filter(r -> !"AVAILABLE".equalsIgnoreCase(r.getStatus()))
                        .toList();

                req.setAttribute("mode", "ALL");
            }

            Map<String, Long> countsAvailable = availableRooms.stream()
                    .collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));
            Map<String, Long> countsUnavailable = unavailableRooms.stream()
                    .collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));

            req.setAttribute("availableRooms", availableRooms);
            req.setAttribute("unavailableRooms", unavailableRooms);
            req.setAttribute("countsAvailable", countsAvailable);
            req.setAttribute("countsUnavailable", countsUnavailable);

            req.getRequestDispatcher("/WEB-INF/views/rooms/list.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load rooms");
            req.getRequestDispatcher("/WEB-INF/views/rooms/list.jsp").forward(req, resp);
        }
    }
}