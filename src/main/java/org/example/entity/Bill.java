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

    private String providerId;

    private String userId;

    public Bill(DomainType type, Double amount, Date dueDate, BillState state, String providerId, String userId) {
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.state = state;
        this.providerId = providerId;
        this.userId = userId;
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

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
