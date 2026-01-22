package org.example.repository;

import org.example.entity.Bill;
import org.example.entity.Customer;
import org.example.entity.PaymentTransaction;
import org.example.entity.Wallet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    // Customer -> Map <BillId, Bill>
    public static Map<Customer, Map<String, Bill>> BILLS = new ConcurrentHashMap<>();

    public static Map<Customer, Map<String, PaymentTransaction>> TRANSACTIONS = new ConcurrentHashMap<>();

    public static Map<Customer, Wallet> WALLETS = new ConcurrentHashMap<>();
}
