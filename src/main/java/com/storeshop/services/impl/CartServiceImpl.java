package com.storeshop.services.impl;

import com.storeshop.entities.Produit;
import com.storeshop.models.Cart;
import com.storeshop.models.CartLine;
import com.storeshop.services.CartService;
import com.storeshop.services.ProduitService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implémentation du service de panier d'achat.
 * Le panier est stocké dans la session HTTP de l'utilisateur.
 * Ce service gère les opérations CRUD sur le panier et la construction des lignes de commande.
 */
@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

  /** Clé de session utilisée pour stocker l'objet Cart. */
  private static final String CART_SESSION_KEY = "CART";

  private final ProduitService produitService;

  /**
   * Récupère le panier de l'utilisateur à partir de la session.
   * Crée un nouveau panier s'il n'existe pas encore.
   * 
   * @param session La session HTTP.
   * @return L'objet Cart.
   */
  @Override
  public Cart getCart(HttpSession session) {
    Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
    if (cart == null) {
      cart = new Cart();
      session.setAttribute(CART_SESSION_KEY, cart);
    }
    return cart;
  }

  /**
   * Ajoute un produit au panier.
   * 
   * @param session   La session HTTP.
   * @param produitId L'ID du produit.
   * @param quantity  La quantité à ajouter.
   */
  @Override
  public void addItem(HttpSession session, Long produitId, int quantity) {
    Cart cart = getCart(session);
    cart.addItem(produitId, quantity);
  }

  /**
   * Met à jour la quantité d'un produit dans le panier.
   * 
   * @param session   La session HTTP.
   * @param produitId L'ID du produit.
   * @param quantity  La nouvelle quantité.
   */
  @Override
  public void updateItem(HttpSession session, Long produitId, int quantity) {
    Cart cart = getCart(session);
    cart.updateItem(produitId, quantity);
  }

  /**
   * Retire un produit du panier.
   * 
   * @param session   La session HTTP.
   * @param produitId L'ID du produit.
   */
  @Override
  public void removeItem(HttpSession session, Long produitId) {
    Cart cart = getCart(session);
    cart.removeItem(produitId);
  }

  /**
   * Vide complètement le panier.
   * 
   * @param session La session HTTP.
   */
  @Override
  public void clear(HttpSession session) {
    Cart cart = getCart(session);
    cart.clear();
  }

  /**
   * Calcule le nombre total d'articles dans le panier.
   * 
   * @param session La session HTTP.
   * @return Le nombre total.
   */
  @Override
  public int getTotalQuantity(HttpSession session) {
    Cart cart = getCart(session);
    int total = 0;
    for (Integer quantity : cart.getItems().values()) {
      total += quantity;
    }
    return total;
  }

  /**
   * Construit les lignes du panier avec les informations complètes des produits.
   * Récupère les données fraîches (prix, nom) depuis la base de données.
   * 
   * @param cart L'objet panier contenant les IDs et quantités.
   * @return Une liste de {@link CartLine}.
   */
  @Override
  public List<CartLine> buildLines(Cart cart) {
    List<CartLine> lines = new ArrayList<>();
    for (Map.Entry<Long, Integer> entry : cart.getItems().entrySet()) {
      Produit produit = produitService.getProduitById(entry.getKey());

      int quantity = entry.getValue();
      double price = produit.getPrice();

      double lineTotal = price * quantity;
      lines.add(new CartLine(produit, quantity, lineTotal));
    }
    return lines;
  }

  /**
   * Calcule le montant total cumulé de toutes les lignes du panier.
   * 
   * @param lines Liste des lignes calculées.
   * @return Le montant total.
   */
  @Override
  public double computeTotal(List<CartLine> lines) {
    double total = 0;
    for (CartLine line : lines) {
      total += line.getLineTotal();
    }
    return total;
  }
}
