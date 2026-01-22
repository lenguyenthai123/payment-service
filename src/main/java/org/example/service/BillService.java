package org.example.service;

import org.example.entity.Bill;
import org.example.entity.Customer;

import java.util.List;

public interface BillService {

    public Boolean create(Customer customer, Bill bill);

    public void delete(Customer customer, String billId);

    public Bill update(Customer customer, Bill bill);

    public List<Bill> search(Customer customer, String searchString);

}
