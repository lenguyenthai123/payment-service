package org.example.entity;

import org.example.enums.PaymentState;
import org.example.utils.EntityWithId;

import java.util.Date;

public class Payment extends EntityWithId {
    private Double amount;

    private Date createdDate;

    private PaymentState state;

    private String billId;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
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
