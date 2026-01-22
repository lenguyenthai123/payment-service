package org.example.entity;

import org.example.enums.PaymentState;
import org.example.utils.EntityWithId;

import java.time.LocalDate;

public class PaymentTransaction extends EntityWithId {
    private Double amount;

    private LocalDate createdDate;

    private PaymentState state;

    private String billId;

    public PaymentTransaction(Double amount, LocalDate createdDate, PaymentState state, String billId) {
        this.amount = amount;
        this.createdDate = createdDate;
        this.state = state;
        this.billId = billId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public PaymentState getState() {
        return state;
    }

    public void setState(PaymentState state) {
        this.state = state;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }
}
