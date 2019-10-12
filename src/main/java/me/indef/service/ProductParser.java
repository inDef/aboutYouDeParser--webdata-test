package me.indef.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.indef.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

class ProductParser {

    static void getProductsByUrlAndAddToProvidedSet
            (String url, CopyOnWriteArraySet<Product> productSet) {

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

            getProductFromJsonNodeAndAddToProvidedSet(productJSON, productSet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getProductFromJsonNodeAndAddToProvidedSet
            (JsonNode productJSON, CopyOnWriteArraySet<Product> productSet) {

        if (productJSON.size() == 0) return;

        String name = productJSON.path("name").asText();

        String brand = productJSON.path("brandName").asText();

        String color = productJSON.path("detailColors").path(0).path("label").asText();

        String articleId = productJSON.path("defaultVariant").path("merchantProductVariantId").asText();

        Integer price = productJSON.path("prices").path("beforeCampaignPrice").asInt();

        Integer initialPrice = productJSON.path("originalPrice").asInt();
        if (initialPrice == 0) initialPrice = price;

        String currency = productJSON.path("prices").path("priceRangeRaw").path("min").path("currencyCode").asText();

        String url = productJSON.path("url").asText();

        String imageId = productJSON.path("defaultImage").path("id").asText();
        String imageUrl = "https://cdn.aboutstatic.com/file/" + imageId;

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
        product.setPrice(formatPrice(price, currency));
        product.setInitialPrice(formatPrice(initialPrice, currency));
        product.setSizesAvailable(sizesAvailable);
        product.setUrl("https://www.aboutyou.de" + url);
        product.setImageUrl(imageUrl);

        if (productSet.add(product)) CounterService.increaseProducts();

        JsonNode siblings = productJSON.path("siblings");
        if (siblings.size() > 0) {
            for (JsonNode sibling :
                    siblings) {
                getProductFromJsonNodeAndAddToProvidedSet(sibling, productSet);
            }
        }
    }

    private static String formatPrice(Integer price, String currency) {
        return price / 100 + "." + price % 100 + " " + currency;
    }

}
