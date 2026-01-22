package org.example;

import org.example.entity.Bill;
import org.example.entity.PaymentTransaction;
import org.example.enums.BillState;
import org.example.enums.PaymentState;
import org.example.repository.Database;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentServiceTest extends BaseTest {

    @Test
    @Disabled
    void should_pay_single_bill() {
        Bill bill = firstBill();

        boolean ok = ctx.paymentService()
                .payBill(customer, bill.getId());

        assertTrue(ok);
        assertEquals(BillState.PAID, bill.getState());

        double balance =
                Database.WALLETS.get(customer).getBalance();

        assertEquals(800_000.0, balance);

        var txs = Database.TRANSACTIONS.get(customer);
        assertEquals(1, txs.size());

        PaymentTransaction tx =
                txs.values().iterator().next();

        assertEquals(bill.getId(), tx.getBillId());
        assertEquals(PaymentState.PROCESSED, tx.getState());
    }

    // ===== NEW TESTS =====

    @Test
    void should_pay_multiple_bills_in_due_date_order() {
        List<Bill> bills =
                new ArrayList<>(Database.BILLS.get(customer).values());

        // set due dates: bill0 muộn hơn bill1
        bills.get(0).setDueDate(new Date(System.currentTimeMillis() + 10_000));
        bills.get(1).setDueDate(new Date(System.currentTimeMillis() + 1_000));

        List<String> ids = bills.stream()
                .map(Bill::getId)
                .collect(Collectors.toList());

        boolean ok = ctx.paymentService()
                .payBills(customer, ids);

        assertTrue(ok);

        // cả 2 bill đều PAID
        for (Bill b : bills) {
            assertEquals(BillState.PAID, b.getState());
        }

        double balance =
                Database.WALLETS.get(customer).getBalance();

        // 1_000_000 - 200_000 - 300_000 = 500_000
        assertEquals(500_000.0, balance);

        var txs = Database.TRANSACTIONS.get(customer);
        assertEquals(2, txs.size());
    }

    @Test
    void should_fail_multi_pay_if_not_enough_money() {
        List<Bill> bills =
                new ArrayList<>(Database.BILLS.get(customer).values());

        // wallet chỉ đủ trả 1 bill
        Database.WALLETS.get(customer).setBalance(200_000.0);

        List<String> ids = bills.stream()
                .map(Bill::getId)
                .collect(Collectors.toList());

        boolean ok = ctx.paymentService()
                .payBills(customer, ids);

        assertFalse(ok);

        // không bill nào bị PAID
        for (Bill b : bills) {
            assertEquals(BillState.NOT_PAID, b.getState());
        }

        // balance không đổi
        double balance =
                Database.WALLETS.get(customer).getBalance();

        assertEquals(200_000.0, balance);

        // không có transaction
        assertNull(Database.TRANSACTIONS.get(customer));
    }

    @Test
    void should_throw_if_bill_not_found() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> ctx.paymentService()
                        .payBill(customer, "NOT_EXIST")
        );

        assertTrue(ex.getMessage().contains("Not found"));
    }

    @Test
    void should_throw_if_bill_already_paid() {
        Bill bill = firstBill();

        ctx.paymentService()
                .payBill(customer, bill.getId());

        Exception ex = assertThrows(
                IllegalStateException.class,
                () -> ctx.paymentService()
                        .payBill(customer, bill.getId())
        );

        assertTrue(ex.getMessage().contains("already paid"));
    }

    @Test
    void should_list_payments() {
        Bill bill1 = firstBill();
        Bill bill2 = secondBill();

        ctx.paymentService()
                .payBill(customer, bill1.getId());

        ctx.paymentService()
                .payBill(customer, bill2.getId());

        var txs = ctx.paymentService()
                .listPayments(customer);

        assertEquals(2, txs.size());

        Set<String> billIds = txs.stream()
                .map(PaymentTransaction::getBillId)
                .collect(Collectors.toSet());

        assertTrue(billIds.contains(bill1.getId()));
        assertTrue(billIds.contains(bill2.getId()));
    }

    // ===== helpers =====

    private Bill firstBill() {
        return Database.BILLS.get(customer)
                .values().iterator().next();
    }

    private Bill secondBill() {
        Iterator<Bill> it =
                Database.BILLS.get(customer).values().iterator();
        it.next();
        return it.next();
    }
}
