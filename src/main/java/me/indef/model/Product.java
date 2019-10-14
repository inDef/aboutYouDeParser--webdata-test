package me.indef.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    Integer id;
    private String name;
    private String brand;
    private String color;
    private String price;
    private String initialPrice;
    private String articleId;
    private List<String> sizesAvailable;
    private String url;
    private String imageUrl;

    @Override
    public int compareTo(Object o) {
        Product other = (Product) o;
        return this.id.compareTo(other.id);
    }
}
