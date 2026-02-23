package lk.icbt.resort.model.service.impl;

import lk.icbt.resort.model.dao.DaoFactory;
import lk.icbt.resort.model.entity.User;
import lk.icbt.resort.model.exception.ValidationException;
import lk.icbt.resort.model.service.AuthService;
import lk.icbt.resort.util.PasswordUtil;

public class AuthServiceImpl implements AuthService {

    @Override
    public User login(String username, String password) throws Exception {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException("Password is required");
        }

        User user = DaoFactory.userDao().findByUsername(username.trim());
        if (user == null) {
            throw new ValidationException("Invalid username or password");
        }

        if (!PasswordUtil.matches(password, user.getPasswordHash())) {
            throw new ValidationException("Invalid username or password");
        }

        return user;
    }
}
