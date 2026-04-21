package com.storeshop.repositories;

import com.storeshop.entities.Commande;
import com.storeshop.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface de gestion de la persistance pour les commandes.
 */
public interface CommandeRepository extends JpaRepository<Commande, Long> {

  /**
   * Récupère la liste des commandes d'un utilisateur, triées par date de création décroissante.
   * 
   * @param user L'utilisateur concerné.
   * @return Liste des commandes.
   */
  List<Commande> findByUserOrderByCreatedAtDesc(User user);

  /**
   * Récupère toutes les commandes du système, triées par date de création décroissante.
   * 
   * @return Liste de toutes les commandes.
   */
  List<Commande> findAllByOrderByCreatedAtDesc();
}
