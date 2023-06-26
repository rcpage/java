package com.apex.eqp.inventory.services;

import com.apex.eqp.inventory.entities.Product;
import com.apex.eqp.inventory.entities.RecalledProduct;
import com.apex.eqp.inventory.helpers.ProductFilter;
import com.apex.eqp.inventory.repositories.InventoryRepository;
import com.apex.eqp.inventory.repositories.RecalledProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final InventoryRepository inventoryRepository;
    private final RecalledProductRepository recalledProductRepository;

    @Transactional
    public Product save(Product product) {
        return inventoryRepository.save(product);
    }

    public boolean delete(Product product) {
        try {
            inventoryRepository.deleteById(product.getId());
            return true;
        } catch (Exception error) {
            return false;
        }
    }

    public Collection<Product> getAllProductsWithoutRecalls() {
        Set<String> recalledProductNames = getRecalledProductNames();
        ProductFilter filter = new ProductFilter(recalledProductNames);
        return filter.removeRecalledFrom(inventoryRepository.findAll());
    }

    public Optional<Product> findById(Integer id) {
        Set<String> recalledProductNames = getRecalledProductNames();
        Optional<Product> result = inventoryRepository.findById(id);
        if (result.isPresent()) {
            if (!recalledProductNames.contains(result.get().getName())) {
                return result;
            }
        }
        return Optional.empty();
    }

    private Set<String> getRecalledProductNames() {
        List<RecalledProduct> recalledProducts = recalledProductRepository.findAll();
        Set<String> recalledProductNames = new HashSet<String>();
        for (RecalledProduct product : recalledProducts) {
            recalledProductNames.add(product.getName());
        }
        return recalledProductNames;
    }
}
