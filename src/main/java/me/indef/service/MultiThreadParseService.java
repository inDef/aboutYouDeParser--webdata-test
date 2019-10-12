package me.indef.service;

import lombok.AllArgsConstructor;
import me.indef.model.Product;

import java.util.concurrent.ConcurrentLinkedQueue;

@AllArgsConstructor
public class MultiThreadParseService extends Thread {

    private String link;
    private ConcurrentLinkedQueue<Product> concurrentLinkedQueue;

    @Override
    public void run() {
        ProductParser.getProductsByUrlAndAddToProvidedQueue(link, concurrentLinkedQueue);
    }
}
