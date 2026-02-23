package lk.icbt.resort.model.dao;

import lk.icbt.resort.model.entity.Customer;

import java.util.List;

public interface CustomerDao {
    int save(Customer customer) throws Exception;
    boolean update(Customer customer) throws Exception;
    boolean deleteById(int customerId) throws Exception;
    Customer findById(int customerId) throws Exception;
    List<Customer> findAll() throws Exception;
    List<Customer> search(String keyword) throws Exception;

    /** Customers who have not yet been used in a reservation (avoid multiple reservations per customer record) */
    List<Customer> findWithoutReservations() throws Exception;
}
