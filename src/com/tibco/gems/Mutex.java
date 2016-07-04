/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

public class Mutex
        implements Sync {
    protected boolean inuse_ = false;

    public void acquire() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Mutex mutex = this;
        synchronized (mutex) {
            try {
                while (this.inuse_) {
                    this.wait();
                }
                this.inuse_ = true;
            } catch (InterruptedException var2_2) {
                this.notify();
                throw var2_2;
            }
        }
    }

    public boolean attempt(long l) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Mutex mutex = this;
        synchronized (mutex) {
            if (!this.inuse_) {
                this.inuse_ = true;
                return true;
            }
            if (l <= 0) {
                return false;
            }
            long l2 = l;
            long l3 = System.currentTimeMillis();
            try {
                do {
                    this.wait(l2);
                    if (this.inuse_) continue;
                    this.inuse_ = true;
                    return true;
                } while ((l2 = l - (System.currentTimeMillis() - l3)) > 0);
                return false;
            } catch (InterruptedException var8_5) {
                this.notify();
                throw var8_5;
            }
        }
    }

    public synchronized void release() {
        this.inuse_ = false;
        this.notify();
    }
}

