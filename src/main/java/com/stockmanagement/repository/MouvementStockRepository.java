package com.stockmanagement.repository;

import com.stockmanagement.model.MouvementStock;
import com.stockmanagement.model.MouvementStock.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {
    
    List<MouvementStock> findByProduitId(Long produitId);
    
    List<MouvementStock> findByTypeMouvement(TypeMouvement typeMouvement);
    
    List<MouvementStock> findByDateMouvementBetween(LocalDateTime debut, LocalDateTime fin);
    
    @Query("SELECT m FROM MouvementStock m WHERE m.produit.id = :produitId AND m.typeMouvement = :typeMouvement ORDER BY m.dateMouvement DESC")
    List<MouvementStock> findByProduitAndType(@Param("produitId") Long produitId, @Param("typeMouvement") TypeMouvement typeMouvement);
    
    @Query("SELECT m FROM MouvementStock m WHERE m.dateMouvement >= :debut ORDER BY m.dateMouvement DESC")
    List<MouvementStock> findRecentMouvements(@Param("debut") LocalDateTime debut);
    
    @Query("SELECT COUNT(m) FROM MouvementStock m WHERE m.typeMouvement = :typeMouvement AND m.dateMouvement BETWEEN :debut AND :fin")
    long countByTypeAndPeriod(@Param("typeMouvement") TypeMouvement typeMouvement, 
                            @Param("debut") LocalDateTime debut, 
                            @Param("fin") LocalDateTime fin);
    
    @Query("SELECT m.produit.nom, SUM(m.quantite) FROM MouvementStock m WHERE m.typeMouvement = :typeMouvement " +
           "AND m.dateMouvement BETWEEN :debut AND :fin GROUP BY m.produit.nom")
    List<Object[]> getTopProductsByMovement(@Param("typeMouvement") TypeMouvement typeMouvement,
                                         @Param("debut") LocalDateTime debut,
                                         @Param("fin") LocalDateTime fin);
}
