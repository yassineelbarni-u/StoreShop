package com.storeshop.controllers;

import com.storeshop.entities.Categorie;
import com.storeshop.services.CategorieService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur pour la gestion des catégories par l'administrateur.
 * Permet de lister, créer, modifier et supprimer des catégories.
 */
@Controller
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategorieController {

  private final CategorieService categorieService;

  /**
   * Affiche la liste de toutes les catégories.
   * 
   * @param model Le modèle pour la vue.
   * @return Le nom de la vue de la liste des catégories.
   */
  @GetMapping
  public String index(Model model) {
    List<Categorie> categories = categorieService.getAllCategories();
    model.addAttribute("listeCategories", categories);
    return "categorie/ListeCategorie";
  }

  /**
   * Affiche le formulaire d'ajout d'une nouvelle catégorie.
   * 
   * @param model Le modèle pour la vue.
   * @return Le nom de la vue du formulaire d'ajout.
   */
  @GetMapping("/add")
  public String showAddForm(Model model) {
    model.addAttribute("categorie", new Categorie());
    return "categorie/ajouterCategorie";
  }

  /**
   * Enregistre une nouvelle catégorie.
   * 
   * @param categorie L'objet catégorie à enregistrer.
   * @return Redirection vers la liste des catégories ou vers le formulaire en cas d'erreur.
   */
  @PostMapping("/add")
  public String addCategorie(@ModelAttribute Categorie categorie) {
    try {
      categorieService.saveCategorie(categorie);
      return "redirect:/admin/categories";
    } catch (RuntimeException e) {
      return "redirect:/admin/categories/add?error=" + e.getMessage();
    }
  }

  /**
   * Affiche le formulaire de modification d'une catégorie existante.
   * 
   * @param id    L'ID de la catégorie à modifier.
   * @param model Le modèle pour la vue.
   * @return Le nom de la vue du formulaire d'édition.
   */
  @GetMapping("/edit")
  public String showEditForm(@RequestParam(name = "id") Long id, Model model) {
    Categorie categorie = categorieService.getCategorieById(id);
    model.addAttribute("categorie", categorie);
    return "categorie/editCategorie";
  }

  /**
   * Met à jour une catégorie existante.
   * 
   * @param categorie L'objet catégorie modifié.
   * @return Redirection vers la liste des catégories ou vers le formulaire en cas d'erreur.
   */
  @PostMapping("/edit")
  public String editCategorie(@ModelAttribute Categorie categorie) {
    try {
      categorieService.saveCategorie(categorie);
      return "redirect:/admin/categories";
    } catch (RuntimeException e) {
      return "redirect:/admin/categories/edit?id=" + categorie.getId() + "&error=" + e.getMessage();
    }
  }

  /**
   * Supprime une catégorie.
   * 
   * @param id L'ID de la catégorie à supprimer.
   * @return Redirection vers la liste des catégories.
   */
  @GetMapping("/delete")
  public String deleteCategorie(@RequestParam(name = "id") Long id) {
    try {
      categorieService.deleteCategorie(id);
      return "redirect:/admin/categories";
    } catch (RuntimeException e) {
      return "redirect:/admin/categories?error=" + e.getMessage();
    }
  }
}
