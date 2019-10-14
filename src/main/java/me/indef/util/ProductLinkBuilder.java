package me.indef.util;

public class ProductLinkBuilder {

    private static String baseUrl = "https://aboutyou.de/p/";

    public static String build(String name, String brand, String id) {
        name = name.toLowerCase().replaceAll("\\W+", "-");
        if (!name.endsWith("-")) name = name + "-";
        brand = brand.toLowerCase().replaceAll("\\W+", " ").trim().replaceAll("\\W+", "-");
        return baseUrl + brand + "/" + name + id.toLowerCase();
    }
}
