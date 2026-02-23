package lk.icbt.resort.model.dao.impl;

import lk.icbt.resort.model.dao.CustomerDao;
import lk.icbt.resort.model.entity.Customer;
import lk.icbt.resort.util.CrudUtil;

import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    @Override
    public int save(Customer customer) throws Exception {
        String sql = "INSERT INTO customers (full_name, phone, email, nic) VALUES (?,?,?,?)";
        long id = CrudUtil.executeInsertAndReturnKey(sql,
                customer.getFullName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getNic());
        return (int) id;
    }

    @Override
    public boolean update(Customer customer) throws Exception {
        // Business rule: NIC must not be editable once saved.
        String sql = "UPDATE customers SET full_name=?, phone=?, email=? WHERE customer_id=?";
        int rows = CrudUtil.executeUpdate(sql,
                customer.getFullName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getCustomerId());
        return rows > 0;
    }

    @Override
    public boolean deleteById(int customerId) throws Exception {
        String sql = "DELETE FROM customers WHERE customer_id=?";
        return CrudUtil.executeUpdate(sql, customerId) > 0;
    }

    @Override
    public Customer findById(int customerId) throws Exception {
        String sql = "SELECT customer_id, full_name, phone, email, nic FROM customers WHERE customer_id=?";
        return CrudUtil.executeQuery(sql, rs -> {
            if (!rs.next()) return null;
            return new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("full_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("nic")
            );
        }, customerId);
    }

    @Override
    public List<Customer> findAll() throws Exception {
        String sql = "SELECT customer_id, full_name, phone, email, nic FROM customers ORDER BY customer_id DESC";
        return CrudUtil.executeQuery(sql, rs -> {
            List<Customer> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("nic")
                ));
            }
            return list;
        });
    }

    @Override
    public List<Customer> search(String keyword) throws Exception {
        String k = (keyword == null) ? "" : keyword.trim();
        if (k.isEmpty()) return findAll();

        String like = "%" + k + "%";
        String sql = """
                SELECT customer_id, full_name, phone, email, nic
                FROM customers
                WHERE full_name LIKE ? OR phone LIKE ? OR email LIKE ? OR nic LIKE ?
                ORDER BY customer_id DESC
                """;

        return CrudUtil.executeQuery(sql, rs -> {
            List<Customer> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("nic")
                ));
            }
            return list;
        }, like, like, like, like);
    }

    @Override
    public List<Customer> findWithoutReservations() throws Exception {
        String sql = """
               
                SELECT c.customer_id, c.full_name, c.phone, c.email, c.nic
                       FROM customers c
                       WHERE
                           -- No reservation at all -> OK
                           NOT EXISTS (
                               SELECT 1 FROM reservations r WHERE r.customer_id = c.customer_id
                           )
                           OR
                           -- Has reservations -> only if latest reservation is PAID and checkout passed
                           EXISTS (
                               SELECT 1
                               FROM reservations r
                               LEFT JOIN bills b ON b.reservation_id = r.reservation_id
                               WHERE r.customer_id = c.customer_id
                                 AND r.reservation_id = (
                                     SELECT r2.reservation_id
                                     FROM reservations r2
                                     WHERE r2.customer_id = c.customer_id
                                     ORDER BY r2.check_out DESC, r2.reservation_id DESC
                                     LIMIT 1
                                 )
                                 AND COALESCE(b.status, 'UNPAID') = 'PAID'
                                 AND r.check_out < CURDATE()
                           )
                       ORDER BY c.customer_id DESC
                """;

        return CrudUtil.executeQuery(sql, rs -> {
            List<Customer> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("nic")
                ));
            }
            return list;
        });
    }
}
