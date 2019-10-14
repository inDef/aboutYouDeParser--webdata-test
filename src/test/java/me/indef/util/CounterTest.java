package me.indef.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CounterTest {

    @Test
    void getHttpRequestsCounter() {
        Integer initialState = Counter.getHttpRequestsCounter();
        Counter.increaseHTTP();
        assertEquals(initialState+1, Counter.getHttpRequestsCounter());
    }

    @Test
    void getProductsRetrievedCounter() {
        Integer initialState = Counter.getProductsRetrievedCounter();
        Counter.increaseProducts();
        assertEquals(initialState+1, Counter.getProductsRetrievedCounter());
    }
}