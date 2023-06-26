package com.apex.eqp.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import com.apex.eqp.inventory.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

public class ProductJsonTest {

    JacksonTester<Product> json;

    JacksonTester<Product[]> list;

    @BeforeEach
    public void beforeEach() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @SneakyThrows
    @Test
    public void jsonHasPropertiesTest() {
        Product product = TestData.Products[0];
        JsonContent<Product> test = json.write(product);
        assertThat(test).isStrictlyEqualToJson("single.json");

        assertThat(test).hasJsonPathNumberValue("@.id");
        assertThat(test).extractingJsonPathNumberValue("@.id").isEqualTo(1);

        assertThat(test).hasJsonPathStringValue("@.name");
        assertThat(test).extractingJsonPathStringValue("@.name").isEqualTo("apple");

        assertThat(test).hasJsonPathNumberValue("@.price");
        assertThat(test).extractingJsonPathNumberValue("@.price").isEqualTo(1.5);

        assertThat(test).hasJsonPathNumberValue("@.quantity");
        assertThat(test).extractingJsonPathNumberValue("@.quantity").isEqualTo(10);
    }

    @SneakyThrows
    @Test
    public void propertySerializationTest() {
        Product product = TestData.Products[0];
        ObjectContent<Product> read = json.read("single.json");
        Product readProduct = read.getObject();

        assertThat(readProduct).isEqualTo(product);

        assertThat(product).isEqualTo(readProduct);

        assertThat(readProduct.getId()).isEqualTo(product.getId());
        assertThat(readProduct.getName()).isEqualTo(product.getName());
        assertThat(readProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(readProduct.getQuantity()).isEqualTo(product.getQuantity());
    }

    @Test
    void productListSerializationTest() throws IOException {
        assertThat(list.write(TestData.Products)).isStrictlyEqualToJson("list.json");
    }

    @Test
    void productListDeserializationTest() throws IOException {
        ObjectContent<Product[]> read = list.read("list.json");
        Product[] readProduct = read.getObject();
        assertThat(readProduct).isEqualTo(TestData.Products);
    }

}
