package com.storeshop.services.impl;

import com.storeshop.entities.Produit;
import com.storeshop.repositories.ProduitRepository;
import com.storeshop.services.ProduitService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Implémentation du service de gestion des produits.
 * Délègue les opérations de recherche, de récupération et de persistance au {@link ProduitRepository}.
 * Intègre des validations métier pour le prix et le stock.
 */
@Service
@Transactional
@AllArgsConstructor
public class ProduitServiceImpl implements ProduitService {

  private final ProduitRepository produitRepository;

  /**
   * Recherche globale des produits pour l'interface d'administration.
   * 
   * @param search Mot-clé de recherche.
   * @param page   Numéro de la page.
   * @param size   Taille de la page.
   * @return Une page de résultats.
   */
  @Override
  public Page<Produit> searchProduits(String search, int page, int size) {
    return produitRepository.searchProduits(search, PageRequest.of(page, size));
  }

  /**
   * Recherche de produits pour l'interface publique avec filtrage par catégorie.
   * 
   * @param search      Mot-clé de recherche.
   * @param categorieId Identifiant de la catégorie (optionnel).
   * @param page        Numéro de la page.
   * @param size        Taille de la page.
   * @return Une page de résultats.
   */
  @Override
  public Page<Produit> searchProduitsPublic(String search, Long categorieId, int page, int size) {
    String normalizedSearch = search == null ? "" : search.trim();
    return produitRepository.searchProduitsPublic(
        normalizedSearch, categorieId, PageRequest.of(page, size));
  }

  /**
   * Récupère un produit par son ID unique.
   * 
   * @param id L'identifiant du produit.
   * @return Le produit trouvé.
   * @throws RuntimeException Si le produit n'existe pas.
   */
  @Override
  public Produit getProduitById(Long id) {
    return produitRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Produit non trouve avec id: " + id));
  }

  /**
   * Enregistre un produit en effectuant des validations sur le nom, le prix et le stock.
   * 
   * @param produit Le produit à sauvegarder.
   * @return Le produit sauvegardé.
   * @throws RuntimeException En cas de données invalides (prix < 0, stock < 0, etc.).
   */
  @Override
  public Produit saveProduit(Produit produit) {
    if (produit.getName() == null || produit.getName().trim().isEmpty()) {
      throw new RuntimeException("Le nom du produit ne peut pas etre vide");
    }

    if (produit.getPrice() < 0) {
      throw new RuntimeException("Le prix ne peut pas être negatif");
    }

    if (produit.getStock() < 0) {
      throw new RuntimeException("Le stock ne peut pas etre negatif");
    }

    return produitRepository.save(produit);
  }

  /**
   * Supprime un produit par son identifiant unique.
   * 
   * @param id L'identifiant du produit.
   * @throws RuntimeException Si le produit n'existe pas.
   */
  @Override
  public void deleteProduit(Long id) {
    if (!produitRepository.existsById(id)) {
      throw new RuntimeException("Produit non trouve avec l'ID: " + id);
    }
    produitRepository.deleteById(id);
  }

  /**
   * Vérifie l'existence d'un produit.
   * 
   * @param id L'identifiant du produit.
   * @return true si le produit existe, false sinon.
   */
  @Override
  public boolean produitExists(Long id) {
    return produitRepository.existsById(id);
  }
}
