package me.indef.service;

import lombok.Getter;

public class CounterService {
    @Getter
    private static Integer httpRequestsCounter = 0;
    @Getter
    private static Integer productsRetrievedCounter = 0;

    static void increaseHTTP() {
        httpRequestsCounter++;
    }

    static void increaseProducts() {
        productsRetrievedCounter++;
    }
}
