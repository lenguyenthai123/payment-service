package org.example;

import org.example.context.ApplicationContext;
import org.example.entity.*;
import org.example.enums.*;
import org.example.repository.Database;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class BaseTest {

    protected ApplicationContext ctx;
    protected Customer customer;

    @BeforeEach
    void setUp() {
        // reset DB
        Database.BILLS = new ConcurrentHashMap<>();
        Database.TRANSACTIONS = new ConcurrentHashMap<>();
        Database.WALLETS = new ConcurrentHashMap<>();

        customer = new Customer("test@gmail.com", "test");
        Database.DEMO_CUSTOMER = customer;

        Wallet wallet = new Wallet(1_000_000.0);
        Database.WALLETS.put(customer, wallet);

        ctx = new ApplicationContext();

        seedBills();
    }

    protected void seedBills() {
        Bill b1 = new Bill(
                DomainType.ELECTRIC,
                200_000.0,
                new Date(),
                BillState.NOT_PAID,
                "EVN",
                customer.getId(),
                null
        );

        Bill b2 = new Bill(
                DomainType.WATER,
                300_000.0,
                new Date(),
                BillState.NOT_PAID,
                "SAVACO",
                customer.getId(),
                null
        );

        Database.BILLS
                .computeIfAbsent(customer, k -> new ConcurrentHashMap<>())
                .put(b1.getId(), b1);

        Database.BILLS.get(customer)
                .put(b2.getId(), b2);
    }
}
