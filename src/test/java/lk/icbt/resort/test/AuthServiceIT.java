package lk.icbt.resort.test;

import lk.icbt.resort.model.entity.User;
import lk.icbt.resort.model.exception.ValidationException;
import lk.icbt.resort.model.service.ServiceFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceIT {

    @Test
    void login_withValidAdminCredentials_shouldReturnUser() throws Exception {
        TestDbSupport.assumeDbUp();

        // schema.sql seeds: username=admin, password_hash=admin123
        User user = ServiceFactory.authService().login("admin", "admin123");

        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertNotNull(user.getRole());
    }

    @Test
    void login_withInvalidPassword_shouldThrowValidationException() {
        TestDbSupport.assumeDbUp();

        assertThrows(ValidationException.class, () ->
                ServiceFactory.authService().login("admin", "wrongPassword")
        );
    }
}