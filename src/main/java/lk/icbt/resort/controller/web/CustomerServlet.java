package lk.icbt.resort.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.resort.model.dao.DaoFactory;
import lk.icbt.resort.model.entity.Customer;
import lk.icbt.resort.model.entity.User;
import lk.icbt.resort.model.exception.ValidationException;
import lk.icbt.resort.model.service.ServiceFactory;
import lk.icbt.resort.util.SecurityUtil;

import java.io.IOException;
import java.util.*;

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        try {
            if (path == null || "/".equals(path)) {
                String q = req.getParameter("q");
                List<Customer> customers = (q != null && !q.isBlank())
                        ? ServiceFactory.customerService().search(q)
                        : ServiceFactory.customerService().getAll();
                req.setAttribute("customers", customers);
                req.setAttribute("q", q);

                // Used to disable delete button when customer has reservations
                Set<Integer> withReservations = new HashSet<>(DaoFactory.reservationDao().findCustomerIdsWithAnyReservations());
                Map<Integer, Boolean> hasReservationMap = new HashMap<>();
                for (Customer c : customers) {
                    hasReservationMap.put(c.getCustomerId(), withReservations.contains(c.getCustomerId()));
                }
                req.setAttribute("hasReservationMap", hasReservationMap);

                req.getRequestDispatcher("/WEB-INF/views/customers/list.jsp").forward(req, resp);
                return;
            }

            switch (path) {
                case "/add" -> req.getRequestDispatcher("/WEB-INF/views/customers/add.jsp").forward(req, resp);
                case "/edit" -> {
                    User user = SecurityUtil.currentUser(req);
                    if (!SecurityUtil.isManager(user)) {
                        req.setAttribute("error", "Only MANAGER can update customer details.");
                        // reload list
                        List<Customer> customers = ServiceFactory.customerService().getAll();
                        req.setAttribute("customers", customers);
                        Set<Integer> withReservations = new HashSet<>(DaoFactory.reservationDao().findCustomerIdsWithAnyReservations());
                        Map<Integer, Boolean> hasReservationMap = new HashMap<>();
                        for (Customer c : customers) {
                            hasReservationMap.put(c.getCustomerId(), withReservations.contains(c.getCustomerId()));
                        }
                        req.setAttribute("hasReservationMap", hasReservationMap);
                        req.getRequestDispatcher("/WEB-INF/views/customers/list.jsp").forward(req, resp);
                        return;
                    }

                    int id = Integer.parseInt(req.getParameter("id"));
                    Customer c = ServiceFactory.customerService().getById(id);
                    req.setAttribute("customer", c);
                    req.getRequestDispatcher("/WEB-INF/views/customers/edit.jsp").forward(req, resp);
                }
                default -> resp.sendError(404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Something went wrong.");
            req.getRequestDispatcher("/WEB-INF/views/customers/list.jsp").forward(req, resp);
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
                    Customer c = new Customer();
                    c.setFullName(req.getParameter("fullName"));
                    c.setPhone(req.getParameter("phone"));
                    c.setEmail(req.getParameter("email"));
                    c.setNic(req.getParameter("nic"));
                    ServiceFactory.customerService().add(c);
                    resp.sendRedirect(req.getContextPath() + "/customers");
                }
                case "/edit" -> {
                    User user = SecurityUtil.currentUser(req);
                    if (!SecurityUtil.isManager(user)) {
                        req.setAttribute("error", "Only MANAGER can update customer details.");
                        List<Customer> customers = ServiceFactory.customerService().getAll();
                        req.setAttribute("customers", customers);
                        Set<Integer> withReservations = new HashSet<>(DaoFactory.reservationDao().findCustomerIdsWithAnyReservations());
                        Map<Integer, Boolean> hasReservationMap = new HashMap<>();
                        for (Customer c : customers) {
                            hasReservationMap.put(c.getCustomerId(), withReservations.contains(c.getCustomerId()));
                        }
                        req.setAttribute("hasReservationMap", hasReservationMap);
                        req.getRequestDispatcher("/WEB-INF/views/customers/list.jsp").forward(req, resp);
                        return;
                    }

                    Customer c = new Customer();
                    c.setCustomerId(Integer.parseInt(req.getParameter("customerId")));
                    c.setFullName(req.getParameter("fullName"));
                    c.setPhone(req.getParameter("phone"));
                    c.setEmail(req.getParameter("email"));
                    // NIC is not editable (ignore any submitted value)
                    ServiceFactory.customerService().update(c);
                    resp.sendRedirect(req.getContextPath() + "/customers");
                }
                case "/delete" -> {
                    User user = SecurityUtil.currentUser(req);
                    if (!SecurityUtil.isManager(user)) {
                        req.setAttribute("error", "Only MANAGER can delete customers.");
                        List<Customer> customers = ServiceFactory.customerService().getAll();
                        req.setAttribute("customers", customers);
                        Set<Integer> withReservations = new HashSet<>(DaoFactory.reservationDao().findCustomerIdsWithAnyReservations());
                        Map<Integer, Boolean> hasReservationMap = new HashMap<>();
                        for (Customer c : customers) {
                            hasReservationMap.put(c.getCustomerId(), withReservations.contains(c.getCustomerId()));
                        }
                        req.setAttribute("hasReservationMap", hasReservationMap);
                        req.getRequestDispatcher("/WEB-INF/views/customers/list.jsp").forward(req, resp);
                        return;
                    }

                    int id = Integer.parseInt(req.getParameter("id"));
                    ServiceFactory.customerService().delete(id);
                    resp.sendRedirect(req.getContextPath() + "/customers");
                }
                default -> resp.sendError(404);
            }
        } catch (ValidationException ve) {
            ve.printStackTrace();
            req.setAttribute("error", ve.getMessage());
            // return to same page
            if ("/add".equals(path)) {
                req.getRequestDispatcher("/WEB-INF/views/customers/add.jsp").forward(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/customers");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Something went wrong.");
            resp.sendRedirect(req.getContextPath() + "/customers");
        }
    }
}
