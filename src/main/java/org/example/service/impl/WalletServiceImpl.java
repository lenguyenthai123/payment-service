package org.example.service.impl;

import org.example.entity.Customer;
import org.example.entity.Wallet;
import org.example.repository.Database;
import org.example.service.WalletService;

public class WalletServiceImpl implements WalletService {

    @Override
    public Boolean charge(Customer customer, Double amount) {
        if (customer == null) {
            throw new IllegalArgumentException("customer must not be null");
        }

        Wallet wallet = Database.WALLETS.get(customer);
        if (wallet == null) {
            throw new IllegalStateException("Wallet not found for customer " + customer.getId());
        }

        wallet.lock();
        try {
            if (wallet.getBalance().compareTo(amount) < 0) {
                return false; // not enough money
            }

            wallet.setBalance(wallet.getBalance() - amount);
            return true;

        } finally {
            wallet.unlock();
        }
    }
}
