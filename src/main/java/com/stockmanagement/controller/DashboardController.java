package com.stockmanagement.controller;

import com.stockmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private MouvementStockRepository mouvementRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalProduits", produitRepository.count());
        stats.put("totalCategories", categorieRepository.count());
        stats.put("lowStockCount", produitRepository.countLowStockProducts(10));
        stats.put("totalMouvements", mouvementRepository.count());
        
        LocalDateTime debut = LocalDateTime.now().minusDays(30);
        LocalDateTime fin = LocalDateTime.now();
        
        long entreesCount = mouvementRepository.countByTypeAndPeriod(
                com.stockmanagement.model.MouvementStock.TypeMouvement.ENTREE, debut, fin);
        long sortiesCount = mouvementRepository.countByTypeAndPeriod(
                com.stockmanagement.model.MouvementStock.TypeMouvement.SORTIE, debut, fin);
        
        stats.put("entrees30jours", entreesCount);
        stats.put("sorties30jours", sortiesCount);
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/produits-par-categorie")
    public List<Object[]> getProduitsParCategorie() {
        return produitRepository.countProductsByCategory();
    }

    @GetMapping("/produits-faible-stock")
    public ResponseEntity<?> getProduitsFaibleStock(@RequestParam(defaultValue = "10") Integer seuil) {
        List<com.stockmanagement.model.Produit> produits = produitRepository.findLowStockProducts(seuil);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/mouvements-recents")
    public ResponseEntity<?> getMouvementsRecents(@RequestParam(defaultValue = "7") Integer days) {
        LocalDateTime debut = LocalDateTime.now().minusDays(days);
        List<com.stockmanagement.model.MouvementStock> mouvements = mouvementRepository.findRecentMouvements(debut);
        return ResponseEntity.ok(mouvements);
    }

    @GetMapping("/top-produits-entrees")
    public List<Object[]> getTopProduitsEntrees(@RequestParam(defaultValue = "30") Integer days) {
        LocalDateTime debut = LocalDateTime.now().minusDays(days);
        LocalDateTime fin = LocalDateTime.now();
        return mouvementRepository.getTopProductsByMovement(
                com.stockmanagement.model.MouvementStock.TypeMouvement.ENTREE, debut, fin);
    }

    @GetMapping("/top-produits-sorties")
    public List<Object[]> getTopProduitsSorties(@RequestParam(defaultValue = "30") Integer days) {
        LocalDateTime debut = LocalDateTime.now().minusDays(days);
        LocalDateTime fin = LocalDateTime.now();
        return mouvementRepository.getTopProductsByMovement(
                com.stockmanagement.model.MouvementStock.TypeMouvement.SORTIE, debut, fin);
    }

    @GetMapping("/categories-avec-produits")
    public List<com.stockmanagement.model.Categorie> getCategoriesAvecProduits() {
        return categorieRepository.findCategoriesWithProducts();
    }
}
