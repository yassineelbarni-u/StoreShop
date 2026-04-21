package com.storeshop.services.impl;

import com.storeshop.entities.Commande;
import com.storeshop.entities.CommandeItem;
import com.storeshop.entities.Produit;
import com.storeshop.entities.User;
import com.storeshop.repositories.CommandeRepository;
import com.storeshop.services.CommandeService;
import com.storeshop.services.ProduitService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implémentation du service de gestion des commandes.
 * Gère la création des commandes, la mutation des stocks, et le suivi du statut des commandes.
 * Les opérations sont transactionnelles pour garantir l'intégrité des données entre le stock et la commande.
 */
@Service
@Transactional
@AllArgsConstructor
public class CommandeServiceImpl implements CommandeService {

  private final CommandeRepository commandeRepository;
  private final ProduitService produitService;

  /**
   * Crée une nouvelle commande pour un utilisateur donné.
   * Vérifie la disponibilité des stocks, met à jour l'inventaire et enregistre la commande avec ses lignes.
   * 
   * @param user  L'utilisateur passant la commande.
   * @param items Map contenant les IDs des produits et leurs quantités respectives.
   * @return La commande créée et sauvegardée.
   * @throws RuntimeException Si le panier est vide ou si le stock est insuffisant pour un produit.
   */
  @Override
  public Commande createOrder(User user, Map<Long, Integer> items) {
    if (items == null || items.isEmpty()) {
      throw new RuntimeException("Le panier est vide");
    }

    Commande commande = new Commande();
    commande.setUser(user);
    commande.setCreatedAt(LocalDateTime.now());
    commande.setStatus("VALIDEE");

    double total = 0;

    for (Map.Entry<Long, Integer> entry : items.entrySet()) {
      Long produitId = entry.getKey();
      int quantity = entry.getValue();

      Produit produit = produitService.getProduitById(produitId);
      if (quantity > produit.getStock()) {
        throw new RuntimeException("Stock insuffisant pour: " + produit.getName());
      }

      produit.setStock(produit.getStock() - quantity);
      produitService.saveProduit(produit);

      CommandeItem item = new CommandeItem();
      item.setProduit(produit);
      item.setQuantity(quantity);
      item.setUnitPrice(produit.getPrice());
      item.setLineTotal(produit.getPrice() * quantity);
      commande.addItem(item);
      total += item.getLineTotal();
    }

    commande.setTotal(total);
    return commandeRepository.save(commande);
  }

  /**
   * Liste les commandes passées par un utilisateur spécifique.
   * 
   * @param user L'utilisateur concerné.
   * @return Liste des commandes triées par date décroissante.
   */
  @Override
  public List<Commande> listUserOrders(User user) {
    return commandeRepository.findByUserOrderByCreatedAtDesc(user);
  }

  /**
   * Liste toutes les commandes enregistrées dans le système.
   * 
   * @return Liste complète des commandes triées par date décroissante.
   */
  @Override
  public List<Commande> listAllOrders() {
    return commandeRepository.findAllByOrderByCreatedAtDesc();
  }

  /**
   * Met à jour le statut d'une commande (ex: EN_COURS, LIVREE).
   * 
   * @param commandeId L'ID de la commande.
   * @param status     Le nouveau statut.
   */
  @Override
  public void updateStatus(Long commandeId, String status) {
    Commande commande =
        commandeRepository
            .findById(commandeId)
            .orElseThrow(() -> new RuntimeException("Commande introuvable"));
    commande.setStatus(status);
    commandeRepository.save(commande);
  }
}
