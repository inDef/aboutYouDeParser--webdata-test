package me.indef.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.indef.util.CategoryIdProvider;
import me.indef.util.Counter;
import me.indef.util.ProductLinkBuilder;

import java.net.URL;
import java.util.concurrent.ConcurrentSkipListSet;

public class ProductsLinksProvider {

    public static ConcurrentSkipListSet<String> getByCategoryPageUrl(String url, Integer page, Integer productsPerPage) {
        ConcurrentSkipListSet<String> links = new ConcurrentSkipListSet<>();

        String categoryId = CategoryIdProvider.getCategoryByUrl(url);
        if (categoryId == null) return links;

        String apiAddress = "https://api-cloud.aboutyou.de/v1/products?" +
                "with=attributes:key(brand|name|colorDetail)," +
                "advancedAttributes:key(siblings)," +
//                "variants," +
//                "categories," +
                "priceRange&filters[category]=" + categoryId +
                "&sortDir=desc" +
                "&sortScore=category_scores" +
                "&sortChannel=etkp" +
                "&page=" + page +
                "&perPage=" + productsPerPage +
                "&campaignKey=px" +
                "&shopId=139";

        try {
            URL apiUrl = new URL(apiAddress);
            JsonNode json = new ObjectMapper().readTree(apiUrl);
            Counter.increaseHTTP();

            JsonNode pagination = json.path("pagination");
            JsonNode entities = json.path("entities");


            for (JsonNode entity : entities) {

                String id = entity.path("id").asText();
                String name = entity.path("attributes").path("name").path("values").path("label").asText();
                String brand = entity.path("attributes").path("brand").path("values").path("label").asText();
                String productUrl = ProductLinkBuilder.build(name, brand, id);
                if (!productUrl.isEmpty()) links.add(productUrl);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return links;
    }

}