package org.example.utils;

import org.example.entity.Bill;
import org.example.entity.PaymentTransaction;

import java.util.List;

public final class TablePrinter {

    public static void printBills(List<Bill> bills) {
        System.out.printf("%-5s %-10s %-10s %-12s %-10s %-15s%n",
                "No.", "Type", "Amount", "DueDate", "State", "Provider");

        int i = 1;
        for (Bill b : bills) {
            System.out.printf("%-5d %-10s %-10f %-12s %-10s %-15s%n",
                    i++,
                    b.getType(),
                    b.getAmount(),
                    b.getDueDate().toString(),
                    b.getState(),
                    b.getProvider());
        }
    }

    public static void printPayments(List<PaymentTransaction> txs) {
        System.out.printf("%-5s %-10s %-12s %-10s %-10s%n",
                "No.", "Amount", "PaymentDate", "State", "BillId");

        int i = 1;
        for (PaymentTransaction tx : txs) {
            System.out.printf("%-5d %-10f %-12s %-10s %-10s%n",
                    i++,
                    tx.getAmount(),
                    tx.getCreatedDate().toString(),
                    tx.getState(),
                    tx.getBillId());
        }
    }
}
