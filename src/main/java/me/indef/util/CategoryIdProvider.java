package me.indef.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class CategoryIdProvider {

    public static String getCategoryByUrl(String url) {

        try {
            Document categoryPage = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36")
                    .referrer("http://www.google.com").get();
            Counter.increaseHTTP();

            return categoryPage.getElementsByAttributeValue("charSet", "UTF-8").get(2).html()
                    .split("\"" + url.split("\\?")[0].split("aboutyou.de")[1] + "\":\"")[1].split("\"}")[0];

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
