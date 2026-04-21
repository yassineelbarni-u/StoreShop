package com.storeshop.controllers;

import com.storeshop.services.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Conseiller de contrôleur pour les attributs de modèle partagés.
 * Injecte des attributs disponibles dans tous les templates Thymeleaf.
 */
@ControllerAdvice
@AllArgsConstructor
public class PublicModelAdvice {

  private final CartService cartService;

  /**
   * Ajoute le nombre total d'articles dans le panier au modèle.
   * Cet attribut est accessible via ${cartCount} dans les vues.
   * 
   * @param session La session HTTP actuelle.
   * @return Le nombre total d'articles.
   */
  @ModelAttribute("cartCount")
  public int cartCount(HttpSession session) {
    return cartService.getTotalQuantity(session);
  }
}
