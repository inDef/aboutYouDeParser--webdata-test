package me.indef.util;

import lombok.Getter;

public class Counter {
    @Getter
    private static Integer httpRequestsCounter = 0;
    @Getter
    private static Integer productsRetrievedCounter = 0;

    public static void increaseHTTP() {
        httpRequestsCounter++;
    }

    public static void increaseProducts() {
        productsRetrievedCounter++;
    }
}
