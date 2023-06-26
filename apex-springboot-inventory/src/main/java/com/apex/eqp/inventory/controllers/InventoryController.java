package com.apex.eqp.inventory.controllers;

import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/inventory/product")
public class InventoryController {

    private final ProductService productService;

    /**
     *
     * @return all the products that are not recalled
     */
    @GetMapping
    public ResponseEntity<Collection<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProductsWithoutRecalls());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProduct(@PathVariable Integer id) {
        Optional<Product> result = productService.findById(id);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            // same as "ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);"
            return ResponseEntity.notFound().build();
        }
        // difficult to understand what this line does
        // without additional inspection
        // return byId.map(ResponseEntity::ok).orElse(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product productUpdate) {
        Optional<Product> find = productService.findById(id);
        if (find.isPresent()) {
            Product update = new Product(id, productUpdate.getName(), productUpdate.getPrice(),
                    productUpdate.getQuantity());
            productService.save(update);
            return ResponseEntity.ok(update);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        Optional<Product> find = productService.findById(id);
        if (find.isPresent()) {
            boolean deleteSuccess = productService.delete(find.get());
            if (deleteSuccess) {
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

}
