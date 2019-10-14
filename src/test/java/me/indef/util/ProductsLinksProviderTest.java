package me.indef.util;

import me.indef.util.ProductsLinksProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductsLinksProviderTest {

    @Test
    void getProductLinksByCategoryId() {
        Integer categoryId = 20290;
        Integer page = 1;
        Integer productsPerPage = 20;

        List<String> links= ProductsLinksProvider.getByCategoryId(categoryId, page, productsPerPage);
        assertEquals(20, links.size());
        for (String link : links) {
            assertTrue(link.matches("https://aboutyou.de/p/.+-\\d+"));
        }
    }

    @Test
    void getProductLinksByInvalidCategoryId() {
        Integer categoryId = 55646;
        Integer page = 1;
        Integer productsPerPage = 20;

        List<String> links= ProductsLinksProvider.getByCategoryId(categoryId, page, productsPerPage);
        assertTrue(links.isEmpty());
    }

}