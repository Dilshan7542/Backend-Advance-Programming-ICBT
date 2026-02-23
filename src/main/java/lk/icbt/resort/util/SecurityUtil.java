package lk.icbt.resort.util;

import jakarta.servlet.http.HttpServletRequest;
import lk.icbt.resort.model.entity.User;

public final class SecurityUtil {
    private SecurityUtil() {}

    public static User currentUser(HttpServletRequest req) {
        if (req == null) return null;
        if (req.getSession(false) == null) return null;
        Object obj = req.getSession(false).getAttribute(AppConstants.SESSION_USER);
        return (obj instanceof User) ? (User) obj : null;
    }

    public static boolean isManager(User user) {
        if (user == null || user.getRole() == null) return false;
        String role = user.getRole().trim().toUpperCase();
        // Treat ADMIN as manager-equivalent as well
        return "MANAGER".equals(role) || "ADMIN".equals(role);
    }
}
