package me.indef.service;

import lombok.AllArgsConstructor;
import me.indef.model.Product;

import java.util.Set;

@AllArgsConstructor
public class MultiThreadParseProductService extends Thread {

    private String link;
    private Set<Product> productSet;
    private Boolean includeSiblings;

    @Override
    public void run() {

        ProductParser.parseProductAddToProvidedSet(link, productSet, includeSiblings);

    }
}
