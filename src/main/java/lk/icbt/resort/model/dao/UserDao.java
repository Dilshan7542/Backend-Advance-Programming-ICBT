package lk.icbt.resort.model.dao;

import lk.icbt.resort.model.entity.User;

public interface UserDao {
    User findByUsername(String username) throws Exception;
}
