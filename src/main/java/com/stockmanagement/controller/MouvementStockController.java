package com.stockmanagement.controller;

import com.stockmanagement.model.MouvementStock;
import com.stockmanagement.model.MouvementStock.TypeMouvement;
import com.stockmanagement.repository.MouvementStockRepository;
import com.stockmanagement.repository.ProduitRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mouvements")
public class MouvementStockController {

    @Autowired
    private MouvementStockRepository mouvementRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @GetMapping
    public List<MouvementStock> getAllMouvements() {
        return mouvementRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MouvementStock> getMouvementById(@PathVariable Long id) {
        Optional<MouvementStock> mouvement = mouvementRepository.findById(id);
        return mouvement.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MouvementStock> createMouvement(@Valid @RequestBody MouvementStock mouvement) {
        if (!produitRepository.existsById(mouvement.getProduit().getId())) {
            return ResponseEntity.badRequest().build();
        }

        mouvement.setProduit(produitRepository.findById(mouvement.getProduit().getId()).orElse(null));
        
        MouvementStock savedMouvement = mouvementRepository.save(mouvement);
        
        updateProductStock(mouvement.getProduit().getId(), mouvement.getTypeMouvement(), mouvement.getQuantite());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMouvement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MouvementStock> updateMouvement(@PathVariable Long id, @Valid @RequestBody MouvementStock mouvementDetails) {
        Optional<MouvementStock> optionalMouvement = mouvementRepository.findById(id);
        if (!optionalMouvement.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if (!produitRepository.existsById(mouvementDetails.getProduit().getId())) {
            return ResponseEntity.badRequest().build();
        }

        MouvementStock mouvement = optionalMouvement.get();
        
        revertProductStock(mouvement.getProduit().getId(), mouvement.getTypeMouvement(), mouvement.getQuantite());
        
        mouvement.setProduit(produitRepository.findById(mouvementDetails.getProduit().getId()).orElse(null));
        mouvement.setTypeMouvement(mouvementDetails.getTypeMouvement());
        mouvement.setQuantite(mouvementDetails.getQuantite());
        mouvement.setDescription(mouvementDetails.getDescription());

        MouvementStock updatedMouvement = mouvementRepository.save(mouvement);
        
        updateProductStock(mouvement.getProduit().getId(), mouvement.getTypeMouvement(), mouvement.getQuantite());
        
        return ResponseEntity.ok(updatedMouvement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable Long id) {
        Optional<MouvementStock> optionalMouvement = mouvementRepository.findById(id);
        if (!optionalMouvement.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        MouvementStock mouvement = optionalMouvement.get();
        revertProductStock(mouvement.getProduit().getId(), mouvement.getTypeMouvement(), mouvement.getQuantite());
        
        mouvementRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/produit/{produitId}")
    public List<MouvementStock> getMouvementsByProduit(@PathVariable Long produitId) {
        return mouvementRepository.findByProduitId(produitId);
    }

    @GetMapping("/type/{typeMouvement}")
    public List<MouvementStock> getMouvementsByType(@PathVariable TypeMouvement typeMouvement) {
        return mouvementRepository.findByTypeMouvement(typeMouvement);
    }

    @GetMapping("/period")
    public List<MouvementStock> getMouvementsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return mouvementRepository.findByDateMouvementBetween(debut, fin);
    }

    @GetMapping("/recent")
    public List<MouvementStock> getRecentMouvements(@RequestParam(defaultValue = "7") Integer days) {
        LocalDateTime debut = LocalDateTime.now().minusDays(days);
        return mouvementRepository.findRecentMouvements(debut);
    }

    @GetMapping("/stats/count-by-type")
    public ResponseEntity<Long> getCountByTypeAndPeriod(
            @RequestParam TypeMouvement typeMouvement,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        long count = mouvementRepository.countByTypeAndPeriod(typeMouvement, debut, fin);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/top-products")
    public List<Object[]> getTopProductsByMovement(
            @RequestParam TypeMouvement typeMouvement,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return mouvementRepository.getTopProductsByMovement(typeMouvement, debut, fin);
    }

    private void updateProductStock(Long produitId, TypeMouvement typeMouvement, Integer quantite) {
        Optional<com.stockmanagement.model.Produit> optionalProduit = produitRepository.findById(produitId);
        if (optionalProduit.isPresent()) {
            com.stockmanagement.model.Produit produit = optionalProduit.get();
            if (typeMouvement == TypeMouvement.ENTREE) {
                produit.setQuantite(produit.getQuantite() + quantite);
            } else {
                produit.setQuantite(Math.max(0, produit.getQuantite() - quantite));
            }
            produitRepository.save(produit);
        }
    }

    private void revertProductStock(Long produitId, TypeMouvement typeMouvement, Integer quantite) {
        Optional<com.stockmanagement.model.Produit> optionalProduit = produitRepository.findById(produitId);
        if (optionalProduit.isPresent()) {
            com.stockmanagement.model.Produit produit = optionalProduit.get();
            if (typeMouvement == TypeMouvement.ENTREE) {
                produit.setQuantite(Math.max(0, produit.getQuantite() - quantite));
            } else {
                produit.setQuantite(produit.getQuantite() + quantite);
            }
            produitRepository.save(produit);
        }
    }
}
