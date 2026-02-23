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

            boolean hasDates = checkInStr != null && !checkInStr.isBlank() && checkOutStr != null && !checkOutStr.isBlank();

            List<Room> rooms;
            if (hasDates) {
                LocalDate checkIn = LocalDate.parse(checkInStr);
                LocalDate checkOut = LocalDate.parse(checkOutStr);
                rooms = ServiceFactory.roomService().getAvailableBetween(checkIn, checkOut);
                req.setAttribute("mode", "AVAILABLE");
                req.setAttribute("checkIn", checkInStr);
                req.setAttribute("checkOut", checkOutStr);
            } else {
                rooms = ServiceFactory.roomService().getAll();
                req.setAttribute("mode", "ALL");
            }

            Map<String, Long> counts = rooms.stream().collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));
            req.setAttribute("counts", counts);
            req.setAttribute("rooms", rooms);

            req.getRequestDispatcher("/WEB-INF/views/rooms/list.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to load rooms");
            req.getRequestDispatcher("/WEB-INF/views/rooms/list.jsp").forward(req, resp);
        }
    }
}
