package org.example.service;

import org.example.entity.Customer;
import org.example.entity.PaymentTransaction;

import java.util.List;

public interface PaymentService {
    public Boolean payBill(Customer customer, String billId);

    public Boolean payBills(Customer customer, List<String> ids);

    public List<PaymentTransaction> listPayments(Customer customer);
}
