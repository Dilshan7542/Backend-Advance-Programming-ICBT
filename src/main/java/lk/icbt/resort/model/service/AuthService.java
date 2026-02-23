package lk.icbt.resort.model.service;

import lk.icbt.resort.model.entity.User;

public interface AuthService {
    User login(String username, String password) throws Exception;
}
