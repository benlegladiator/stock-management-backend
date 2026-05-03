package com.stockmanagement.repository;

import com.stockmanagement.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    
    List<Produit> findByCategorieId(Long categorieId);
    
    List<Produit> findByMarque(String marque);
    
    List<Produit> findByNomContainingIgnoreCase(String nom);
    
    List<Produit> findByQuantiteLessThan(Integer quantite);
    
    @Query("SELECT p FROM Produit p WHERE p.prix BETWEEN :minPrix AND :maxPrix")
    List<Produit> findByPriceRange(@Param("minPrix") BigDecimal minPrix, @Param("maxPrix") BigDecimal maxPrix);
    
    @Query("SELECT p FROM Produit p WHERE p.quantite < :seuil ORDER BY p.quantite ASC")
    List<Produit> findLowStockProducts(@Param("seuil") Integer seuil);
    
    @Query("SELECT COUNT(p) FROM Produit p WHERE p.quantite < :seuil")
    long countLowStockProducts(@Param("seuil") Integer seuil);
    
    @Query("SELECT c.nom, COUNT(p) FROM Categorie c LEFT JOIN c.produits p GROUP BY c.nom")
    List<Object[]> countProductsByCategory();
    
    boolean existsByNomAndMarque(String nom, String marque);
}
