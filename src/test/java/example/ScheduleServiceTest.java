package org.example;

import org.example.entity.Bill;
import org.example.repository.Database;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleServiceTest extends BaseTest {

    @Test
    void should_schedule_bill() {
        Bill bill = Database.BILLS.get(customer)
                .values().iterator().next();

        Date future = new Date(System.currentTimeMillis() + 10_000);

        ctx.scheduleService()
                .schedule(customer, bill.getId(), future);

        assertNotNull(bill.getScheduledDate());
        assertEquals(future, bill.getScheduledDate());
    }
}
