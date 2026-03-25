package com.storeshop.services;

import com.storeshop.models.Cart;
import com.storeshop.models.CartLine;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * Shopping cart backed by the HTTP session. Mutations update the same session-bound {@link Cart}
 * instance so the storefront stays stateless on the server except for session storage.
 */
public interface CartService {

  /** Returns the cart for the session, creating and attaching an empty one if needed. */
  Cart getCart(HttpSession session);

  /** Adds quantity for a product id, or merges with existing line quantities. */
  void addItem(HttpSession session, Long produitId, int quantity);

  /** Sets the quantity for a line; behavior for zero quantity follows {@link Cart} rules. */
  void updateItem(HttpSession session, Long produitId, int quantity);

  void removeItem(HttpSession session, Long produitId);

  void clear(HttpSession session);

  /** Sum of all line quantities (not number of distinct products). */
  int getTotalQuantity(HttpSession session);

  /**
   * Resolves each cart entry to a {@link CartLine} with current product data and line subtotal.
   * Missing or invalid product ids may surface as errors from the product service.
   */
  List<CartLine> buildLines(Cart cart);

  /** Grand total from a list of lines (typically from {@link #buildLines(Cart)}). */
  double computeTotal(List<CartLine> lines);
}
