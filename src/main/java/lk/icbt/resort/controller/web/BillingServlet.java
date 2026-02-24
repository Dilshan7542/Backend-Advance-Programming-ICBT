package lk.icbt.resort.controller.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.resort.model.dto.BillView;
import lk.icbt.resort.model.entity.Reservation;
import lk.icbt.resort.model.exception.ValidationException;
import lk.icbt.resort.model.service.ServiceFactory;

import java.io.IOException;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            BillView bill = ServiceFactory.billingService().generateBill(id);
            Reservation reservation = ServiceFactory.reservationService().getById(bill.getReservationId());
            req.setAttribute("bill", bill);
            req.setAttribute("resvSatus", reservation.getStatus());
            req.getRequestDispatcher("/WEB-INF/views/billing/bill.jsp").forward(req, resp);
        } catch (ValidationException ve) {
            ve.printStackTrace();
            req.setAttribute("error", ve.getMessage());
            resp.sendRedirect(req.getContextPath() + "/reservations");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Unable to generate bill");
            resp.sendRedirect(req.getContextPath() + "/reservations");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String status = req.getParameter("status");
            ServiceFactory.billingService().updateBillStatus(id, status);
            resp.sendRedirect(req.getContextPath() + "/billing?id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/reservations");
        }
    }
}
