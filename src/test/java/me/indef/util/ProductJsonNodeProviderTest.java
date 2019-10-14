package me.indef.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductJsonNodeProviderTest {


    @Test
    void getFromValidUrl() {
        String url = "https://www.aboutyou.de/p/g-star-raw/jeans-3301-4399645";
        JsonNode validTestJsonNode = ProductJsonNodeProvider.getFromUrl(url);
        assert validTestJsonNode != null;
        assertTrue(validTestJsonNode.toString().matches("\\{.+,\"id\":\"4399645\",.+}"));
    }

    @Test
    void getFromInvalidUrl() {
        String url = "https://www.aboutyou.de/p/askd/asd-333";
        JsonNode invalidTestJsonNode = ProductJsonNodeProvider.getFromUrl(url);
        assertNull(invalidTestJsonNode);

    }

    @Test
    void test(){
        String url = "https://www.aboutyou.de/p/only-sons/hemd-alfredo-3649771";
        JsonNode validTestJsonNode = ProductJsonNodeProvider.getFromUrl(url);
        System.out.println(validTestJsonNode);
    }
}