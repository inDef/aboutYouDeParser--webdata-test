package me.indef.service;

import me.indef.model.Product;
import me.indef.util.CategoryIdProvider;
import me.indef.util.Counter;
import me.indef.util.ProductsLinksProvider;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.junit.jupiter.api.Assertions.*;

class ProductParserTest {

    @Test
    void parseOneProductAndAddToProvidedSet() {

        Set<Product> products = new ConcurrentSkipListSet<>();
        Integer categoryId = CategoryIdProvider.getCategoryByUrl("https://www.aboutyou.de/maenner/bekleidung");
        List<String> links = ProductsLinksProvider.getByCategoryId(categoryId, 1, 1);
        ProductParser.parseProductAddToProvidedSet(links.get(0), products, false);

        assertEquals(1, products.size());

    }

    @Test
    void parseOneProductWithSiblingsAndAddToProvidedSet() {

        Set<Product> products = new ConcurrentSkipListSet<>();
        Integer categoryId = CategoryIdProvider.getCategoryByUrl("https://www.aboutyou.de/maenner/bekleidung");
        List<String> links = ProductsLinksProvider.getByCategoryId(categoryId, 2, 1);
        ProductParser.parseProductAddToProvidedSet(links.get(0), products, true);

        assertEquals(products.size(), (int) Counter.getProductsRetrievedCounter());
        System.out.println(products.size()>0);
        System.out.println(products);

    }
}