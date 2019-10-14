package me.indef;

import me.indef.model.Product;
import me.indef.service.MultiThreadParseCategoryPagesService;
import me.indef.util.CategoryIdProvider;
import me.indef.util.Counter;
import me.indef.util.JsonToFileService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class App {

    public static void main(String[] args) {


        String url = "https://www.aboutyou.de/maenner/bekleidung";
        Integer pages = 100;


        Set<Product> products = new ConcurrentSkipListSet<>();
        Integer categoryId = CategoryIdProvider.getCategoryByUrl(url);
        List<Thread> threads = Collections.synchronizedList(new ArrayList<>());

        for (Integer page = 1; page <= pages; page++) {
            Thread parsePages = new MultiThreadParseCategoryPagesService(categoryId, page, products);
            threads.add(parsePages);
            parsePages.start();
        }

        do {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (threadsStillWork(threads));

        JsonToFileService.writeToFile(products);
        System.out.println("Http requests done: " + Counter.getHttpRequestsCounter());
        System.out.println("Products retrieved: " + Counter.getProductsRetrievedCounter());
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
