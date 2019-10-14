package me.indef.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryIdProviderTest {

    @Test
    void getCategoryByValidUrl() {
        String url = "https://www.aboutyou.de/maenner/bekleidung";
        Integer categoryId = CategoryIdProvider.getCategoryByUrl(url);
        assertEquals(20290, categoryId);
    }

    @Test
    void  getCategoryByInvaligUrl(){
        String url =  "https://www.aboutyou.de/asd/ad-ad";
        Integer categoryId = CategoryIdProvider.getCategoryByUrl(url);
        assertNull(categoryId);
    }
}