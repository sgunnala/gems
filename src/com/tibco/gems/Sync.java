/*
 * Decompiled with CFR 0_114.
 */
package com.tibco.gems;

public interface Sync {
    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60000;
    public static final long ONE_HOUR = 3600000;
    public static final long ONE_DAY = 86400000;
    public static final long ONE_WEEK = 604800000;
    public static final long ONE_YEAR = 31556952000L;
    public static final long ONE_CENTURY = 3155695200000L;

    public void acquire() throws InterruptedException;

    public boolean attempt(long var1) throws InterruptedException;

    public void release();
}

