package lk.icbt.resort.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.resort.model.dto.BillView;
import lk.icbt.resort.model.service.ServiceFactory;
import net.sf.jasperreports.engine.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/billing/print")
public class BillPrintServlet extends HttpServlet {

    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int reservationId = Integer.parseInt(req.getParameter("id"));
            BillView bill = ServiceFactory.billingService().generateBill(reservationId);

            Map<String, Object> params = new HashMap<>();
            params.put("reservationId", bill.getReservationId());
            params.put("customerName", bill.getCustomerName());
            params.put("roomNo", bill.getRoomNo());
            params.put("roomType", bill.getRoomType());
            params.put("nights", bill.getNights());
            params.put("pricePerNight", bill.getPricePerNight());
            params.put("subTotal", bill.getSubTotal());
            params.put("serviceCharge", bill.getServiceCharge());
            params.put("tax", bill.getTax());
            params.put("total", bill.getTotal());
            params.put("status", bill.getStatus());
            params.put("paidAt", bill.getPaidAt() == null ? "" : bill.getPaidAt().format(TS_FMT));
            params.put("generatedAt", LocalDateTime.now().format(TS_FMT));

            // Load JRXML from classpath (src/main/resources/reports/bill.jrxml)
            try (InputStream jrxml = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("reports/bill.jrxml")) {
                if (jrxml == null) {
                    resp.sendError(500, "Report template not found: reports/bill.jrxml");
                    return;
                }

                JasperReport report = JasperCompileManager.compileReport(jrxml);
                JasperPrint print = JasperFillManager.fillReport(report, params, new JREmptyDataSource(1));
                byte[] pdf = JasperExportManager.exportReportToPdf(print);

                resp.setContentType("application/pdf");
                resp.setHeader("Content-Disposition", "inline; filename=bill_" + reservationId + ".pdf");
                resp.setContentLength(pdf.length);
                resp.getOutputStream().write(pdf);
                resp.getOutputStream().flush();
            }

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            resp.sendError(400, "Invalid reservation id");
        } catch (Exception e) {
            e.printStackTrace();
            // Fail safely back to bill page
            resp.sendRedirect(req.getContextPath() + "/billing?id=" + req.getParameter("id"));
        }
    }
}
