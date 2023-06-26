package com.apex.eqp.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.apex.eqp.inventory.entities.Product;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.catalina.connector.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryControllerRestTest {
    @Autowired
    TestRestTemplate API;

    @Test
    public void shouldReturnAProductAsJsonString() {
        Product test = TestData.Products[0];

        ResponseEntity<String> response = API.getForEntity("/api/inventory/product/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(test.getId());

        String name = documentContext.read("$.name");
        assertThat(name).isEqualTo(test.getName());

        Double price = documentContext.read("$.price");
        assertThat(price).isEqualTo(test.getPrice());

        Integer quantity = documentContext.read("$.quantity");
        assertThat(quantity).isEqualTo(test.getQuantity());
    }

    @Test
    public void shouldReturnAProductObject() {
        Product test = TestData.Products[0];
        ResponseEntity<Product> response = API.getForEntity("/api/inventory/product/1", Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(test);
    }

    @Test
    public void shouldReturnProductNotFound() {
        ResponseEntity<String> response = API.getForEntity("/api/inventory/product/0", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldCreateAndReturnProduct() {
        ResponseEntity<Product> response = API.postForEntity("/api/inventory/product", TestData.NewTestProduct,
                Product.class);
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.hasBody()).isEqualTo(true);
        assertThat(response.getBody()).isNotEqualTo(null);

        // update product id
        TestData.NewTestProduct.setId(response.getBody().getId());

        ResponseEntity<Product> find = API.getForEntity(
                getProductPathById(TestData.NewTestProduct.getId()), Product.class);
        assertThat(find.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(find.getBody()).isNotEqualTo(null);
        assertThat(find.getBody()).isEqualTo(TestData.NewTestProduct);
    }

    @Test
    public void shouldUpdateProduct() {

        Product productToUpdate = TestData.ProductToUpdate;
        HttpEntity<Product> httpEntity = new HttpEntity<Product>(productToUpdate);
        ResponseEntity<Product> response = API.exchange(
                getProductPathById(productToUpdate.getId()), HttpMethod.PUT, httpEntity,
                Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody()).isEqualTo(productToUpdate);
    }

    @Test
    public void shouldDeleteProduct() {
        ResponseEntity<Void> response = API.exchange(getProductPathById(TestData.ProductToDelete.getId()),
                HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void shouldAttemptDeleteAndReturnNotFound() {
        ResponseEntity<Void> response = API.exchange(getProductPathById(TestData.ProductToDeleteAndNotFind.getId()),
                HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isNotEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String getProductPathById(Integer id) {
        return String.format("/api/inventory/product/%d", id);
    }
}
