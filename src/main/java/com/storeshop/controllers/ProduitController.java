package com.storeshop.controllers;

import com.storeshop.entities.Categorie;
import com.storeshop.entities.Produit;
import com.storeshop.services.CategorieService;
import com.storeshop.services.ProduitService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Contrôleur pour la gestion administrative des produits.
 * Gère le cycle de vie des produits : affichage, création, modification, suppression et upload d'images.
 */
@Controller
@RequestMapping("/admin/produits")
@AllArgsConstructor
public class ProduitController {

  private final ProduitService produitService;
  private final CategorieService categorieService;

  /** Chemin pour le stockage des images uploadées. */
  private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

  /**
   * Affiche la liste des produits avec pagination et recherche.
   * 
   * @param model  Le modèle pour la vue.
   * @param page   Le numéro de la page.
   * @param size   Le nombre d'éléments par page.
   * @param search Le mot-clé de recherche.
   * @return Le nom de la vue de la liste des produits.
   */
  @GetMapping
  public String index(
      Model model,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "5") int size,
      @RequestParam(name = "search", defaultValue = "") String search) {

    Page<Produit> produitsPage = produitService.searchProduits(search, page, size);
    model.addAttribute("ListeProduit", produitsPage.getContent());
    model.addAttribute("pages", new int[produitsPage.getTotalPages()]);
    model.addAttribute("currentPage", page);
    model.addAttribute("search", search);
    return "produit/ListeProduit";
  }

  /**
   * Supprime un produit du catalogue.
   * 
   * @param id     L'ID du produit.
   * @param page   La page actuelle pour la redirection.
   * @param search Le terme de recherche actuel.
   * @return Redirection vers la liste des produits.
   */
  @GetMapping("/delete")
  public String deleteProduit(
      @RequestParam(name = "id") Long id,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "search", defaultValue = "") String search) {
    try {
      produitService.deleteProduit(id);
      return "redirect:/admin/produits?page=" + page + "&search=" + search;
    } catch (RuntimeException e) {
      return "redirect:/admin/produits?page="
          + page
          + "&search="
          + search
          + "&error="
          + e.getMessage();
    }
  }

  /**
   * Affiche le formulaire de modification d'un produit.
   * 
   * @param id     L'ID du produit à modifier.
   * @param search Le terme de recherche actuel.
   * @param model  Le modèle pour la vue.
   * @return Le nom de la vue du formulaire d'édition.
   */
  @GetMapping("/edit")
  public String showEditForm(
      @RequestParam(name = "id") Long id,
      @RequestParam(name = "search", defaultValue = "") String search,
      Model model) {
    Produit produit = produitService.getProduitById(id);
    List<Categorie> categories = categorieService.getAllCategories();

    model.addAttribute("produit", produit);
    model.addAttribute("categories", categories);
    model.addAttribute("search", search);

    return "produit/editProduit";
  }

  /**
   * Enregistre les modifications d'un produit.
   * 
   * @param produit     L'objet produit contenant les données modifiées.
   * @param categorieId L'ID de la catégorie associée.
   * @param search      Le terme de recherche actuel.
   * @param imageFile   Le fichier image optionnel.
   * @return Redirection vers la liste des produits ou le formulaire en cas d'erreur.
   */
  @PostMapping("/edit")
  @SuppressWarnings("CallToPrintStackTrace")
  public String saveProduit(
      @ModelAttribute Produit produit,
      @RequestParam(name = "categorieId", required = false) Long categorieId,
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "imageFile", required = false) MultipartFile imageFile) {

    if (categorieId != null) {
      Categorie categorie = categorieService.getCategorieById(categorieId);
      produit.setCategorie(categorie);
    }

    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String fileName = saveImageFile(imageFile);
        produit.setImageUrl("/uploads/" + fileName);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try {
      produitService.saveProduit(produit);
      return "redirect:/admin/produits?search=" + search;
    } catch (RuntimeException e) {
      return "redirect:/admin/produits/edit?id="
          + produit.getId()
          + "&search="
          + search
          + "&error="
          + e.getMessage();
    }
  }

  /**
   * Affiche le formulaire d'ajout d'un produit.
   * 
   * @param search Le terme de recherche actuel.
   * @param model  Le modèle pour la vue.
   * @return Le nom de la vue du formulaire d'ajout.
   */
  @GetMapping("/add")
  public String showAddForm(
      @RequestParam(name = "search", defaultValue = "") String search, Model model) {
    List<Categorie> categories = categorieService.getAllCategories();
    model.addAttribute("produit", new Produit());
    model.addAttribute("categories", categories);
    model.addAttribute("search", search);
    return "produit/ajouterProduit";
  }

  /**
   * Enregistre un nouveau produit.
   * 
   * @param produit     Le produit à ajouter.
   * @param categorieId L'ID de la catégorie.
   * @param search      Le terme de recherche actuel.
   * @param imageFile   Le fichier image uploadé.
   * @return Redirection vers le catalogue ou le formulaire en cas d'erreur.
   */
  @PostMapping("/add")
  @SuppressWarnings("CallToPrintStackTrace")
  public String addProduit(
      @ModelAttribute Produit produit,
      @RequestParam(name = "categorieId", required = false) Long categorieId,
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "imageFile", required = false) MultipartFile imageFile) {

    if (categorieId != null) {
      Categorie categorie = categorieService.getCategorieById(categorieId);
      produit.setCategorie(categorie);
    }

    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String fileName = saveImageFile(imageFile);
        produit.setImageUrl("/uploads/" + fileName);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try {
      produitService.saveProduit(produit);
      return "redirect:/admin/produits?search=" + search;
    } catch (RuntimeException e) {
      return "redirect:/admin/produits/add?search=" + search + "&error=" + e.getMessage();
    }
  }

  /**
   * Redirection vers le dashboard admin.
   * 
   * @return Redirection URL.
   */
  @GetMapping("/")
  public String home() {
    return "redirect:/admin/dashboard";
  }

  /**
   * Sauvegarde un fichier image sur le disque avec un nom unique.
   */
  private String saveImageFile(MultipartFile file) throws IOException {
    Path uploadPath = Paths.get(UPLOAD_DIR);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    String originalFileName = file.getOriginalFilename();
    String extension = "";
    if (originalFileName != null && originalFileName.contains(".")) {
      extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    }
    String fileName = UUID.randomUUID().toString() + extension;

    Path filePath = uploadPath.resolve(fileName);
    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    return fileName;
  }
}
