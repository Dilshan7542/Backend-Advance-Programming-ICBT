package lk.icbt.resort.model.dao.impl;

import lk.icbt.resort.model.dao.ReservationActionDao;
import lk.icbt.resort.model.entity.ReservationAction;
import lk.icbt.resort.util.CrudUtil;

public class ReservationActionDaoImpl implements ReservationActionDao {

    @Override
    public void insert(ReservationAction action) throws Exception {
        String sql = "INSERT INTO reservation_actions (reservation_id, user_id, username, role, action, reason) VALUES (?,?,?,?,?,?)";
        CrudUtil.executeUpdate(sql,
                action.getReservationId(),
                action.getUserId(),
                action.getUsername(),
                action.getRole(),
                action.getAction(),
                action.getReason());
    }
}
