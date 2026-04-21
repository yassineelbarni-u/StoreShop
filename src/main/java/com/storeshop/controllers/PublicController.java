package com.storeshop.controllers;

import com.storeshop.entities.Produit;
import com.storeshop.services.AccountService;
import com.storeshop.services.CategorieService;
import com.storeshop.services.ProduitService;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;

/**
 * Contrôleur pour la partie publique du site.
 * Gère l'affichage des produits, l'inscription des utilisateurs et la consultation des détails produits.
 */
@Controller
@AllArgsConstructor
public class PublicController {

  private final ProduitService produitService;
  private final CategorieService categorieService;
  private final AccountService accountService;

  /**
   * Page d'accueil affichant la liste des produits avec filtres.
   * 
   * @param model       Le modèle pour la vue.
   * @param page        Index de la page.
   * @param size        Nombre de produits par page.
   * @param search      Critère de recherche par nom.
   * @param categorieId Filtre par catégorie.
   * @return Le nom de la vue d'accueil.
   */
  @GetMapping({"/", "/home", "/produits"})
  public String home(
      Model model,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "8") int size,
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "categorieId", required = false) Long categorieId) {

    Page<Produit> produitsPage =
        produitService.searchProduitsPublic(search, categorieId, page, size);
    model.addAttribute("ListeProduit", produitsPage.getContent());
    model.addAttribute("pages", new int[produitsPage.getTotalPages()]);
    model.addAttribute("currentPage", page);
    model.addAttribute("search", search);
    model.addAttribute("selectedCategorieId", categorieId);
    model.addAttribute("categories", categorieService.getAllCategories());
    return "public/home";
  }

  /**
   * Affiche les détails d'un produit spécifique.
   * 
   * @param id    L'ID du produit.
   * @param model Le modèle pour la vue.
   * @return La vue de détail du produit.
   */
  @GetMapping("/produits/detail")
  public String showProduitDetail(@RequestParam(name = "id") Long id, Model model) {
    model.addAttribute("produit", produitService.getProduitById(id));
    return "public/detail-produit";
  }

  /**
   * Affiche le formulaire d'inscription.
   * 
   * @return La vue d'inscription.
   */
  @GetMapping("/register")
  public String showRegisterForm() {
    return "public/register";
  }

  /**
   * Traite la demande d'inscription d'un nouvel utilisateur.
   * 
   * @param username        Nom d'utilisateur.
   * @param email           Email.
   * @param password        Mot de passe.
   * @param confirmPassword Confirmation du mot de passe.
   * @return Redirection vers login ou retour au formulaire avec erreur.
   */
  @PostMapping("/register")
  public String register(
      @RequestParam String username,
      @RequestParam String email,
      @RequestParam String password,
      @RequestParam String confirmPassword) {
    try {
      accountService.AddUser(username, password, email, confirmPassword);
      return "redirect:/login?registered";
    } catch (RuntimeException e) {
      return "redirect:/register?error="
          + encode(e.getMessage())
          + "&username="
          + encode(username)
          + "&email="
          + encode(email);
    }
  }

  /**
   * Utilitaire pour encoder les paramètres d'URL.
   * 
   * @param value La valeur à encoder.
   * @return La valeur encodée en UTF-8.
   */
  private String encode(String value) {
    return UriUtils.encode(value == null ? "" : value, StandardCharsets.UTF_8);
  }
}
