package com.storeshop.controllers;

import com.storeshop.services.CommandeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur destiné à l'administration des commandes.
 * Permet de lister toutes les commandes et de modifier leur statut.
 */
@Controller
@RequestMapping("/admin/commandes")
@AllArgsConstructor
public class AdminCommandeController {

  private final CommandeService commandeService;

  /**
   * Affiche la liste complète des commandes pour l'administrateur.
   * 
   * @param model Le modèle pour la vue.
   * @return Le nom de la vue d'administration des commandes.
   */
  @GetMapping
  public String listOrders(Model model) {
    model.addAttribute("orders", commandeService.listAllOrders());
    return "admin/commandes";
  }

  /**
   * Met à jour le statut d'une commande spécifique.
   * 
   * @param id     L'ID de la commande.
   * @param status Le nouveau statut à appliquer.
   * @return Redirection vers la liste des commandes.
   */
  @PostMapping("/status")
  public String updateStatus(
      @RequestParam(name = "id") Long id, @RequestParam(name = "status") String status) {
    commandeService.updateStatus(id, status);
    return "redirect:/admin/commandes?success";
  }
}
