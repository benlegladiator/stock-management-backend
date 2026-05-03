package com.stockmanagement.controller;

import com.stockmanagement.model.Categorie;
import com.stockmanagement.repository.CategorieRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    @Autowired
    private CategorieRepository categorieRepository;

    @GetMapping
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable Long id) {
        Optional<Categorie> categorie = categorieRepository.findById(id);
        return categorie.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Categorie> createCategorie(@Valid @RequestBody Categorie categorie) {
        if (categorieRepository.existsByNom(categorie.getNom())) {
            return ResponseEntity.badRequest().build();
        }
        Categorie savedCategorie = categorieRepository.save(categorie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategorie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categorie> updateCategorie(@PathVariable Long id, @Valid @RequestBody Categorie categorieDetails) {
        Optional<Categorie> optionalCategorie = categorieRepository.findById(id);
        if (!optionalCategorie.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Categorie categorie = optionalCategorie.get();
        
        if (!categorie.getNom().equals(categorieDetails.getNom()) && 
            categorieRepository.existsByNom(categorieDetails.getNom())) {
            return ResponseEntity.badRequest().build();
        }

        categorie.setNom(categorieDetails.getNom());
        Categorie updatedCategorie = categorieRepository.save(categorie);
        return ResponseEntity.ok(updatedCategorie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategorie(@PathVariable Long id) {
        if (!categorieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        long productCount = categorieRepository.countProductsByCategorieId(id);
        if (productCount > 0) {
            return ResponseEntity.badRequest().build();
        }

        categorieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/with-products")
    public List<Categorie> getCategoriesWithProducts() {
        return categorieRepository.findCategoriesWithProducts();
    }

    @GetMapping("/{id}/product-count")
    public ResponseEntity<Long> getProductCountByCategorie(@PathVariable Long id) {
        if (!categorieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        long count = categorieRepository.countProductsByCategorieId(id);
        return ResponseEntity.ok(count);
    }
}
