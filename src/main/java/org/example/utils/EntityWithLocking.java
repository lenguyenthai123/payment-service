package org.example.utils;

import java.util.concurrent.locks.ReentrantLock;

public abstract class EntityWithLocking extends EntityWithId {
    protected final ReentrantLock lock = new ReentrantLock();

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }
}
