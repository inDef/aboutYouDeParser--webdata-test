package me.indef.util;

import me.indef.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductLinkBuilderTest {

    @BeforeEach
    void setUp() {
        String name = "testName";
        String brand = "testBrand";
        String  id = "testId";
    }

    @Test
    void build() {
        String name = "test'Name'";
        String brand = "test'Brand'";
        String  id = "testId";

        String link = ProductLinkBuilder.build(name, brand, id);
        assertEquals("https://aboutyou.de/p/test-brand/test-name-testid", link);
    }
}