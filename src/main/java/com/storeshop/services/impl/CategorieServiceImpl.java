package com.storeshop.services.impl;

import com.storeshop.entities.Categorie;
import com.storeshop.repositories.CategorieRepository;
import com.storeshop.services.CategorieService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implémentation du service de gestion des catégories.
 * Assure la cohérence des données et les validations métier lors de la manipulation des catégories.
 */
@Service
@Transactional
@AllArgsConstructor
public class CategorieServiceImpl implements CategorieService {

  private final CategorieRepository categorieRepository;

  /**
   * Récupère toutes les catégories disponibles.
   * 
   * @return Liste des catégories.
   */
  @Override
  public List<Categorie> getAllCategories() {
    return categorieRepository.findAll();
  }

  /**
   * Récupère une catégorie par son identifiant unique.
   * 
   * @param id L'ID de la catégorie.
   * @return La catégorie trouvée.
   * @throws RuntimeException Si aucune catégorie ne correspond à l'ID.
   */
  @Override
  public Categorie getCategorieById(Long id) {
    return categorieRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + id));
  }

  /**
   * Enregistre ou met à jour une catégorie.
   * Vérifie que le nom n'est pas vide et qu'il n'y a pas de doublon lors de la création.
   * 
   * @param categorie L'objet catégorie à sauvegarder.
   * @return La catégorie sauvegardée.
   * @throws RuntimeException Si le nom est vide ou déjà utilisé pour une nouvelle catégorie.
   */
  @Override
  public Categorie saveCategorie(Categorie categorie) {
    if (categorie.getNom() == null || categorie.getNom().trim().isEmpty()) {
      throw new RuntimeException("Le nom de la catégorie ne peut pas être vide");
    }

    // Duplicate name guard applies only to inserts; updates keep their id and skip this branch.
    if (categorie.getId() == null) {
      if (categorieRepository.existsByNom(categorie.getNom())) {
        throw new RuntimeException("Une catégorie avec ce nom existe déjà");
      }
    }

    return categorieRepository.save(categorie);
  }

  /**
   * Supprime une catégorie par son ID.
   * 
   * @param id L'ID de la catégorie.
   * @throws RuntimeException Si la catégorie n'existe pas.
   */
  @Override
  public void deleteCategorie(Long id) {
    if (!categorieRepository.existsById(id)) {
      throw new RuntimeException("Catégorie non trouvée avec l'ID: " + id);
    }
    categorieRepository.deleteById(id);
  }

  /**
   * Vérifie si une catégorie existe déjà par son nom.
   * 
   * @param nom Le nom à vérifier.
   * @return true si elle existe, false sinon.
   */
  @Override
  public boolean categorieExists(String nom) {
    return categorieRepository.existsByNom(nom);
  }
}
