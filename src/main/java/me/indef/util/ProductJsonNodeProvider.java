package me.indef.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ProductJsonNodeProvider {

    public static JsonNode getFromUrl(String url) {

        if (!url.matches("https://.+/p/.+-\\d+")) return null;

        try {
            Document categoryPage = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                    .maxBodySize(0)
                    .referrer("http://www.google.com").get();
            Counter.increaseHTTP();

            String elementJSON = categoryPage.getElementsByAttributeValue("charSet", "UTF-8")
                    .get(2)
                    .html()
                    .split(",\"categoriesByPathname\"")[0]
                    .split(",\"products\":")[1]
                    .split(",\"categories\":\\{")[0];

            String productId = url.split("-")[url.split("-").length - 1];
            JsonNode productJsonNode = new ObjectMapper().readTree(elementJSON).path(productId);
            if (!productJsonNode.toString().isEmpty()) return productJsonNode;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
