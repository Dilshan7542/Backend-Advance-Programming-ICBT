package lk.icbt.resort.model.service.impl;

import lk.icbt.resort.model.dao.DaoFactory;
import lk.icbt.resort.model.entity.Customer;
import lk.icbt.resort.model.exception.ValidationException;
import lk.icbt.resort.model.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    @Override
    public int add(Customer customer) throws Exception {
        validate(customer);
        return DaoFactory.customerDao().save(customer);
    }

    @Override
    public boolean update(Customer customer) throws Exception {
        if (customer == null || customer.getCustomerId() <= 0) {
            throw new ValidationException("Customer ID is required");
        }
        validate(customer);
        return DaoFactory.customerDao().update(customer);
    }

    @Override
    public boolean delete(int customerId) throws Exception {
        if (customerId <= 0) throw new ValidationException("Customer ID is required");

        // Business rule: customer cannot be deleted if they already have any reservation record.
        if (DaoFactory.reservationDao().existsAnyForCustomer(customerId)) {
            throw new ValidationException("Cannot delete customer. This customer already has reservations.");
        }
        return DaoFactory.customerDao().deleteById(customerId);
    }

    @Override
    public Customer getById(int customerId) throws Exception {
        if (customerId <= 0) throw new ValidationException("Customer ID is required");
        return DaoFactory.customerDao().findById(customerId);
    }

    @Override
    public List<Customer> getAll() throws Exception {
        return DaoFactory.customerDao().findAll();
    }

    @Override
    public List<Customer> search(String keyword) throws Exception {
        return DaoFactory.customerDao().search(keyword);
    }

    @Override
    public List<Customer> getSelectableForReservation() throws Exception {
        return DaoFactory.customerDao().findWithoutReservations();
    }

    private void validate(Customer c) {
        if (c == null) throw new ValidationException("Customer is required");
        if (c.getFullName() == null || c.getFullName().isBlank()) throw new ValidationException("Full name is required");
        if (c.getPhone() == null || c.getPhone().isBlank()) throw new ValidationException("Phone is required");
        // email + nic optional
    }
}
