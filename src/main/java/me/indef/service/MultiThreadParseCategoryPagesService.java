package me.indef.service;

import lombok.AllArgsConstructor;
import me.indef.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class MultiThreadParseCategoryPagesService extends Thread {

    private Integer categoryId;
    private Integer page;
    private Set<Product> products;

    @Override
    public void run() {
        List<String> links = ProductsLinksProvider.getByCategoryId(categoryId, page , 100);
        List<Thread> threads = Collections.synchronizedList(new ArrayList<>());

        for (String link : links) {
            Thread parseProduct = new MultiThreadParseProductService(link, products, true);
            threads.add(parseProduct);
            parseProduct.start();
        }

        do {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (threadsStillWork(threads));
    }

    private static boolean threadsStillWork(List<Thread> threads) {
        for (Thread thread : threads) {
            if (thread.getState().equals(Thread.State.NEW) || thread.isAlive()) {
                return true;
            }
        }

        return false;
    }
}
