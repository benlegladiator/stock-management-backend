package com.stockmanagement.controller;

import com.stockmanagement.model.Product;
import com.stockmanagement.model.Category;
import com.stockmanagement.model.Stock;
import com.stockmanagement.repository.ProductRepository;
import com.stockmanagement.repository.CategoryRepository;
import com.stockmanagement.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/migration")
public class MigrationController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private StockRepository stockRepository;

    @PostMapping("/categories")
    public ResponseEntity<String> importCategories(@RequestBody List<Category> categories) {
        categoryRepository.saveAll(categories);
        return ResponseEntity.ok("Categories imported successfully");
    }

    @PostMapping("/products")
    public ResponseEntity<String> importProducts(@RequestBody List<Product> products) {
        productRepository.saveAll(products);
        return ResponseEntity.ok("Products imported successfully");
    }

    @PostMapping("/stocks")
    public ResponseEntity<String> importStocks(@RequestBody List<Stock> stocks) {
        stockRepository.saveAll(stocks);
        return ResponseEntity.ok("Stocks imported successfully");
    }
}
