package com.storeshop.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente une catégorie de produits dans la boutique.
 * Sert à organiser les produits par type (ex: Électronique, Vêtements).
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {
  /**
   * Identifiant unique de la catégorie.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Liste des produits appartenant à cette catégorie.
   */
  @OneToMany(mappedBy = "categorie")
  private List<Produit> produits;

  /**
   * Nom de la catégorie.
   */
  private String nom;
}
