package org.example.repository;

import org.example.entity.Bill;
import org.example.entity.Customer;
import org.example.entity.PaymentTransaction;
import org.example.entity.Wallet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    public static Customer DEMO_CUSTOMER = new Customer("ADMIN@gmail.com", "admin");

    public static Map<Customer, Map<String, Bill>> BILLS = new ConcurrentHashMap<>();

    public static Map<Customer, Map<String, PaymentTransaction>> TRANSACTIONS = new ConcurrentHashMap<>();

    public static Map<Customer, Wallet> WALLETS = new ConcurrentHashMap<>();
}
