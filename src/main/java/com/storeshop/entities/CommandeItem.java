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
 * Représente un article individuel au sein d'une commande.
 * Contient les informations sur le produit, la quantité et le prix au moment de l'achat.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommandeItem {

  /**
   * Identifiant unique de la ligne de commande.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * La commande à laquelle cet article est associé.
   */
  @ManyToOne(optional = false)
  private Commande commande;

  /**
   * Le produit commandé.
   */
  @ManyToOne(optional = false)
  private Produit produit;

  /**
   * Quantité commandée pour ce produit.
   */
  private int quantity;

  /**
   * Prix unitaire du produit au moment de la commande.
   */
  private double unitPrice;

  /**
   * Total de la ligne (prix unitaire * quantité).
   */
  private double lineTotal;
}
