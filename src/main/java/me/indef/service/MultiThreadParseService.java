package me.indef.service;

import lombok.AllArgsConstructor;
import me.indef.model.Product;

import java.util.concurrent.CopyOnWriteArraySet;

@AllArgsConstructor
public class MultiThreadParseService extends Thread {

    private String link;
    private CopyOnWriteArraySet<Product> productSet;

    @Override
    public void run() {
        ProductParser.getProductsByUrlAndAddToProvidedSet(link, productSet);
    }
}
