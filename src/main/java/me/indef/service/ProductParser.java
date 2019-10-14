package me.indef.service;

import com.fasterxml.jackson.databind.JsonNode;
import me.indef.model.Product;
import me.indef.util.Counter;
import me.indef.util.ProductJsonNodeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

class ProductParser {

    private static final Semaphore SEMAPHORE = new Semaphore(50, true);

    public static void parseProductAddToProvidedSet(String productUrl, Set<Product> productSet, Boolean includeSiblings) {

        JsonNode productJSON = null;

        try {
            SEMAPHORE.acquire();

        if (!doesProductSetContainProductByUrl(productUrl, productSet)) {
            productJSON = ProductJsonNodeProvider.getFromUrl(productUrl);
        }

        SEMAPHORE.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (productJSON == null) return;

        Product product = fetchProductFromJsonNode(productJSON);
        if (product.getId() < 0) {
            System.out.println("This product url is unreachable: " + productUrl + ". It won't be added.");
            return;
        }
        if (productSet.add(product)) Counter.increaseProducts();

        if (!includeSiblings) return;
        JsonNode siblings = productJSON.path("siblings");
        for (JsonNode sibling : siblings) {
            Product siblingProduct = fetchProductFromJsonNode(sibling);
            if (productSet.add(siblingProduct)) Counter.increaseProducts();
        }

    }

    private static Product fetchProductFromJsonNode(JsonNode productJSON) {

        Integer id = productJSON.path("id").asInt(-1);

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
        product.setId(id);
        product.setArticleId(articleId);
        product.setName(name);
        product.setBrand(brand);
        product.setColor(color);
        product.setPrice(formatPrice(price, currency));
        product.setInitialPrice(formatPrice(initialPrice, currency));
        product.setSizesAvailable(sizesAvailable);
        product.setUrl("https://www.aboutyou.de" + url);
        product.setImageUrl(imageUrl);

        return product;
    }

    private static String formatPrice(Integer price, String currency) {
        return price / 100 + "." + price % 100 + " " + currency;
    }

    private static Boolean doesProductSetContainProductByUrl(String url, Set<Product> productSet) {
        String[] splitUtilArray = url.split("-");
        Integer productId = Integer.valueOf(splitUtilArray[splitUtilArray.length - 1]);
        Product testProduct = new Product();
        testProduct.setId(productId);
        return productSet.contains(testProduct);
    }

}
