package org.example.entity;

import org.example.enums.BillState;
import org.example.enums.DomainType;
import org.example.utils.EntityWithId;

import java.util.Date;

public class Bill extends EntityWithId {

    private DomainType type;

    private Double amount;

    private Date dueDate;

    private BillState state;

    private String provider;

    private String userId;

    private Date scheduledDate;

    public Bill(DomainType type, Double amount, Date dueDate, BillState state, String provider, String userId, Date scheduledDate) {
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.state = state;
        this.provider = provider;
        this.userId = userId;
        this.scheduledDate = scheduledDate;
    }

    public DomainType getType() {
        return type;
    }

    public void setType(DomainType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BillState getState() {
        return state;
    }

    public void setState(BillState state) {
        this.state = state;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean isPaid() {
        return this.state == BillState.PAID;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public boolean isScheduled() {
        return scheduledDate != null;
    }
}
