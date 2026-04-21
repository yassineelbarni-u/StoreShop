package com.storeshop.repositories;

import com.storeshop.entities.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Interface de gestion de la persistance pour les produits.
 */
public interface ProduitRepository extends JpaRepository<Produit, Long> {

  /**
   * Recherche des produits à partir d'un mot-clé (nom, description ou catégorie).
   * 
   * @param search   Le texte à rechercher.
   * @param pageable Les informations de pagination.
   * @return Un objet Page contenant les résultats de la recherche.
   */
  @Query(
      "SELECT p FROM Produit p WHERE "
          + "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + "LOWER(p.categorie.nom) LIKE LOWER(CONCAT('%', :search, '%'))")
  Page<Produit> searchProduits(@Param("search") String search, Pageable pageable);

  /**
   * Recherche filtrée pour la partie publique (par catégorie et/ou mot-clé).
   * 
   * @param search      Le texte à rechercher.
   * @param categorieId L'identifiant de la catégorie (optionnel).
   * @param pageable    Les informations de pagination.
   * @return Un objet Page contenant les résultats de la recherche.
   */
  @Query(
      "SELECT p FROM Produit p LEFT JOIN p.categorie c WHERE "
          + "(:categorieId IS NULL OR c.id = :categorieId) AND ("
          + ":search = '' OR "
          + "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + "LOWER(COALESCE(p.description, '')) LIKE LOWER(CONCAT('%', :search, '%')) OR "
          + "LOWER(COALESCE(c.nom, '')) LIKE LOWER(CONCAT('%', :search, '%')))")
  Page<Produit> searchProduitsPublic(
      @Param("search") String search, @Param("categorieId") Long categorieId, Pageable pageable);
}
