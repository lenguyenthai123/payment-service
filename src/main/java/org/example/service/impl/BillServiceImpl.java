package org.example.service.impl;

import org.example.entity.Bill;
import org.example.entity.Customer;
import org.example.repository.Database;
import org.example.service.BillService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BillServiceImpl implements BillService {

    @Override
    public Boolean create(Customer customer, Bill bill) {
        validateCustomer(customer);
        validateBill(bill);

        Map<String, Bill> customerBills = Database.BILLS.computeIfAbsent(customer, k -> new ConcurrentHashMap<>());

        if (customerBills.containsKey(bill.getId())) {
            return false; // bill id already exists for this customer
        }

        bill.setUserId(customer.getId());
        customerBills.put(bill.getId(), bill);

        return true;
    }

    @Override
    public void delete(Customer customer, String billId) {
        validateCustomer(customer);

        Map<String, Bill> customerBills = Database.BILLS.computeIfAbsent(customer, k -> new ConcurrentHashMap<>());

        customerBills.remove(billId);
    }

    @Override
    public Bill update(Customer customer, Bill bill) {
        validateCustomer(customer);
        validateBill(bill);

        Map<String, Bill> customerBills = Database.BILLS.computeIfAbsent(customer, k -> new ConcurrentHashMap<>());
        bill.setUserId(customer.getId());
        customerBills.put(bill.getId(), bill);
        return bill;
    }

    @Override
    public List<Bill> search(Customer customer, String searchString) {
        validateCustomer(customer);
        return Database.BILLS.computeIfAbsent(customer, k -> new ConcurrentHashMap<>()).values().stream().filter(b -> (searchString == null) || (Objects.equals(searchString, b.getProviderId()))).toList();
    }

    private void validateBill(Bill bill) {
        if (bill == null) {
            throw new IllegalArgumentException("bill must not be null");
        }
        if (bill.getAmount() < 0) {
            throw new IllegalArgumentException("amount must be >= 0");
        }
    }

    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("customer must not be null");
        }
    }

}
