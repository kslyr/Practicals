package org.example.storage.internal;

import java.util.concurrent.atomic.AtomicLong;

/** Службовий клас модуля: пакет не експортується, інші модулі його не бачать. */
public final class IdGenerator {
    private static final AtomicLong COUNTER = new AtomicLong();

    private IdGenerator() {
    }

    public static long next() {
        return COUNTER.incrementAndGet();
    }
}
