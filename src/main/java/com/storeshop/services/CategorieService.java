package com.storeshop.services;

import com.storeshop.entities.Categorie;
import java.util.List;

/** CRUD and name checks for product categories. */
public interface CategorieService {

  List<Categorie> getAllCategories();

  /**
   * @param id primary key
   * @return the category
   * @throws RuntimeException if no category exists for {@code id}
   */
  Categorie getCategorieById(Long id);

  /**
   * Persists a category. New entities must have a non-blank unique name; updates may reuse the same
   * name for the same id depending on repository rules.
   *
   * @throws RuntimeException on empty name or duplicate name for a new category
   */
  Categorie saveCategorie(Categorie categorie);

  /**
   * @throws RuntimeException if {@code id} does not exist
   */
  void deleteCategorie(Long id);

  /** Whether a category with this display name already exists. */
  boolean categorieExists(String nom);
}
