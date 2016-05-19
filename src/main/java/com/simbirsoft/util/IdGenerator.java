package com.simbirsoft.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class IdGenerator {

    private final AtomicInteger idGenerator = new AtomicInteger();

    /**
     * Generate new id.
     * 
     * @return new id
     */
    public int newId() {
        return idGenerator.incrementAndGet();
    }
}
