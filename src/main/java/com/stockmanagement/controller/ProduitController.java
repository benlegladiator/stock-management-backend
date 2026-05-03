package com.stockmanagement.controller;

import com.stockmanagement.model.Produit;
import com.stockmanagement.repository.ProduitRepository;
import com.stockmanagement.repository.CategorieRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @GetMapping
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        Optional<Produit> produit = produitRepository.findById(id);
        return produit.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Produit> createProduit(@Valid @RequestBody Produit produit) {
        try {
            // Vérifier si la catégorie existe
            if (produit.getCategorie() == null || produit.getCategorie().getId() == null) {
                return ResponseEntity.badRequest().build();
            }

            if (!categorieRepository.existsById(produit.getCategorie().getId())) {
                return ResponseEntity.badRequest().build();
            }

            // Vérifier si le produit existe déjà
            if (produitRepository.existsByNomAndMarque(produit.getNom(), produit.getMarque())) {
                return ResponseEntity.badRequest().build();
            }

            // Récupérer la catégorie complète
            produit.setCategorie(categorieRepository.findById(produit.getCategorie().getId()).orElse(null));
            
            // Sauvegarder le produit
            Produit savedProduit = produitRepository.save(produit);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduit);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @Valid @RequestBody Produit produitDetails) {
        Optional<Produit> optionalProduit = produitRepository.findById(id);
        if (!optionalProduit.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Produit produit = optionalProduit.get();

        if (!categorieRepository.existsById(produitDetails.getCategorie().getId())) {
            return ResponseEntity.badRequest().build();
        }

        if (!produit.getNom().equals(produitDetails.getNom()) && 
            !produit.getMarque().equals(produitDetails.getMarque()) &&
            produitRepository.existsByNomAndMarque(produitDetails.getNom(), produitDetails.getMarque())) {
            return ResponseEntity.badRequest().build();
        }

        produit.setNom(produitDetails.getNom());
        produit.setMarque(produitDetails.getMarque());
        produit.setPrix(produitDetails.getPrix());
        produit.setQuantite(produitDetails.getQuantite());
        produit.setCategorie(categorieRepository.findById(produitDetails.getCategorie().getId()).orElse(null));

        Produit updatedProduit = produitRepository.save(produit);
        return ResponseEntity.ok(updatedProduit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        if (!produitRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produitRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorie/{categorieId}")
    public List<Produit> getProduitsByCategorie(@PathVariable Long categorieId) {
        return produitRepository.findByCategorieId(categorieId);
    }

    @GetMapping("/marque/{marque}")
    public List<Produit> getProduitsByMarque(@PathVariable String marque) {
        return produitRepository.findByMarque(marque);
    }

    @GetMapping("/search")
    public List<Produit> searchProduits(@RequestParam String nom) {
        return produitRepository.findByNomContainingIgnoreCase(nom);
    }

    @GetMapping("/low-stock")
    public List<Produit> getLowStockProducts(@RequestParam(defaultValue = "10") Integer seuil) {
        return produitRepository.findLowStockProducts(seuil);
    }

    @GetMapping("/price-range")
    public List<Produit> getProduitsByPriceRange(@RequestParam BigDecimal minPrix, 
                                                @RequestParam BigDecimal maxPrix) {
        return produitRepository.findByPriceRange(minPrix, maxPrix);
    }

    @GetMapping("/low-stock/count")
    public ResponseEntity<Long> getLowStockCount(@RequestParam(defaultValue = "10") Integer seuil) {
        long count = produitRepository.countLowStockProducts(seuil);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/by-category")
    public List<Object[]> getStatsByCategory() {
        return produitRepository.countProductsByCategory();
    }
}
