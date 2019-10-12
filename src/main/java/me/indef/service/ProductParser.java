package me.indef.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.indef.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

class ProductParser {

    static void getProductsByUrlAndAddToProvidedQueue
            (String url, ConcurrentLinkedQueue<Product> productConcurrentLinkedQueue) {

        try {
            Document categoryPage = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                    .referrer("http://www.google.com").get();
            CounterService.increaseHTTP();

            String elementJSON = categoryPage.getElementsByAttributeValue("charSet", "UTF-8")
                    .get(2).html()
                    .split(",\"categoriesByPathname\"")[0]
                    .split(",\"products\":")[1] + "}";

            String productId = url.split("-")[url.split("-").length - 1];

            JsonNode productJSON = new ObjectMapper()
                    .readTree(elementJSON)
                    .path(productId);

            getProductFromJsonNodeAndAddToProvidedQueue(productJSON, productConcurrentLinkedQueue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getProductFromJsonNodeAndAddToProvidedQueue
            (JsonNode productJSON, ConcurrentLinkedQueue<Product> productConcurrentLinkedQueue) {

        if (productJSON.size() == 0) return;

        String name = productJSON.path("name").asText();
        String brand = productJSON.path("brandName").asText();
        String color = productJSON.path("detailColors").path(0).path("label").asText();
        String articleId = productJSON.path("defaultVariant").path("merchantProductVariantId").asText();
        String price = productJSON.path("prices").path("beforeCampaignPrice").asText();

        if (price.length() == 0) price = "000";

        String formattedPrice = price.substring(0, price.length() - 2) + "."
                + price.substring(price.length() - 2) + " EUR";

        List<String> sizesAvailable = new ArrayList<>();
        JsonNode variants = productJSON.path("variants");

        for (JsonNode variant : variants) {

            String size = variant.path("sizes").path("shop").asText();
            String length = variant.path("sizes").path("length").asText();
            Integer storeQuantity = variant.path("quantity").asInt();

            if (storeQuantity > 0) {
                if (!length.equals("null")) {
                    sizesAvailable.add(size + "/" + length);
                } else {
                    sizesAvailable.add(size);
                }
            }
        }

        Product product = new Product();
        product.setArticleId(articleId);
        product.setName(name);
        product.setBrand(brand);
        product.setColor(color);
        product.setPrice(formattedPrice);
        product.setSizesAvailable(sizesAvailable);

        productConcurrentLinkedQueue.add(product);
        CounterService.increaseProducts();

        JsonNode siblings = productJSON.path("siblings");
        if (siblings.size() > 0) {
            for (JsonNode sibling :
                    siblings) {
                getProductFromJsonNodeAndAddToProvidedQueue(sibling, productConcurrentLinkedQueue);
            }
        }
    }

}
