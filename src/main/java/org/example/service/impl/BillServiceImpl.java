package org.example.service.impl;

import org.example.entity.Bill;
import org.example.entity.Customer;
import org.example.repository.Database;
import org.example.service.BillService;

import java.time.LocalDate;
import java.util.*;
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
    public List<Bill> searchByProvider(Customer customer, String provider) {
        validateCustomer(customer);
        return Database.BILLS.computeIfAbsent(customer, k -> new ConcurrentHashMap<>()).values().stream().filter(b -> (provider == null) || (Objects.equals(provider, b.getProvider()))).toList();
    }

    @Override
    public Optional<Bill> find(Customer customer, String billId) {
        validateCustomer(customer);
        Map<String, Bill> customerBills = Database.BILLS.computeIfAbsent(customer, k -> new ConcurrentHashMap<>());
        return Optional.ofNullable(customerBills.get(billId));
    }

    @Override
    public List<Bill> listDueBills(Customer customer) {
        Date today = new Date();

        return Database.BILLS
                .getOrDefault(customer, Map.of())
                .values()
                .stream()
                .filter(b -> !b.isPaid())
                .filter(b -> !b.getDueDate().after(today))
                .toList();
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
