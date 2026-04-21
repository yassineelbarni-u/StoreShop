package com.storeshop.repositories;

import com.storeshop.entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de gestion de la persistance pour les catégories.
 */
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

  /**
   * Vérifie si une catégorie existe avec un nom donné.
   * 
   * @param nom Le nom à vérifier.
   * @return true si elle existe, sinon false.
   */
  boolean existsByNom(String nom);

  /**
   * Recherche une catégorie par son nom.
   * 
   * @param nom Le nom de la catégorie.
   * @return La catégorie trouvée ou null.
   */
  Categorie findByNom(String nom);
}
