package com.storeshop.models;

import com.storeshop.entities.Produit;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Représente une ligne détaillée dans le panier pour l'affichage.
 * Combine l'entité Produit avec la quantité et le sous-total calculé.
 */
@AllArgsConstructor
@Data
public class CartLine {

  /** Le produit concerné. */
  private final Produit produit;
  /** Quantité sélectionnée. */
  private final int quantity;
  /** Montant total pour cette ligne (prix * quantité). */
  private final double lineTotal;
}
