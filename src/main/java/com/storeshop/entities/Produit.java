package com.storeshop.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente un produit disponible dans la boutique.
 * Contient les informations de base telles que le nom, le prix, la description et le stock.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produit {

  /**
   * Identifiant unique du produit.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Catégorie à laquelle appartient le produit.
   */
  @ManyToOne private Categorie categorie;

  /**
   * Nom du produit.
   */
  private String name;

  /**
   * URL ou chemin vers l'image du produit.
   */
  private String imageUrl;

  /**
   * Description détaillée du produit.
   */
  private String description;

  /**
   * Prix de vente unitaire du produit.
   */
  private double price;

  /**
   * Quantité disponible en stock.
   */
  private int stock;
}
