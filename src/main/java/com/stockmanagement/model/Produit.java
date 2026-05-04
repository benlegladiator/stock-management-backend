package com.stockmanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;
    
    @NotBlank(message = "La marque est obligatoire")
    @Size(max = 50, message = "La marque ne doit pas dépasser 50 caractères")
    @Column(name = "marque", nullable = false, length = 50)
    private String marque;
    
    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.1", message = "Le prix doit être supérieur à 0")
    @Column(name = "prix", nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;
    
    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 0, message = "La quantité ne peut pas être négative")
    @Column(name = "quantite", nullable = false)
    private Integer quantite;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    @JsonBackReference
    private Categorie categorie;
    
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;
    
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    // Constructeurs
    public Produit() {
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    public Produit(String nom, String marque, BigDecimal prix, Integer quantite, Categorie categorie) {
        this();
        this.nom = nom;
        this.marque = marque;
        this.prix = prix;
        this.quantite = quantite;
        this.categorie = categorie;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
        this.dateModification = LocalDateTime.now();
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
        this.dateModification = LocalDateTime.now();
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
        this.dateModification = LocalDateTime.now();
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
        this.dateModification = LocalDateTime.now();
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        this.dateModification = LocalDateTime.now();
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", marque='" + marque + '\'' +
                ", prix=" + prix +
                ", quantite=" + quantite +
                ", categorie=" + (categorie != null ? categorie.getNom() : "null") +
                '}';
    }
}
