package com.storeshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur gérant l'accès à la page de connexion.
 */
@Controller
public class LoginController {

  /**
   * Affiche la page de connexion personnalisée.
   * 
   * @return Le nom de la vue de connexion.
   */
  @GetMapping("/login")
  public String login() {
    return "login/login";
  }
}
