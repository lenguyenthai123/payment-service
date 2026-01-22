package org.example.entity;

import org.example.utils.EntityWithId;

import java.util.concurrent.locks.ReentrantLock;

public class Wallet extends EntityWithId {

    private Double balance;

    private final ReentrantLock lock = new ReentrantLock();

    public Wallet(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public void setBalance(Double balance) {
        lock.lock();
        try {
            this.balance = balance;
        } finally {
            lock.unlock();
        }
    }
}
