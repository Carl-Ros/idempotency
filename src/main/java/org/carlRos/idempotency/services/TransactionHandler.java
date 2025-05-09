package org.carlRos.idempotency.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.function.Supplier;

@Service
public class TransactionHandler {

    @Transactional
    public <T> T runInTransaction(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional
    public <T> void runInTransaction(Runnable action) {
        action.run();
    }
}