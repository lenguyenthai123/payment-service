package org.example.service.impl;

import org.example.entity.Bill;
import org.example.entity.Customer;
import org.example.service.BillService;
import org.example.service.ScheduleService;

import java.util.Date;

public final class ScheduleServiceImpl implements ScheduleService {

    private final BillService billService;

    public ScheduleServiceImpl(BillService billService) {
        this.billService = billService;
    }

    @Override
    public void schedule(Customer customer, String billId, Date date) {
        Bill bill = billService.find(customer, billId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Bill not found: " + billId));

        if (bill.isPaid()) {
            throw new IllegalStateException("Bill already paid: " + billId);
        }

        bill.setScheduledDate(date);
    }
}
