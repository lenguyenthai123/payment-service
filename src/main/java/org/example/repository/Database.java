package org.example.repository;

import org.example.entity.Bill;
import org.example.entity.Customer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    // Customer -> Map <BillId, Bill>
    public static Map<Customer, Map<String, Bill>> BILLS = new ConcurrentHashMap<>();
}
