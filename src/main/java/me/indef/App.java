package me.indef;

import me.indef.model.Product;
import me.indef.service.CounterService;
import me.indef.service.JsonToFileService;
import me.indef.service.MultiThreadParseService;
import me.indef.service.ProductsLinksProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class App {
    private static List<MultiThreadParseService> parseThreadList = new ArrayList<>();

    private static Boolean isAnyAlive = true;

    public static void main(String[] args) {

        ConcurrentLinkedQueue<Product> resultQueue = new ConcurrentLinkedQueue<>();

        ConcurrentSkipListSet<String> links = ProductsLinksProvider.getByCategoryPageUrl("https://www.aboutyou.de/maenner/bekleidung", 1, 99);
        for (String link : links) {
            MultiThreadParseService parse = new MultiThreadParseService(link, resultQueue);
            parseThreadList.add(parse);
            parse.start();
        }

        while (isAnyAlive) {
            try {
                Thread.sleep(1000);
                checkAlive(parseThreadList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        JsonToFileService.writeToFile(resultQueue);
        System.out.println("Http requests done: " + CounterService.getHttpRequestsCounter());
        System.out.println("Products retrieved: " + CounterService.getProductsRetrievedCounter());

    }

    private static void checkAlive(List<MultiThreadParseService> parseThreadList) {
        for (MultiThreadParseService parseThread : parseThreadList) {
            if (parseThread.isAlive()) {
                isAnyAlive = true;
                return;
            }
        }
        isAnyAlive = false;
    }
}
