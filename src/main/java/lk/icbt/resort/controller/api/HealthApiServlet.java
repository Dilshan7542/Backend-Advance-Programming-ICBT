package lk.icbt.resort.controller.api;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.icbt.resort.util.AppConfig;
import lk.icbt.resort.util.JsonUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/health")
public class HealthApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("status", "OK");
        payload.put("app", AppConfig.getOrDefault("app.name", "Ocean View Resort"));
        payload.put("time", LocalDateTime.now().toString());

        resp.getWriter().write(JsonUtil.toJson(payload));
    }
}
