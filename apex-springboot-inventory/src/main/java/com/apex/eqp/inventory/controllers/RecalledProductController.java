package com.apex.eqp.inventory.controllers;

import com.apex.eqp.inventory.entities.RecalledProduct;
import com.apex.eqp.inventory.services.RecalledProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/inventory/recalled")
public class RecalledProductController {

    private final RecalledProductService recalledProductService;

    @PostMapping
    public ResponseEntity<RecalledProduct> createProduct(@RequestBody RecalledProduct recalledProduct) {
        return ResponseEntity.ok(recalledProductService.save(recalledProduct));
    }

    @GetMapping("/")
    ResponseEntity<Collection<RecalledProduct>> findRecallProducts() {
        Collection<RecalledProduct> allRecalledProducts = recalledProductService.getAllRecalledProducts();
        // List<RecalledProduct> list = new
        // ArrayList<RecalledProduct>(allRecalledProducts);
        // Collections.sort(list, (a, b) -> b.getId().compareTo(a.getId()));
        // Collections.sort(list, (a,b)-> a.getName().compareTo(b.getName()));
        // return ResponseEntity.ok(list);
        return ResponseEntity.ok(allRecalledProducts);
    }
}
