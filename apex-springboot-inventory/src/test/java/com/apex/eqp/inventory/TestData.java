package com.apex.eqp.inventory;

import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.entities.RecalledProduct;

public class TestData {
        public static Product[] Products = new Product[] {
                        new Product(1, "apple", 1.5, 10),
                        new Product(2, "cookies", 2.5, 10),
                        new Product(3, "gum", 3.5, 11)
        };

        public static RecalledProduct[] RecalledProducts = new RecalledProduct[] {
                        new RecalledProduct(1, "gum", false)
        };

        public static Product NewTestProduct = Product.builder()
                        .name("test")
                        .price(1.00)
                        .quantity(100)
                        .build();

        public static Product ProductToUpdate = Product.builder()
                        .id(1).price(5.00).quantity(100).build();

        public static Product ProductToDelete = Product.builder().id(1).build();

        public static Product ProductToDeleteAndNotFind = Product.builder().id(100).build();
}
