package lk.icbt.resort.model.dao.impl;

import lk.icbt.resort.model.dao.UserDao;
import lk.icbt.resort.model.entity.User;
import lk.icbt.resort.util.CrudUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class UserDaoImpl implements UserDao {

    @Override
    public User findByUsername(String username) throws Exception {
        String sql = "SELECT user_id, username, password_hash, role, created_at FROM users WHERE username = ?";
        return CrudUtil.executeQuery(sql, rs -> {
            if (!rs.next()) return null;
            int id = rs.getInt("user_id");
            String un = rs.getString("username");
            String pw = rs.getString("password_hash");
            String role = rs.getString("role");
            Timestamp ts = rs.getTimestamp("created_at");
            LocalDateTime created = (ts != null) ? ts.toLocalDateTime() : null;
            return new User(id, un, pw, role, created);
        }, username);
    }
}
