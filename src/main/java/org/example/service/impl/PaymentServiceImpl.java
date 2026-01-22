package org.example.service.impl;

import org.example.entity.Bill;
import org.example.entity.Customer;
import org.example.entity.PaymentTransaction;
import org.example.enums.BillState;
import org.example.enums.PaymentState;
import org.example.repository.Database;
import org.example.service.BillService;
import org.example.service.PaymentService;
import org.example.service.WalletService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PaymentServiceImpl implements PaymentService {

    private final BillService billService;
    private final WalletService walletService;

    public PaymentServiceImpl(BillService billService, WalletService walletService) {
        this.billService = Objects.requireNonNull(billService);
        this.walletService = Objects.requireNonNull(walletService);
    }

    @Override
    public Boolean payBill(Customer customer, String billId) {
        Objects.requireNonNull(billId, "billId must not be null");

        Bill bill = billService.find(customer, billId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Not found a bill with id " + billId));

        if (bill.getState() == BillState.PAID) {
            throw new IllegalStateException("Bill " + billId + " is already paid");
        }

        walletService.charge(customer, bill.getAmount());
        bill.setState(BillState.PAID);

        PaymentTransaction transaction = new PaymentTransaction(
                bill.getAmount(),
                LocalDate.now(),
                PaymentState.PROCESSED,
                bill.getId()
        );
        saveTransaction(customer, transaction);
        return true;
    }

    @Override
    public Boolean payBills(Customer customer, List<String> ids) {
        if (customer == null) {
            throw new IllegalArgumentException("customer must not be null");
        }
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("bill ids must not be empty");
        }

        List<Bill> billsToPay = ids.stream()
                .map(id -> billService.find(customer, id)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Not found a bill with id " + id)))
                .filter(bill -> bill.getState() == BillState.NOT_PAID)
                .sorted(Comparator.comparing(Bill::getDueDate))
                .toList();

        Double totalAmount = billsToPay.stream()
                .mapToDouble(Bill::getAmount)
                .sum();

        boolean charged = walletService.charge(customer, totalAmount);
        if (!charged) {
            return false;
        }

        for (Bill bill : billsToPay) {
            bill.setState(BillState.PAID);
            PaymentTransaction transaction = new PaymentTransaction(
                    bill.getAmount(),
                    LocalDate.now(),
                    PaymentState.PROCESSED,
                    bill.getId()
            );
            saveTransaction(customer, transaction);
        }

        return true;
    }

    @Override
    public List<PaymentTransaction> listPayments(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer must not be null");
        }
        Map<String, PaymentTransaction> transactions = Database.TRANSACTIONS.computeIfAbsent(customer, k -> new ConcurrentHashMap<>());
        return transactions.values().stream().toList();
    }

    private void saveTransaction(Customer customer, PaymentTransaction transaction) {
        Database.TRANSACTIONS.computeIfAbsent(customer, k -> new ConcurrentHashMap<>()).computeIfAbsent(transaction.getId(), k -> transaction);
    }
}
