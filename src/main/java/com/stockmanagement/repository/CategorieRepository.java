package com.stockmanagement.repository;

import com.stockmanagement.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    
    Optional<Categorie> findByNom(String nom);
    
    boolean existsByNom(String nom);
    
    @Query("SELECT c FROM Categorie c WHERE c.id IN " +
           "(SELECT p.categorie.id FROM Produit p GROUP BY p.categorie.id)")
    List<Categorie> findCategoriesWithProducts();
    
    @Query("SELECT COUNT(p) FROM Produit p WHERE p.categorie.id = :categorieId")
    long countProductsByCategorieId(Long categorieId);
}
