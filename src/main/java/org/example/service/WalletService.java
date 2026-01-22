package org.example.service;

import org.example.entity.Customer;

public interface WalletService {
    public Boolean charge(Customer customer, Double amount);
}
