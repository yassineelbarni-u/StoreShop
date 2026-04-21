package com.storeshop.controllers;

import com.storeshop.models.Cart;
import com.storeshop.models.CartLine;
import com.storeshop.services.CartService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur gérant les opérations sur le panier d'achat.
 * Permet d'ajouter, modifier ou supprimer des articles du panier stocké en session.
 */
@Controller
@RequestMapping("/panier")
@AllArgsConstructor
public class CartController {

  private final CartService cartService;

  /**
   * Affiche le contenu actuel du panier.
   * 
   * @param model   Le modèle pour la vue.
   * @param session La session HTTP.
   * @return Le nom de la vue du panier.
   */
  @GetMapping
  public String viewCart(Model model, HttpSession session) {
    Cart cart = cartService.getCart(session);
    List<CartLine> lines = cartService.buildLines(cart);
    model.addAttribute("lines", lines);
    model.addAttribute("total", cartService.computeTotal(lines));
    return "public/panier";
  }

  /**
   * Ajoute un produit au panier.
   * 
   * @param produitId  L'ID du produit à ajouter.
   * @param quantity   La quantité à ajouter.
   * @param returnUrl  L'URL de retour après ajout (optionnel).
   * @param session    La session HTTP.
   * @return Redirection vers le panier ou l'URL de retour.
   */
  @PostMapping("/add")
  public String addItem(
      @RequestParam(name = "produitId") Long produitId,
      @RequestParam(name = "quantity", defaultValue = "1") int quantity,
      @RequestParam(name = "returnUrl", required = false) String returnUrl,
      HttpSession session) {

    cartService.addItem(session, produitId, quantity);
    if (returnUrl != null && returnUrl.startsWith("/")) {
      return "redirect:" + returnUrl;
    }
    return "redirect:/panier";
  }

  /**
   * Met à jour la quantité d'un produit dans le panier.
   * 
   * @param produitId L'ID du produit.
   * @param quantity  La nouvelle quantité.
   * @param session   La session HTTP.
   * @return Redirection vers la vue du panier.
   */
  @PostMapping("/update")
  public String updateItem(
      @RequestParam(name = "produitId") Long produitId,
      @RequestParam(name = "quantity") int quantity,
      HttpSession session) {

    cartService.updateItem(session, produitId, quantity);
    return "redirect:/panier";
  }

  /**
   * Retire un produit du panier.
   * 
   * @param produitId L'ID du produit à retirer.
   * @param session   La session HTTP.
   * @return Redirection vers la vue du panier.
   */
  @PostMapping("/remove")
  public String removeItem(@RequestParam(name = "produitId") Long produitId, HttpSession session) {

    cartService.removeItem(session, produitId);
    return "redirect:/panier";
  }

  /**
   * Vide complètement le panier.
   * 
   * @param session La session HTTP.
   * @return Redirection vers la vue du panier.
   */
  @PostMapping("/clear")
  public String clearCart(HttpSession session) {
    cartService.clear(session);
    return "redirect:/panier";
  }
}
