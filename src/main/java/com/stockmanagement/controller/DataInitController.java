package com.stockmanagement.controller;

import com.stockmanagement.model.Categorie;
import com.stockmanagement.model.Produit;
import com.stockmanagement.model.MouvementStock;
import com.stockmanagement.model.MouvementStock.TypeMouvement;
import com.stockmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/init")
public class DataInitController {

    @Autowired
    private CategorieRepository categorieRepository;
    
    @Autowired
    private ProduitRepository produitRepository;
    
    @Autowired
    private MouvementStockRepository mouvementRepository;

    @PostMapping("/data")
    public ResponseEntity<String> initializeData() {
        try {
            // Nettoyage des données existantes
            mouvementRepository.deleteAll();
            produitRepository.deleteAll();
            categorieRepository.deleteAll();
            
            // Création des catégories
            Categorie cat1 = new Categorie();
            cat1.setNom("Téléphones");
            cat1 = categorieRepository.save(cat1);
            
            Categorie cat2 = new Categorie();
            cat2.setNom("Ordinateurs");
            cat2 = categorieRepository.save(cat2);
            
            Categorie cat3 = new Categorie();
            cat3.setNom("Tablettes");
            cat3 = categorieRepository.save(cat3);
            
            Categorie cat4 = new Categorie();
            cat4.setNom("Téléviseurs");
            cat4 = categorieRepository.save(cat4);
            
            Categorie cat5 = new Categorie();
            cat5.setNom("Consoles de jeux");
            cat5 = categorieRepository.save(cat5);
            
            // Création des produits
            Produit p1 = new Produit();
            p1.setNom("iPhone 15 Pro");
            p1.setMarque("Apple");
            p1.setPrix(new BigDecimal("1199.99"));
            p1.setQuantite(25);
            p1.setCategorie(cat1);
            p1.setDateCreation(LocalDateTime.now());
            p1.setDateModification(LocalDateTime.now());
            p1 = produitRepository.save(p1);
            
            Produit p2 = new Produit();
            p2.setNom("Samsung Galaxy S24");
            p2.setMarque("Samsung");
            p2.setPrix(new BigDecimal("999.99"));
            p2.setQuantite(30);
            p2.setCategorie(cat1);
            p2.setDateCreation(LocalDateTime.now());
            p2.setDateModification(LocalDateTime.now());
            p2 = produitRepository.save(p2);
            
            Produit p3 = new Produit();
            p3.setNom("MacBook Pro 16\"");
            p3.setMarque("Apple");
            p3.setPrix(new BigDecimal("2499.99"));
            p3.setQuantite(15);
            p3.setCategorie(cat2);
            p3.setDateCreation(LocalDateTime.now());
            p3.setDateModification(LocalDateTime.now());
            p3 = produitRepository.save(p3);
            
            Produit p4 = new Produit();
            p4.setNom("Dell XPS 15");
            p4.setMarque("Dell");
            p4.setPrix(new BigDecimal("1799.99"));
            p4.setQuantite(20);
            p4.setCategorie(cat2);
            p4.setDateCreation(LocalDateTime.now());
            p4.setDateModification(LocalDateTime.now());
            p4 = produitRepository.save(p4);
            
            Produit p5 = new Produit();
            p5.setNom("iPad Pro 12.9\"");
            p5.setMarque("Apple");
            p5.setPrix(new BigDecimal("1099.99"));
            p5.setQuantite(18);
            p5.setCategorie(cat3);
            p5.setDateCreation(LocalDateTime.now());
            p5.setDateModification(LocalDateTime.now());
            p5 = produitRepository.save(p5);
            
            Produit p6 = new Produit();
            p6.setNom("Samsung Galaxy Tab S9");
            p6.setMarque("Samsung");
            p6.setPrix(new BigDecimal("899.99"));
            p6.setQuantite(22);
            p6.setCategorie(cat3);
            p6.setDateCreation(LocalDateTime.now());
            p6.setDateModification(LocalDateTime.now());
            p6 = produitRepository.save(p6);
            
            Produit p7 = new Produit();
            p7.setNom("Samsung OLED 55\"");
            p7.setMarque("Samsung");
            p7.setPrix(new BigDecimal("1299.99"));
            p7.setQuantite(8);
            p7.setCategorie(cat4);
            p7.setDateCreation(LocalDateTime.now());
            p7.setDateModification(LocalDateTime.now());
            p7 = produitRepository.save(p7);
            
            Produit p8 = new Produit();
            p8.setNom("LG OLED 65\"");
            p8.setMarque("LG");
            p8.setPrix(new BigDecimal("1899.99"));
            p8.setQuantite(5);
            p8.setCategorie(cat4);
            p8.setDateCreation(LocalDateTime.now());
            p8.setDateModification(LocalDateTime.now());
            p8 = produitRepository.save(p8);
            
            Produit p9 = new Produit();
            p9.setNom("PlayStation 5");
            p9.setMarque("Sony");
            p9.setPrix(new BigDecimal("499.99"));
            p9.setQuantite(35);
            p9.setCategorie(cat5);
            p9.setDateCreation(LocalDateTime.now());
            p9.setDateModification(LocalDateTime.now());
            p9 = produitRepository.save(p9);
            
            Produit p10 = new Produit();
            p10.setNom("Xbox Series X");
            p10.setMarque("Microsoft");
            p10.setPrix(new BigDecimal("499.99"));
            p10.setQuantite(28);
            p10.setCategorie(cat5);
            p10.setDateCreation(LocalDateTime.now());
            p10.setDateModification(LocalDateTime.now());
            p10 = produitRepository.save(p10);
            
            // Création des mouvements de stock
            MouvementStock m1 = new MouvementStock();
            m1.setProduit(p1);
            m1.setTypeMouvement(TypeMouvement.ENTREE);
            m1.setQuantite(50);
            m1.setDateMouvement(LocalDateTime.now().minusDays(30));
            m1.setDescription("Réception stock initial");
            mouvementRepository.save(m1);
            
            MouvementStock m2 = new MouvementStock();
            m2.setProduit(p1);
            m2.setTypeMouvement(TypeMouvement.SORTIE);
            m2.setQuantite(25);
            m2.setDateMouvement(LocalDateTime.now().minusDays(15));
            m2.setDescription("Vente client");
            mouvementRepository.save(m2);
            
            MouvementStock m3 = new MouvementStock();
            m3.setProduit(p3);
            m3.setTypeMouvement(TypeMouvement.ENTREE);
            m3.setQuantite(20);
            m3.setDateMouvement(LocalDateTime.now().minusDays(25));
            m3.setDescription("Nouvelle livraison");
            mouvementRepository.save(m3);
            
            MouvementStock m4 = new MouvementStock();
            m4.setProduit(p3);
            m4.setTypeMouvement(TypeMouvement.SORTIE);
            m4.setQuantite(5);
            m4.setDateMouvement(LocalDateTime.now().minusDays(10));
            m4.setDescription("Vente en ligne");
            mouvementRepository.save(m4);
            
            MouvementStock m5 = new MouvementStock();
            m5.setProduit(p7);
            m5.setTypeMouvement(TypeMouvement.ENTREE);
            m5.setQuantite(10);
            m5.setDateMouvement(LocalDateTime.now().minusDays(20));
            m5.setDescription("Commande fournisseur");
            mouvementRepository.save(m5);
            
            MouvementStock m6 = new MouvementStock();
            m6.setProduit(p7);
            m6.setTypeMouvement(TypeMouvement.SORTIE);
            m6.setQuantite(2);
            m6.setDateMouvement(LocalDateTime.now().minusDays(5));
            m6.setDescription("Vente magasin");
            mouvementRepository.save(m6);
            
            return ResponseEntity.ok("Données initialisées avec succès !");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'initialisation: " + e.getMessage());
        }
    }
}
