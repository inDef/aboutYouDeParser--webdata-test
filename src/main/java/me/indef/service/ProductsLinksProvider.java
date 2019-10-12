package me.indef.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentSkipListSet;

public class ProductsLinksProvider {

    public static ConcurrentSkipListSet<String> getByCategoryPageUrl(String url, Integer page, Integer productsPerPage) {
        ConcurrentSkipListSet<String> links = new ConcurrentSkipListSet<>();

        String categoryId = "";

        try {
            Document categoryPage = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                    .referrer("http://www.google.com").get();
            CounterService.increaseHTTP();

            categoryId = categoryPage.getElementsByAttributeValue("charSet", "UTF-8").get(2).html()
                    .split("\"" + url.split("aboutyou.de")[1] + "\":\"")[1].split("\"}")[0];

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (categoryId.equals("")) return links;

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
            JsonNode entities = new ObjectMapper().readTree(apiUrl).path("entities");

            for (JsonNode entity : entities) {

                String id = entity.path("id").asText();
                String name = entity.path("attributes").path("name").path("values").path("label").asText();
                String brand = entity.path("attributes").path("brand").path("values").path("label").asText();
                String productUrl = formProductUrl(name, brand, id);
                if (!productUrl.isEmpty()) links.add(productUrl);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return links;
    }

    private static String formProductUrl(String name, String brand, String id) {
        name = name.toLowerCase().replaceAll("\\W+", "-");
        if (!name.endsWith("-")) name = name + "-";
        brand = brand.toLowerCase().replaceAll("\\W+", "-");
        return "https://aboutyou.de/p/" + brand + "/" + name + id;
    }
}