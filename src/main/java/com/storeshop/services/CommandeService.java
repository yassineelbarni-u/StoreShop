package com.storeshop.services;

import com.storeshop.entities.Commande;
import com.storeshop.entities.User;
import java.util.List;
import java.util.Map;

/** Order lifecycle: creation from cart-like item maps, listing, and status updates for staff. */
public interface CommandeService {

  /**
   * Builds a validated order for the user: checks stock, decrements inventory, snapshots prices,
   * and persists the order in status {@code VALIDEE}. Runs in a transaction with the
   * implementation.
   *
   * @param user order owner
   * @param items product id → quantity from the cart
   * @return saved order with items and total
   * @throws RuntimeException if the cart is empty, a product is missing, or stock is insufficient
   */
  Commande createOrder(User user, Map<Long, Integer> items);

  /** Orders for that user, newest first. */
  List<Commande> listUserOrders(User user);

  /** All orders, newest first (admin). */
  List<Commande> listAllOrders();

  /**
   * Updates workflow status (e.g. shipped, cancelled) for an existing order.
   *
   * @throws RuntimeException if the order id is unknown
   */
  void updateStatus(Long commandeId, String status);
}
