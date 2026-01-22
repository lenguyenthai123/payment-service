package org.example.cronjob;

import org.example.entity.Bill;
import org.example.entity.Customer;
import org.example.enums.BillState;
import org.example.repository.Database;
import org.example.service.PaymentService;


import java.util.Date;
import java.util.Map;

public class ScheduledPaymentEngine implements Runnable {

    private final PaymentService paymentService;
    private volatile boolean running = true;

    // polling mỗi 5 giây (test cho nhanh)
    private final long pollingMillis = 1_000;

    public ScheduledPaymentEngine(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void run() {
        System.out.println("[Scheduler] Started");

        while (running) {
            try {
                processScheduledBills();
                Thread.sleep(pollingMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                // không cho chết thread vì 1 bill lỗi
                e.printStackTrace();
            }
        }

        System.out.println("[Scheduler] Stopped");
    }

    private void processScheduledBills() {
        Date now = new Date();

        for (Map.Entry<Customer, Map<String, Bill>> entry
                : Database.BILLS.entrySet()) {

            Customer customer = entry.getKey();
            Map<String, Bill> bills = entry.getValue();

            for (Bill bill : bills.values()) {
                if (shouldProcess(bill, now)) {
                    processOneBill(customer, bill);
                }
            }
        }
    }

    private boolean shouldProcess(Bill bill, Date now) {
        return bill.getState() != BillState.PAID
                && bill.getScheduledDate() != null
                && !bill.getScheduledDate().after(now);
    }

    private void processOneBill(Customer customer, Bill bill) {
        String billId = bill.getId();

        try {
            boolean ok = paymentService.payBill(customer, billId);

            if (ok) {
                System.out.println("[Scheduler] Auto paid bill " + billId);
            }
        } catch (Exception e) {
            // log + ignore để lần sau retry
            System.out.println("[Scheduler] Failed to auto pay bill "
                    + billId + ": " + e.getMessage());
        }
    }

    public void shutdown() {
        running = false;
    }
}
