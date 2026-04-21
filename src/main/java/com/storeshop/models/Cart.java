package com.storeshop.models;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Représente le panier d'achat d'un utilisateur en mode stockage temporaire.
 * Stocke les ID des produits et les quantités associées.
 */
public class Cart implements Serializable {

  /**
   * Structure de données stockant les associations ProduitID -> Quantité.
   */
  private final Map<Long, Integer> items = new LinkedHashMap<>();

  /**
   * Retourne l'ensemble des articles du panier.
   * 
   * @return Une Map contenant les ID des produits et leurs quantités.
   */
  public Map<Long, Integer> getItems() {
    return items;
  }

  /**
   * Ajoute une quantité spécifiée d'un produit au panier.
   * Si le produit existe déjà, la quantité est augmentée.
   * 
   * @param produitId L'identifiant du produit.
   * @param quantity  La quantité à ajouter.
   */
  public void addItem(Long produitId, int quantity) {
    if (quantity <= 0) {
      return;
    }
    items.merge(produitId, quantity, Integer::sum);
  }

  /**
   * Met à jour la quantité d'un produit spécifique dans le panier.
   * Si la quantité est nulle ou négative, le produit est retiré.
   * 
   * @param produitId L'identifiant du produit.
   * @param quantity  La nouvelle quantité.
   */
  public void updateItem(Long produitId, int quantity) {
    if (quantity <= 0) {
      items.remove(produitId);
      return;
    }
    items.put(produitId, quantity);
  }

  /**
   * Retire complètement un produit du panier.
   * 
   * @param produitId L'identifiant du produit à retirer.
   */
  public void removeItem(Long produitId) {
    items.remove(produitId);
  }

  /**
   * Vide totalement le contenu du panier.
   */
  public void clear() {
    items.clear();
  }

  /**
   * Vérifie si le panier est vide.
   * 
   * @return true si aucune article n'est présent, sinon false.
   */
  public boolean isEmpty() {
    return items.isEmpty();
  }
}
