package com.stockmanagement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mouvement_stock")
public class MouvementStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    @JsonManagedReference
    private Produit produit;

    @NotNull(message = "Le type de mouvement est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_mouvement", nullable = false, length = 10)
    private TypeMouvement typeMouvement;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime dateMouvement;

    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    private String description;

    // Énumération pour les types de mouvement
    public enum TypeMouvement {
        ENTREE,
        SORTIE
    }

    // Constructeurs
    public MouvementStock() {
        this.dateMouvement = LocalDateTime.now();
    }

    public MouvementStock(Produit produit, TypeMouvement typeMouvement, Integer quantite, String description) {
        this();
        this.produit = produit;
        this.typeMouvement = typeMouvement;
        this.quantite = quantite;
        this.description = description;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public TypeMouvement getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(TypeMouvement typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MouvementStock{" +
                "id=" + id +
                ", produit=" + (produit != null ? produit.getNom() : "null") +
                ", typeMouvement=" + typeMouvement +
                ", quantite=" + quantite +
                ", dateMouvement=" + dateMouvement +
                ", description='" + description + '\'' +
                '}';
    }
}
