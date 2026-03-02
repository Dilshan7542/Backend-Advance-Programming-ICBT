package lk.icbt.resort.model.service;

import lk.icbt.resort.model.entity.Customer;

import java.util.List;

public interface CustomerService {
    int add(Customer customer) throws Exception;
    boolean update(Customer customer) throws Exception;
    boolean delete(int customerId) throws Exception;
    Customer getById(int customerId) throws Exception;
    List<Customer> getAll() throws Exception;
    List<Customer> search(String keyword) throws Exception;

    List<Customer> getSelectableForReservation() throws Exception;
}
