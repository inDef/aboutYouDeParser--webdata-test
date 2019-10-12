package me.indef;

import me.indef.model.Product;
import me.indef.service.CounterService;
import me.indef.service.JsonToFileService;
import me.indef.service.MultiThreadParseService;
import me.indef.service.ProductsLinksProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

public class App {
    private static List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) {

        CopyOnWriteArraySet<Product> resultSet = new CopyOnWriteArraySet<>();

        ConcurrentSkipListSet<String> links = ProductsLinksProvider.getByCategoryPageUrl("https://www.aboutyou.de/maenner/bekleidung", 1, 99);

        for (String link : links) {
            Thread parse = new MultiThreadParseService(link, resultSet);
            threads.add(parse);
            parse.start();
        }

        do {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (threadsStillWork(threads));

        JsonToFileService.writeToFile(resultSet);
        System.out.println("Http requests done: " + CounterService.getHttpRequestsCounter());
        System.out.println("Products retrieved: " + CounterService.getProductsRetrievedCounter());

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
