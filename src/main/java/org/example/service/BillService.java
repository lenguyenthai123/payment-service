package org.example.service;

import org.example.entity.Bill;
import org.example.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface BillService {

    public Boolean create(Customer customer, Bill bill);

    public void delete(Customer customer, String billId);

    public Bill update(Customer customer, Bill bill);

    public List<Bill> listDueBills(Customer customer);

    public List<Bill> searchByProvider(Customer customer, String provider);

    public Optional<Bill> find(Customer customer, String billId);
}
