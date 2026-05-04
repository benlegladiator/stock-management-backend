package com.stockmanagement.controller;

import com.stockmanagement.model.MouvementStock;
import com.stockmanagement.model.MouvementStock.TypeMouvement;
import com.stockmanagement.repository.MouvementStockRepository;
import com.stockmanagement.repository.ProduitRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
        try {
            // Vérifier si le produit existe
            if (mouvement.getProduit() == null || mouvement.getProduit().getId() == null) {
                return ResponseEntity.badRequest().build();
            }

            if (!produitRepository.existsById(mouvement.getProduit().getId())) {
                return ResponseEntity.badRequest().build();
            }

            // Récupérer le produit complet
            mouvement.setProduit(produitRepository.findById(mouvement.getProduit().getId()).orElse(null));
            
            // Définir la date si non fournie
            if (mouvement.getDateMouvement() == null) {
                mouvement.setDateMouvement(LocalDateTime.now());
            }
            
            MouvementStock savedMouvement = mouvementRepository.save(mouvement);
            
            // Mettre à jour le stock du produit
            updateProductStock(mouvement.getProduit().getId(), mouvement.getTypeMouvement(), mouvement.getQuantite());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMouvement);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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

        try {
            MouvementStock mouvement = optionalMouvement.get();
            
            // Annuler l'ancien mouvement
            revertProductStock(mouvement.getProduit().getId(), mouvement.getTypeMouvement(), mouvement.getQuantite());
            
            // Mettre à jour le mouvement
            mouvement.setProduit(produitRepository.findById(mouvementDetails.getProduit().getId()).orElse(null));
            mouvement.setTypeMouvement(mouvementDetails.getTypeMouvement());
            mouvement.setQuantite(mouvementDetails.getQuantite());
            mouvement.setDescription(mouvementDetails.getDescription());

            MouvementStock updatedMouvement = mouvementRepository.save(mouvement);
            
            // Appliquer le nouveau mouvement
            updateProductStock(mouvement.getProduit().getId(), mouvement.getTypeMouvement(), mouvement.getQuantite());
            
            return ResponseEntity.ok(updatedMouvement);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable Long id) {
        Optional<MouvementStock> optionalMouvement = mouvementRepository.findById(id);
        if (!optionalMouvement.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            MouvementStock mouvement = optionalMouvement.get();
            
            // Annuler l'effet du mouvement sur le stock
            revertProductStock(mouvement.getProduit().getId(), mouvement.getTypeMouvement(), mouvement.getQuantite());
            
            mouvementRepository.deleteById(id);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/produit/{produitId}")
    public List<MouvementStock> getMouvementsByProduit(@PathVariable Long produitId) {
        return mouvementRepository.findByProduitId(produitId);
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
