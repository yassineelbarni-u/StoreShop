package com.storeshop.controllers;

import com.storeshop.entities.User;
import com.storeshop.models.Cart;
import com.storeshop.services.AccountService;
import com.storeshop.services.CartService;
import com.storeshop.services.CommandeService;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contrôleur gérant les commandes coté client.
 * Permet de lister l'historique des commandes et de finaliser un achat (checkout).
 */
@Controller
@RequestMapping("/commandes")
@AllArgsConstructor
public class OrderController {

  private final AccountService accountService;
  private final CommandeService commandeService;
  private final CartService cartService;

  /**
   * Liste les commandes de l'utilisateur connecté.
   * 
   * @param model     Le modèle pour la vue.
   * @param principal L'objet d'authentification de l'utilisateur.
   * @return Le nom de la vue de l'historique des commandes.
   */
  @GetMapping
  public String listOrders(Model model, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    User user = accountService.loadUserByUsername(principal.getName());
    model.addAttribute("orders", commandeService.listUserOrders(user));
    return "public/commandes";
  }

  /**
   * Finalise la commande à partir du contenu du panier.
   * 
   * @param session   La session HTTP.
   * @param principal L'objet d'authentification de l'utilisateur.
   * @return Redirection vers l'historique des commandes ou le panier en cas d'erreur.
   */
  @PostMapping("/checkout")
  public String checkout(HttpSession session, Principal principal) {
    if (principal == null) {
      return "redirect:/login";
    }
    Cart cart = cartService.getCart(session);
    User user = accountService.loadUserByUsername(principal.getName());
    try {
      commandeService.createOrder(user, cart.getItems());
      cartService.clear(session);
      return "redirect:/commandes?success";
    } catch (RuntimeException e) {
      return "redirect:/panier?error=" + e.getMessage();
    }
  }
}
