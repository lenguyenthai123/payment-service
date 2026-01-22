package org.example.mock;

import org.example.entity.*;
import org.example.enums.*;
import org.example.repository.Database;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockDataInitializer {

    public static void init() {
        Customer customer = Database.DEMO_CUSTOMER;

        // ==== WALLET ====
        Wallet wallet = new Wallet(1_000_000.0); // 1 triệu
        Database.WALLETS.put(customer, wallet);

        // ==== BILLS ====
        Map<String, Bill> bills = new ConcurrentHashMap<>();

        Bill bill1 = new Bill(
                DomainType.ELECTRIC,
                150_000.0,
                daysFromNow(-2),               // quá hạn 2 ngày
                BillState.NOT_PAID,
                "EVN",
                customer.getId(),
                null
        );
//        bill1.setId("BILL-001");

        Bill bill2 = new Bill(
                DomainType.INTERNET,
                300_000.0,
                daysFromNow(3),                // còn hạn 3 ngày
                BillState.NOT_PAID,
                "VNPT",
                customer.getId(),
                null
        );
//        bill2.setId("BILL-002");

        Bill bill3 = new Bill(
                DomainType.WATER,
                120_000.0,
                daysFromNow(-5),
                BillState.PAID,
                "SAWACO",
                customer.getId(),
                null
        );
//        bill3.setId("BILL-003");

        Bill bill4 = new Bill(
                DomainType.INTERNET,
                200_000.0,
                daysFromNow(1),
                BillState.NOT_PAID,
                "VIETTEL",
                customer.getId(),
                daysFromNow(1)                 // đã schedule
        );
//        bill4.setId("BILL-004");

        bills.put(bill1.getId(), bill1);
        bills.put(bill2.getId(), bill2);
        bills.put(bill3.getId(), bill3);
        bills.put(bill4.getId(), bill4);

        Database.BILLS.put(customer, bills);

        // ==== TRANSACTIONS ====
        Map<String, PaymentTransaction> txs = new ConcurrentHashMap<>();

        PaymentTransaction tx1 = new PaymentTransaction(
                120_000.0,
                LocalDate.now().minusDays(5),
                PaymentState.PROCESSED,
                bill3.getId()
        );
//        tx1.setId("TX-001");

        txs.put(tx1.getId(), tx1);

        Database.TRANSACTIONS.put(customer, txs);
    }

    // ===== Helper =====
    private static Date daysFromNow(int days) {
        long millis = System.currentTimeMillis()
                + days * 24L * 60 * 60 * 1000;
        return new Date(millis);
    }
}
