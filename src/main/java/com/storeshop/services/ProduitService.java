package com.storeshop.services;

import com.storeshop.entities.Produit;
import org.springframework.data.domain.Page;

/** Product catalog: paged search, load/save/delete, and existence checks. */
public interface ProduitService {

  /** Admin-oriented search over products (implementation defines matching rules). */
  Page<Produit> searchProduits(String search, int page, int size);

  /**
   * Storefront search with optional category filter; empty or blank {@code search} is treated as
   * “no text filter” by the repository layer.
   */
  Page<Produit> searchProduitsPublic(String search, Long categorieId, int page, int size);

  /**
   * @throws RuntimeException if the id does not exist
   */
  Produit getProduitById(Long id);

  /**
   * Validates name, non-negative price and stock, then saves.
   *
   * @throws RuntimeException when validation fails
   */
  Produit saveProduit(Produit produit);

  /**
   * @throws RuntimeException if the id does not exist
   */
  void deleteProduit(Long id);

  boolean produitExists(Long id);
}
