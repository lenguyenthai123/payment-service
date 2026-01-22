package org.example.entity;

import org.example.utils.EntityWithId;
import org.example.utils.EntityWithLocking;

import java.util.concurrent.locks.ReentrantLock;

public class Wallet extends EntityWithLocking {

    private Double balance;

    public Wallet(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        lock();
        try {
            return balance;
        } finally {
            unlock();
        }
    }

    public void setBalance(Double balance) {
        lock();
        try {
            this.balance = balance;
        } finally {
            unlock();
        }
    }
}
