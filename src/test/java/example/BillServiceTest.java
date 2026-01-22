package org.example;

import org.example.entity.Bill;
import org.example.entity.Customer;
import org.example.enums.BillState;
import org.example.enums.DomainType;
import org.example.repository.Database;
import org.example.service.BillService;
import org.example.service.impl.BillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class BillServiceTest {

    private BillService billService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        Database.BILLS = new ConcurrentHashMap<>();

        customer = new Customer("test@gmail.com", "test");
        billService = new BillServiceImpl();
    }

    // ===== create() =====

    @Test
    void should_create_bill_successfully() {
        Bill bill = newBill("EVN", 200_000, daysFromNow(1));

        boolean ok = billService.create(customer, bill);

        assertTrue(ok);

        Map<String, Bill> bills = Database.BILLS.get(customer);
        assertEquals(1, bills.size());
        assertEquals(customer.getId(), bill.getUserId());
    }

    @Test
    @Disabled
    void should_fail_create_when_duplicate_id() {
        Bill bill1 = newBill("EVN", 200_000, daysFromNow(1));
        Bill bill2 = newBill("EVN", 300_000, daysFromNow(2));

        // force same id
//        bill2.setId(bill1.getId());

        assertTrue(billService.create(customer, bill1));
        assertFalse(billService.create(customer, bill2));

        Map<String, Bill> bills = Database.BILLS.get(customer);
        assertEquals(1, bills.size());
    }

    @Test
    void should_throw_when_create_with_null_customer() {
        Bill bill = newBill("EVN", 100_000, daysFromNow(1));

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> billService.create(null, bill)
        );

        assertTrue(ex.getMessage().contains("customer"));
    }

    @Test
    void should_throw_when_create_with_null_bill() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> billService.create(customer, null)
        );

        assertTrue(ex.getMessage().contains("bill"));
    }

    @Test
    void should_throw_when_create_with_negative_amount() {
        Bill bill = newBill("EVN", -100, daysFromNow(1));

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> billService.create(customer, bill)
        );

        assertTrue(ex.getMessage().contains("amount"));
    }

    // ===== delete() =====

    @Test
    void should_delete_bill() {
        Bill bill = newBill("EVN", 200_000, daysFromNow(1));
        billService.create(customer, bill);

        billService.delete(customer, bill.getId());

        Map<String, Bill> bills = Database.BILLS.get(customer);
        assertTrue(bills.isEmpty());
    }

    @Test
    void should_ignore_delete_non_existing_bill() {
        billService.delete(customer, "NOT_EXIST");

        Map<String, Bill> bills = Database.BILLS.get(customer);
        assertTrue(bills.isEmpty());
    }

    // ===== update() =====

    @Test
    void should_update_bill() {
        Bill bill = newBill("EVN", 200_000, daysFromNow(1));
        billService.create(customer, bill);

        bill.setAmount(500_000.0);
        bill.setProvider("VNPT");

        Bill updated = billService.update(customer, bill);

        assertEquals(500_000.0, updated.getAmount());
        assertEquals("VNPT", updated.getProvider());

        Bill stored = Database.BILLS.get(customer).get(bill.getId());
        assertEquals(500_000.0, stored.getAmount());
    }

    // ===== searchByProvider() =====

    @Test
    void should_search_by_provider() {
        Bill b1 = newBill("EVN", 200_000, daysFromNow(1));
        Bill b2 = newBill("VNPT", 300_000, daysFromNow(2));
        Bill b3 = newBill("EVN", 400_000, daysFromNow(3));

        billService.create(customer, b1);
        billService.create(customer, b2);
        billService.create(customer, b3);

        List<Bill> evnBills =
                billService.searchByProvider(customer, "EVN");

        assertEquals(2, evnBills.size());
        assertTrue(evnBills.stream()
                .allMatch(b -> "EVN".equals(b.getProvider())));
    }

    @Test
    void should_return_all_when_provider_null() {
        Bill b1 = newBill("EVN", 200_000, daysFromNow(1));
        Bill b2 = newBill("VNPT", 300_000, daysFromNow(2));

        billService.create(customer, b1);
        billService.create(customer, b2);

        List<Bill> bills =
                billService.searchByProvider(customer, null);

        assertEquals(2, bills.size());
    }

    // ===== find() =====

    @Test
    void should_find_bill() {
        Bill bill = newBill("EVN", 200_000, daysFromNow(1));
        billService.create(customer, bill);

        Optional<Bill> found =
                billService.find(customer, bill.getId());

        assertTrue(found.isPresent());
        assertEquals(bill.getId(), found.get().getId());
    }

    @Test
    void should_return_empty_when_bill_not_found() {
        Optional<Bill> found =
                billService.find(customer, "NOT_EXIST");

        assertTrue(found.isEmpty());
    }

    // ===== listDueBills() =====

    @Test
    void should_list_due_bills() {
        // overdue
        Bill b1 = newBill("EVN", 200_000, daysFromNow(-1));

        // due today
        Bill b2 = newBill("VNPT", 300_000, daysFromNow(0));

        // future
        Bill b3 = newBill("SAVACO", 400_000, daysFromNow(2));

        billService.create(customer, b1);
        billService.create(customer, b2);
        billService.create(customer, b3);

        List<Bill> dueBills =
                billService.listDueBills(customer);

        assertEquals(2, dueBills.size());

        Set<String> ids = dueBills.stream()
                .map(Bill::getId)
                .collect(Collectors.toSet());

        assertTrue(ids.contains(b1.getId()));
        assertTrue(ids.contains(b2.getId()));
        assertFalse(ids.contains(b3.getId()));
    }

    @Test
    void should_not_include_paid_bills_in_due_list() {
        Bill b1 = newBill("EVN", 200_000, daysFromNow(-1));
        b1.setState(BillState.PAID);

        billService.create(customer, b1);

        List<Bill> dueBills =
                billService.listDueBills(customer);

        assertTrue(dueBills.isEmpty());
    }

    // ===== helpers =====

    private Bill newBill(String provider, double amount, Date dueDate) {
        return new Bill(
                DomainType.ELECTRIC,
                amount,
                dueDate,
                BillState.NOT_PAID,
                provider,
                null,
                null
        );
    }

    private Date daysFromNow(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
