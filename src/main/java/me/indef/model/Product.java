package me.indef.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String name;
    private String brand;
    private String color;
    private String price;
    private String initialPrice;
    private String articleId;
    private List<String> sizesAvailable;
    private String url;
    private String imageUrl;

}
