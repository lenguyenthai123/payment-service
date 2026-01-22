package org.example.service;

import org.example.entity.Customer;

import java.util.Date;

public interface ScheduleService {

    public void schedule(Customer customer, String billId, Date date);

}
